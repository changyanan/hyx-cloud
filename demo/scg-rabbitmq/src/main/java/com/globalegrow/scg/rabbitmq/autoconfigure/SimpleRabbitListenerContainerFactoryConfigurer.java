package com.globalegrow.scg.rabbitmq.autoconfigure;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.AbstractRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.PropertyMapper;

public final class SimpleRabbitListenerContainerFactoryConfigurer extends
		AbstractRabbitListenerContainerFactoryConfigurer<SimpleRabbitListenerContainerFactory> {
	public static AbstractRabbitListenerContainerFactoryConfigurer<SimpleRabbitListenerContainerFactory> build(MessageConverter messageConverter, MessageRecoverer messageRecoverer, RabbitProperties properties) {
		SimpleRabbitListenerContainerFactoryConfigurer configurer=new SimpleRabbitListenerContainerFactoryConfigurer();
		configurer.setMessageConverter(messageConverter);
		configurer.setMessageRecoverer(messageRecoverer);
		configurer.setRabbitProperties(properties);
		return configurer;
	}
	@Override
	public void configure(SimpleRabbitListenerContainerFactory factory,
			ConnectionFactory connectionFactory) {
		PropertyMapper map = PropertyMapper.get();
		RabbitProperties.SimpleContainer config = getRabbitProperties().getListener()
				.getSimple();
		configure(factory, connectionFactory, config);
		map.from(config::getConcurrency).whenNonNull()
				.to(factory::setConcurrentConsumers);
		map.from(config::getMaxConcurrency).whenNonNull()
				.to(factory::setMaxConcurrentConsumers);
		map.from(config::getTransactionSize).whenNonNull().to(factory::setTxSize);
	}

}