package com.globalegrow.redis.code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.hyx.core.code.IdWorker;
import com.hyx.core.code.Sequence;
import com.hyx.core.code.Sequence36;
import com.hyx.core.code.TwitterIdWorker;
import com.hyx.core.utils.Assert;
import com.reger.l2cache.serverid.config.ServerIdAutoConfiguration;
import com.reger.l2cache.serverid.event.ServerIdRefreshApplicationEvent;

@Configuration
@AutoConfigureBefore(ServerIdAutoConfiguration.class)
@ConditionalOnMissingBean(IdWorkerServerIdAutoConfiguration.class)
public class IdWorkerServerIdAutoConfiguration implements ApplicationListener<ServerIdRefreshApplicationEvent>  {
	private static final Logger LOGGER = LoggerFactory.getLogger(IdWorkerServerIdAutoConfiguration.class);
	private Integer serverId=-1;
	TwitterIdWorker idWorker=new TwitterIdWorker();
	
	@Override
	public void onApplicationEvent(ServerIdRefreshApplicationEvent event) {
		Assert.notEquals(event.getServerId(), -1, "serverId异常");
		serverId = event.getServerId();
		Sequence.setServerid( serverId);
		Sequence36.setServerid(serverId);
		idWorker.setWorkerId(serverId);
		LOGGER.info("设置编码生成器的工作组编号 {}",serverId);
	}
	
	@Bean@Lazy
	public IdWorker idWorker() {
		return idWorker;
	}
}
