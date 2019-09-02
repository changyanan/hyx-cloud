package com.globalegrow.scg.rabbitmq.autoconfigure;

import java.time.Duration;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.config.DirectRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

class RabbitBeanFactory {
	
	public static CachingConnectionFactory rabbitConnectionFactory(
			RabbitProperties properties)
			throws Exception {
		PropertyMapper map = PropertyMapper.get();
		CachingConnectionFactory factory = new CachingConnectionFactory(
				getRabbitConnectionFactoryBean(properties).getObject());
		map.from(properties::determineAddresses).to(factory::setAddresses);
		map.from(properties::isPublisherConfirms).to(factory::setPublisherConfirms);
		map.from(properties::isPublisherReturns).to(factory::setPublisherReturns);
		RabbitProperties.Cache.Channel channel = properties.getCache().getChannel();
		map.from(channel::getSize).whenNonNull().to(factory::setChannelCacheSize);
		map.from(channel::getCheckoutTimeout).whenNonNull().as(Duration::toMillis)
				.to(factory::setChannelCheckoutTimeout);
		RabbitProperties.Cache.Connection connection = properties.getCache()
				.getConnection();
		map.from(connection::getMode).whenNonNull().to(factory::setCacheMode);
		map.from(connection::getSize).whenNonNull()
				.to(factory::setConnectionCacheSize);
//		map.from(connectionNameStrategy::getIfUnique).whenNonNull()
//				.to(factory::setConnectionNameStrategy);
		return factory;
	}

	private static RabbitConnectionFactoryBean getRabbitConnectionFactoryBean(
			RabbitProperties properties) throws Exception {
		PropertyMapper map = PropertyMapper.get();
		RabbitConnectionFactoryBean factory = new RabbitConnectionFactoryBean();
		map.from(properties::determineHost).whenNonNull().to(factory::setHost);
		map.from(properties::determinePort).to(factory::setPort);
		map.from(properties::determineUsername).whenNonNull()
				.to(factory::setUsername);
		map.from(properties::determinePassword).whenNonNull()
				.to(factory::setPassword);
		map.from(properties::determineVirtualHost).whenNonNull()
				.to(factory::setVirtualHost);
		map.from(properties::getRequestedHeartbeat).whenNonNull()
				.asInt(Duration::getSeconds).to(factory::setRequestedHeartbeat);
		RabbitProperties.Ssl ssl = properties.getSsl();
		if (ssl.isEnabled()) {
			factory.setUseSSL(true);
			map.from(ssl::getAlgorithm).whenNonNull().to(factory::setSslAlgorithm);
			map.from(ssl::getKeyStoreType).to(factory::setKeyStoreType);
			map.from(ssl::getKeyStore).to(factory::setKeyStore);
			map.from(ssl::getKeyStorePassword).to(factory::setKeyStorePassphrase);
			map.from(ssl::getTrustStoreType).to(factory::setTrustStoreType);
			map.from(ssl::getTrustStore).to(factory::setTrustStore);
			map.from(ssl::getTrustStorePassword).to(factory::setTrustStorePassphrase);
		}
		map.from(properties::getConnectionTimeout).whenNonNull()
				.asInt(Duration::toMillis).to(factory::setConnectionTimeout);
		factory.afterPropertiesSet();
		return factory;
	}

	public static RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter,
			RabbitProperties rabbitProperties) {
		PropertyMapper map = PropertyMapper.get();
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		if (messageConverter != null) {
			template.setMessageConverter(messageConverter);
		}
		template.setMandatory(determineMandatoryFlag(rabbitProperties));
		RabbitProperties.Template properties = rabbitProperties.getTemplate();
		if (properties.getRetry().isEnabled()) {
			template.setRetryTemplate(createRetryTemplate(properties.getRetry()));
		}
		map.from(properties::getReceiveTimeout).whenNonNull().as(Duration::toMillis)
				.to(template::setReceiveTimeout);
		map.from(properties::getReplyTimeout).whenNonNull().as(Duration::toMillis)
				.to(template::setReplyTimeout);
		map.from(properties::getExchange).to(template::setExchange);
		map.from(properties::getRoutingKey).to(template::setRoutingKey);
		return template;
	}

	private static boolean determineMandatoryFlag(RabbitProperties rabbitProperties) {
		Boolean mandatory = rabbitProperties.getTemplate().getMandatory();
		return (mandatory != null ? mandatory : rabbitProperties.isPublisherReturns());
	}

	private static RetryTemplate createRetryTemplate(RabbitProperties.Retry properties) {
		PropertyMapper map = PropertyMapper.get();
		RetryTemplate template = new RetryTemplate();
		SimpleRetryPolicy policy = new SimpleRetryPolicy();
		map.from(properties::getMaxAttempts).to(policy::setMaxAttempts);
		template.setRetryPolicy(policy);
		ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
		map.from(properties::getInitialInterval).whenNonNull().as(Duration::toMillis)
				.to(backOffPolicy::setInitialInterval);
		map.from(properties::getMultiplier).to(backOffPolicy::setMultiplier);
		map.from(properties::getMaxInterval).whenNonNull().as(Duration::toMillis)
				.to(backOffPolicy::setMaxInterval);
		template.setBackOffPolicy(backOffPolicy);
		return template;
	}




	public static AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}

	public static RabbitMessagingTemplate rabbitMessagingTemplate(RabbitTemplate rabbitTemplate) {
		RabbitMessagingTemplate rabbitMessagingTemplate = new RabbitMessagingTemplate(rabbitTemplate);
		return rabbitMessagingTemplate;
	}

	public static SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
			ConnectionFactory connectionFactory, MessageConverter messageConverter, MessageRecoverer messageRecoverer,
			RabbitProperties rabbitProperties) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		SimpleRabbitListenerContainerFactoryConfigurer.build(messageConverter, messageRecoverer, rabbitProperties).configure(factory, connectionFactory);
		return factory;
	}

	public DirectRabbitListenerContainerFactory directRabbitListenerContainerFactory(ConnectionFactory connectionFactory, MessageConverter messageConverter, MessageRecoverer messageRecoverer,
			RabbitProperties rabbitProperties) {
		DirectRabbitListenerContainerFactory factory = new DirectRabbitListenerContainerFactory();
		DirectRabbitListenerContainerFactoryConfigurer.build(messageConverter, messageRecoverer, rabbitProperties).configure(factory, connectionFactory);
		return factory;
	}
}