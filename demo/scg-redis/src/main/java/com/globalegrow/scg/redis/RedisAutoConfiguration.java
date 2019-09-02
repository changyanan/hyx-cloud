//
//package com.globalegrow.scg.redis;
//
//import java.net.UnknownHostException;
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//
//import com.globalegrow.scg.redis.template.IntegerRedisTemplate;
//
//@Configuration
//@ConditionalOnClass(RedisOperations.class)
//@EnableConfigurationProperties(RedisProperties.class)
////@Import({ LettuceConnectionConfiguration.class })
//public class RedisAutoConfiguration {
//
//	@Bean
//	@ConditionalOnMissingBean(name = "redisTemplate")
//	public RedisTemplate<Object, Object> redisTemplate(
//			RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
//		RedisTemplate<Object, Object> template = new RedisTemplate<>();
//		template.setConnectionFactory(redisConnectionFactory);
//		return template;
//	}
//
//	@Bean
//	@ConditionalOnMissingBean
//	public StringRedisTemplate stringRedisTemplate(
//			RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
//		StringRedisTemplate template = new StringRedisTemplate();
//		template.setConnectionFactory(redisConnectionFactory);
//		return template;
//	}
//	
//	@Bean
//	@ConditionalOnMissingBean
//	public IntegerRedisTemplate integerRedisTemplate(
//			RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
//		IntegerRedisTemplate template = new IntegerRedisTemplate();
//		template.setConnectionFactory(redisConnectionFactory);
//		return template;
//	}
////	
////	@Bean
////	@ConditionalOnMissingBean
////	public EnableTransactionIntegerRedisTemplate enableTransactionIntegerRedisTemplate(
////			RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
////		EnableTransactionIntegerRedisTemplate template = new EnableTransactionIntegerRedisTemplate();
////		template.setConnectionFactory(redisConnectionFactory);
////		return template;
////	}
//
//}
