//package com.globalegrow.scg.redis.template;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisConnectionUtils;
//
//
//public class EnableTransactionIntegerRedisTemplate extends IntegerRedisTemplate{
//
//	static Logger Logger = LoggerFactory.getLogger(EnableTransactionIntegerRedisTemplate.class);
//
//	public EnableTransactionIntegerRedisTemplate() {
//		super();
//		setEnableTransactionSupport(true);
//	}
//
//	public EnableTransactionIntegerRedisTemplate(RedisConnectionFactory connectionFactory) {
//		this();
//		setConnectionFactory(connectionFactory);
//		afterPropertiesSet();
//	}
//
//	public void unbind() {
//		RedisConnectionUtils.unbindConnection(getConnectionFactory());
//	}
//}
