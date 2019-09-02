//
//package com.globalegrow.scg.redis;
//
//import java.net.UnknownHostException;
//
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisClusterConfiguration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisSentinelConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//
//import com.lambdaworks.redis.RedisClient;
//import com.lambdaworks.redis.resource.ClientResources;
//import com.lambdaworks.redis.resource.DefaultClientResources;
//
//@Configuration
//@EnableConfigurationProperties(RedisProperties.class)
//@ConditionalOnClass(RedisClient.class)
//public class LettuceConnectionConfiguration extends RedisConnectionConfiguration {
//
//	private final RedisProperties properties;
//
//	LettuceConnectionConfiguration(RedisProperties properties,
//			ObjectProvider<RedisSentinelConfiguration> sentinelConfigurationProvider,
//			ObjectProvider<RedisClusterConfiguration> clusterConfigurationProvider) {
//		super(properties, sentinelConfigurationProvider, clusterConfigurationProvider);
//		this.properties = properties;
//	}
//
//	@Bean(destroyMethod = "shutdown")
//	@ConditionalOnMissingBean(ClientResources.class)
//	public DefaultClientResources lettuceClientResources() {
//		return DefaultClientResources.create();
//	}
//
//	@Bean
//	@ConditionalOnMissingBean(RedisConnectionFactory.class)
//	public LettuceConnectionFactory redisConnectionFactory(ClientResources clientResources)
//			throws UnknownHostException {
//		LettuceConnectionFactory connectionFactory= new LettuceConnectionFactory(getClusterConfiguration());
//		connectionFactory.setClientResources(clientResources);
//		connectionFactory.setUseSsl(properties.isSsl());
////		connectionFactory.setTimeout(timeout);
//		connectionFactory.setTimeout(properties.getTimeout());
//		connectionFactory.setPassword(properties.getPassword());
//		return connectionFactory;
//	}
//
//}
