package com.globalegrow.scg.rabbitmq.autoconfigure;

import org.springframework.amqp.rabbit.config.DirectRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.AbstractRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.PropertyMapper;

public final class DirectRabbitListenerContainerFactoryConfigurer extends
		AbstractRabbitListenerContainerFactoryConfigurer<DirectRabbitListenerContainerFactory> {

	@Override
	public void configure(DirectRabbitListenerContainerFactory factory,
			ConnectionFactory connectionFactory) {
		PropertyMapper map = PropertyMapper.get();
		RabbitProperties.DirectContainer config = getRabbitProperties().getListener()
				.getDirect();
		configure(factory, connectionFactory, config);
		map.from(config::getConsumersPerQueue).whenNonNull()
				.to(factory::setConsumersPerQueue);
	}
	
	public static AbstractRabbitListenerContainerFactoryConfigurer<DirectRabbitListenerContainerFactory> build(MessageConverter messageConverter, MessageRecoverer messageRecoverer, RabbitProperties properties) {
		DirectRabbitListenerContainerFactoryConfigurer configurer=new DirectRabbitListenerContainerFactoryConfigurer();
		configurer.setMessageConverter(messageConverter);
		configurer.setMessageRecoverer(messageRecoverer);
		configurer.setRabbitProperties(properties);
		return configurer;
	}

}