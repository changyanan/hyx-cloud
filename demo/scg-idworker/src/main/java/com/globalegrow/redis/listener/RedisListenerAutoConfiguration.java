//package com.globalegrow.redis.listener;
//
//import java.lang.reflect.Method;
//import java.util.concurrent.Executor;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.AnnotationUtils;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisKeyValueAdapter;
//import org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents;
//import org.springframework.data.redis.core.RedisOperations;
//import org.springframework.data.redis.listener.PatternTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import org.springframework.util.NumberUtils;
//
//@Configuration
//public class RedisListenerAutoConfiguration
//		implements BeanPostProcessor, BeanFactoryPostProcessor, ApplicationContextAware {
//	private static final Logger LOGGER = LoggerFactory.getLogger(RedisListenerAutoConfiguration.class);
//	private final static RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//	private static ConfigurableListableBeanFactory beanFactory;
//	private static ApplicationContext applicationContext;
//	private static RedisSerializer<String> stringSerializer = new StringRedisSerializer();
//	
//	@Autowired(required=false)
//	private Executor redisTaskExecutor;
//	@Autowired(required=false)
//	private Executor redisSubscriptionExecutor;
//	private static RedisSerializer<?> byteSerializer = new RedisSerializer<byte[]>() {
//		@Override
//		public byte[] deserialize(byte[] bytes) { return bytes; }
//
//		@Override
//		public byte[] serialize(byte[] obj) { return obj; }
//	};
//
//	private static RedisSerializer<?> getRedisSerializer(Class<?> type) {
//		if (String.class.isAssignableFrom(type)) {
//			return stringSerializer;
//		} else if (byte[].class.isAssignableFrom(type)) {
//			return byteSerializer;
//		} else if (Number.class.isAssignableFrom(type)) {
//			return new RedisSerializer<Number>() {
//				@SuppressWarnings("unchecked")
//				@Override
//				public Number deserialize(byte[] bytes) { return NumberUtils.parseNumber(new String(bytes), (Class<Number>) type); }
//				@Override
//				public byte[] serialize(Number obj) { return String.valueOf(obj).getBytes(); }
//			};
//		}
//		return new Jackson2JsonRedisSerializer<>(type);
//	}
//
//	@Override
//	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//		RedisListenerAutoConfiguration.beanFactory = beanFactory;
//	}
//
//	@Override
//	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//		RedisListenerAutoConfiguration.applicationContext = applicationContext;
//	}
//
//	@Override
//	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//		return bean;
//	}
//
//	@Override
//	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//		this.listenerAdapter(bean, beanName);
//		return bean;
//	}
//
//	void listenerAdapter(Object bean, String beanName) {
//		Method[] methods = bean.getClass().getMethods();
//		for (Method method : methods) {
//			RedisListener redisListener = AnnotationUtils.findAnnotation(method, RedisListener.class);
//			if (redisListener != null) {
//				MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(bean, method.getName());
//				Class<?>[] parameterTypes = method.getParameterTypes();
//				if (parameterTypes.length > 0) {
//					Class<?> type = parameterTypes[0];
//					listenerAdapter.setSerializer(getRedisSerializer(type));
//				}
//				container.addMessageListener(listenerAdapter, new PatternTopic(redisListener.channelPattern()));
//				String listenerName = beanName + "-" + method.getName() + "-listener";
//				int num = 0;
//				while (beanFactory.containsBean(listenerName)) {
//					listenerName = beanName + "-" + method.getName() + num++ + "-listener";
//				}
//				beanFactory.registerSingleton(listenerName, listenerAdapter);
//				LOGGER.debug("注册redis listener {}", listenerName);
//				listenerAdapter.afterPropertiesSet();
//				LOGGER.debug("redis订阅{}完成,method:{}", redisListener.channelPattern(), method.getName());
//			}
//		}
//	}
//
//	@Bean
//	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
//		container.setConnectionFactory(connectionFactory);
//		if (this.redisTaskExecutor != null) {
//			container.setTaskExecutor(this.redisTaskExecutor);
//		}
//		if (this.redisSubscriptionExecutor != null) {
//			container.setSubscriptionExecutor(this.redisSubscriptionExecutor);
//		}
//		return container;
//	}
//
//	@Bean
//	RedisKeyValueAdapter redisKeyValueAdapter(RedisOperations<?, ?> stringRedisTemplate) {
//		RedisKeyValueAdapter redisKeyValueAdapter = new RedisKeyValueAdapter(stringRedisTemplate);
//		redisKeyValueAdapter.setEnableKeyspaceEvents(EnableKeyspaceEvents.ON_STARTUP);
//		redisKeyValueAdapter.setKeyspaceNotificationsConfigParameter("Ex");
//		redisKeyValueAdapter.setApplicationContext(applicationContext);
//		redisKeyValueAdapter.afterPropertiesSet();
//		return redisKeyValueAdapter;
//	}
//}
