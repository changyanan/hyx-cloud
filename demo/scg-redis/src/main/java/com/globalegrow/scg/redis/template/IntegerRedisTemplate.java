//package com.globalegrow.scg.redis.template;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.data.redis.connection.DefaultStringRedisConnection;
//import org.springframework.data.redis.connection.RedisConnection;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//public class IntegerRedisTemplate extends RedisTemplate<String, Integer> {
//	Logger Logger = LoggerFactory.getLogger(IntegerRedisTemplate.class);
//
//	public IntegerRedisTemplate() {
//		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
//		RedisSerializer<Integer> integerSerializer = new RedisSerializer<Integer>() {
//			public Integer deserialize(byte[] bytes) {
//				return (bytes == null ? null : Integer.parseInt(new String(bytes)));
//			}
//
//			public byte[] serialize(Integer integer) {
//				return (integer == null ? null : String.valueOf(integer).getBytes());
//			}
//		};
//		setKeySerializer(stringSerializer);
//		setValueSerializer(integerSerializer);
//		setHashKeySerializer(stringSerializer);
//		setHashValueSerializer(integerSerializer);
//	}
//
//	public IntegerRedisTemplate(RedisConnectionFactory connectionFactory) {
//		this();
//		setConnectionFactory(connectionFactory);
//		afterPropertiesSet();
//	}
//
//	protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
//		return new DefaultStringRedisConnection(connection);
//	}
//}
