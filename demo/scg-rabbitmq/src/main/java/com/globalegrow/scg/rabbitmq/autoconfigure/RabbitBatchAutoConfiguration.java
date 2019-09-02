package com.globalegrow.scg.rabbitmq.autoconfigure;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.globalegrow.scg.rabbitmq.properties.RabbitBatchProperties;
import com.rabbitmq.client.Channel;

@Configuration
@ConditionalOnClass({ RabbitTemplate.class, Channel.class })
@AutoConfigureBefore(RabbitAutoConfiguration.class)
@EnableConfigurationProperties(RabbitBatchProperties.class)
public class RabbitBatchAutoConfiguration implements BeanDefinitionRegistryPostProcessor,EnvironmentAware  {

	private Environment environment;
	private static AtomicBoolean atomicBoolean=new AtomicBoolean(false);

	@Override
	public void setEnvironment(Environment environment) {
		this.environment=environment;
	}

	private void register(String name,RabbitProperties rabbitProperties,MessageConverter messageConverter, MessageRecoverer messageRecoverer) {
		String connectionFactoryname=this.captureName(name + "ConnectionFactory");
		String rabbitTemplateName=this.captureName(name + "RabbitTemplate");
		String rabbitListenerContainerFactoryName=this.captureName(name + "RabbitListenerContainerFactory");
		String amqpAdminName=this.captureName(name + "AmqpAdmin");
		String rabbitMessagingTemplateName=this.captureName(name + "RabbitMessagingTemplate");

		BeanRegisterUtlis.register(connectionFactoryname, 				RabbitBeanFactory.class, "rabbitConnectionFactory", 		rabbitProperties);
		BeanRegisterUtlis.register(rabbitTemplateName, 					RabbitBeanFactory.class, "rabbitTemplate", 					connectionFactoryname, messageConverter , 	rabbitProperties);
		BeanRegisterUtlis.register(rabbitListenerContainerFactoryName, 	RabbitBeanFactory.class, "rabbitListenerContainerFactory", 	connectionFactoryname, messageConverter,	messageRecoverer, rabbitProperties);
		BeanRegisterUtlis.register(amqpAdminName, 						RabbitBeanFactory.class, "amqpAdmin", 						connectionFactoryname);
		BeanRegisterUtlis.register(rabbitMessagingTemplateName, 		RabbitBeanFactory.class, "rabbitMessagingTemplate", 		rabbitTemplateName);
	}

	private String captureName(String name) {
		char[] cs = name.trim().toCharArray();
		if(cs[0]>='A'&&cs[0]<='Z') {
			cs[0] += 32;
		}
		return String.valueOf(cs);
	}
	private RabbitBatchProperties getRabbitBatchProperties() {
		try {
			return ConfigUtils.getConfig(environment, RabbitBatchProperties.prefix, RabbitBatchProperties.class);
		} catch (Exception e) {
			return null;
		}
	}
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		if(atomicBoolean.getAndSet(true)) {
			return;
		}
		MessageConverter messageConverter=this.getConverter(beanFactory);

		MessageRecoverer messageRecoverer=this.getBean(beanFactory,MessageRecoverer.class);
		RabbitBatchProperties batchProperties=this.getRabbitBatchProperties();
		RabbitProperties properties=ConfigUtils.getConfig(environment, RabbitBatchProperties.prefix, RabbitProperties.class);
		this.register("", properties,messageConverter,messageRecoverer);
		if ( batchProperties != null &&batchProperties.getBatch() != null && ! batchProperties.getBatch().isEmpty()) {
			 batchProperties.getBatch().forEach((name, rabbitProperties) -> {
				this.register(name, rabbitProperties,messageConverter,messageRecoverer);
			});
		}
	}
	private <T>  T getBean(BeanFactory beanFactory, Class<T> claz) {
		try {
			return beanFactory.getBean(claz);
		} catch (NoUniqueBeanDefinitionException e) {
			throw e;
		} catch (Exception e) {
			return null;
		}
	}

	private SimpleModule simpleModule() {
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
		simpleModule.addSerializer(long.class, ToStringSerializer.instance);
		simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

		return simpleModule;
	}

	private MessageConverter getConverter(BeanFactory beanFactory) {
		MessageConverter messageConverter = this.getBean(beanFactory,MessageConverter.class);
		if(messageConverter!=null) {
			return messageConverter;
		}
		ObjectMapper objectMapper=new ObjectMapper();
		objectMapper.registerModule(simpleModule());
		return new Jackson2JsonMessageConverter(objectMapper);
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
//		if(atomicBoolean.getAndSet(true)) {
//			return;
//		}
//		MessageConverter messageConverter=this.getBean((BeanFactory)registry,MessageConverter.class);
//		MessageRecoverer messageRecoverer=this.getBean((BeanFactory)registry,MessageRecoverer.class);
//		RabbitBatchProperties batchProperties=ConfigUtils.getConfig(environment, RabbitBatchProperties.prefix, RabbitBatchProperties.class);
//		RabbitProperties properties=ConfigUtils.getConfig(environment, RabbitBatchProperties.prefix, RabbitProperties.class);
//		this.register("", properties,messageConverter,messageRecoverer);
//		Map<String, RabbitProperties> batch = batchProperties.getBatch();
//		if (batch != null && !batch.isEmpty()) {
//			batch.forEach((name, rabbitProperties) -> {
//				this.register(name, rabbitProperties,messageConverter,messageRecoverer);
//			});
//		}
	}
}
