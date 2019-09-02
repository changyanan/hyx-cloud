package com.globalegrow.async.protocol.impl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.globalegrow.async.core.NotAckException;
import com.globalegrow.async.protocol.Protocol;
import com.globalegrow.async.protocol.RabbitMqProperties;
import com.hyx.core.utils.AsyncUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.RpcServer;
import com.rabbitmq.client.ShutdownSignalException;

@Component
@EnableConfigurationProperties(RabbitMqProperties.class)
public class RabbitmqProtocol implements Protocol, ApplicationContextAware, ApplicationListener<ContextClosedEvent> {

	private static final Logger log = LoggerFactory.getLogger(RabbitmqProtocol.class);

	private static final String uuid = UUID.randomUUID().toString().replaceAll("-", "");
	private final AtomicInteger index = new AtomicInteger(0);
	private final AtomicInteger notAckSize = new AtomicInteger(0);
	private final AtomicBoolean iscloseEd = new AtomicBoolean(false);
	private final List<String> consumerTags = new LinkedList<>();

	private RabbitTemplate rabbitTemplate;
	ConnectionFactory connectionFactory;
	@Autowired
	RabbitMqProperties rabbitMqProperties;
	@Autowired
	TaskExecutor taskExecutor;
	int queueSize = 0;
	ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
		Map<String, RabbitTemplate> rabbitTemplates = applicationContext.getBeansOfType(RabbitTemplate.class);
		if (rabbitTemplates.containsKey(rabbitMqProperties.getRabbitTemplateBeanName())) {
			rabbitTemplate = rabbitTemplates.get(rabbitMqProperties.getRabbitTemplateBeanName());
			connectionFactory = rabbitTemplate.getConnectionFactory();
		}
		Assert.notNull(rabbitTemplate, "请设置正确的rabbitTemplate");
		queueSize = rabbitMqProperties.getQueueNames().size();
		Assert.notEmpty(rabbitMqProperties.getQueueNames(), "使用rabbitmq作为async介质必须设置传递数据的队列");
	}

	@Override
	public String getName() {
		return "x-application/rabbitmq";
	}

	@Override
	public void send(byte[] args) {
		int id = index.updateAndGet(a -> ++a % queueSize);
		String routingKey = rabbitMqProperties.getQueueNames().get(id);
		log.debug("当前消息被发送到 {}", routingKey);
		rabbitTemplate.send(rabbitMqProperties.getExchangeName(), routingKey,
				new Message(args, new MessageProperties()));
	}

	private Channel getChannel(String consumerTag) {
		return connectionFactory.createConnection().createChannel(false);
	}

	@Override
	public void receive(Consumer<byte[]> consumer) {
		log.info("开始通过mq接收异步消息。。。");
		List<String> queueNames = rabbitMqProperties.getQueueNames();
		int clientCount = rabbitMqProperties.getClientCount();
		for (String queueName : queueNames) {
			for (int client = 0; client < clientCount; client++) {
				String consumerTag = queueName + "-" + uuid + "-" + client;
				consumerTags.add(this.basicConsume(consumer, queueName, consumerTag));
			}
		}
	}

	private String basicConsume(Consumer<byte[]> consumer, String queueName, String consumerTag) {
		CancelCallback cancelCallback = (String consumerTagStr) -> {
			consumerTags.remove(consumerTagStr);
			if (!iscloseEd.get()) {
				log.info("消费者{} 被取消，尝试重新启动", consumerTagStr);
				consumerTags.add(this.basicConsume(consumer, queueName, consumerTagStr));
			}
		};

		try {
			int concurrency = rabbitMqProperties.getConcurrency();
			Channel channel = getChannel(consumerTag);
			channel.basicQos(concurrency);
			RpcServer rpcServer=new RpcServer(channel, queueName);
			rpcServer.mainloop();
			return channel.basicConsume(queueName, false, consumerTag, false, false, null,
					(String consumerTagStr, Delivery message) -> {
						log.debug("从队列{}收到消息", message.getEnvelope());
						if (iscloseEd.get()) {
							channel.basicNack(message.getEnvelope().getDeliveryTag(), true, true);
							log.debug("服务开始关闭收到的消息直接Nack {}", message.getEnvelope());
							return;
						}
						AsyncUtils.execute(() -> {
							try {
								notAckSize.incrementAndGet();
								handle(consumer, message, consumerTag);
							} catch (IOException e) {
								log.warn("确认ack消息异常 {} ", message.getEnvelope(), e);
							} finally {
								notAckSize.decrementAndGet();
							}

						});
					}, cancelCallback, (String consumerTagStr, ShutdownSignalException sig) -> {
						log.info("服务{}开始关闭....", consumerTagStr, sig);
					});
		} catch (ShutdownSignalException ie) {
			log.info("rabbitmq队列开始关闭,{}", ie.getMessage());
			throw ie;
		} catch (IOException e) {
			log.info("接受rabbitmq消息出现异常", e);
			throw new RuntimeException(e);
		}
	}

	private void handle(Consumer<byte[]> consumer, Delivery message, String consumerTag) throws IOException {
		long deliveryTag = message.getEnvelope().getDeliveryTag();
		try {
			consumer.accept(message.getBody());
			this.basicAck(consumerTag, deliveryTag);
		} catch (NotAckException ne) {
			this.basicNack(consumerTag, deliveryTag);
		} catch (Exception e) {
			log.info("接受rabbitmq消息出现异常", e);
			this.basicAck(consumerTag, deliveryTag);
		}
	}

	private void basicAck(String consumerTag, Long deliveryTag) {
		for (int i = 0; i < 3; i++) {
			try {
				Channel channel = getChannel(consumerTag);
				channel.basicAck(deliveryTag, false);
				break;
			} catch (IOException e) {
				log.warn("消息ack失败", consumerTag, deliveryTag, e);
			}
		}
	}

	private void basicNack(String consumerTag, Long deliveryTag) {
		for (int i = 0; i < 3; i++) {
			try {
				Channel channel = getChannel(consumerTag);
				channel.basicNack(deliveryTag, true, true);
				break;
			} catch (IOException e) {
				log.warn("消息ack失败", consumerTag, deliveryTag, e);
			}
		}
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		consumerTags.forEach(consumerTag -> {
			try {
				Channel channel = getChannel(consumerTag);
				channel.basicCancel(consumerTag);
			} catch (IOException e) {
				log.info("取消消费者失败", e);
			}
		});
		int size = 0;
		while (notAckSize.get() > 0) {
			if (size++ >= 120) {
				log.info("等待超时正常关机失败");
				break;
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				log.info("等待失败", e);
				break;
			}
		}
		log.info("RabbitmqProtocol 等待关机结束");
	}

}
