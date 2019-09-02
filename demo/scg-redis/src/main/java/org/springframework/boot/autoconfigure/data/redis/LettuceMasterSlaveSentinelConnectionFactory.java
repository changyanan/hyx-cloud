package org.springframework.boot.autoconfigure.data.redis;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

public class LettuceMasterSlaveSentinelConnectionFactory extends LettuceConnectionFactory {
	
	private List<LettuceConnectionFactory> slave=new ArrayList<>();
	private volatile AtomicInteger index=new AtomicInteger(0);
	private final RedisSentinelConfiguration sentinelConfiguration;
	
	public LettuceMasterSlaveSentinelConnectionFactory(RedisSentinelConfiguration sentinelConfiguration,
			LettuceClientConfiguration clientConfig) {
		super(sentinelConfiguration, clientConfig);
		this.sentinelConfiguration=sentinelConfiguration;
	}

	public LettuceMasterSlaveSentinelConnectionFactory(RedisSentinelConfiguration sentinelConfiguration) {
		super(sentinelConfiguration);
		this.sentinelConfiguration=sentinelConfiguration;
	}
	
	@Override
	public RedisConnection getConnection() {
		if(MasterSlave.isMaster()) {
			return super.getConnection();
		}
		int i = index.getAndUpdate(a->(a+1)%(slave.size()+1));
		if(i==0) {
			return super.getConnection();
		}
		RedisConnection connection= slave.get(i-1).getConnection();
		return connection;
	}
	
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		RedisSentinelConnection sentinelConnection = getSentinelConnection();
		Collection<RedisServer> slaves = sentinelConnection.slaves(sentinelConfiguration.getMaster());
		slaves.forEach(slave->{
			RedisStandaloneConfiguration standaloneConfig=new RedisStandaloneConfiguration(slave.getHost(), slave.getPort());
			standaloneConfig.setDatabase(sentinelConfiguration.getDatabase());
			standaloneConfig.setPassword(sentinelConfiguration.getPassword());
			LettuceConnectionFactory connectionFactory= new LettuceConnectionFactory(standaloneConfig);
			connectionFactory.setShareNativeConnection(super.getShareNativeConnection());
			connectionFactory.afterPropertiesSet();
			this.slave.add(connectionFactory);
		});
	}
	
	@Override
	public void setShareNativeConnection(boolean shareNativeConnection) {
		super.setShareNativeConnection(shareNativeConnection);
		this.slave.forEach(slave->slave.setShareNativeConnection(shareNativeConnection));
	}
	@Override
	public void destroy() {
		super.destroy();
		this.slave.forEach(LettuceConnectionFactory::destroy);
	}
}
