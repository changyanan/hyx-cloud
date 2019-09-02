 package com.globalegrow.scg.rabbitmq.properties;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = RabbitBatchProperties.prefix)
public class RabbitBatchProperties{
	public final static String prefix = "spring.rabbitmq";
	/**
	 * 当链接多个rabbitmq时使用
	 *  生成的对象bean名
	 */
	private final Map<String, RabbitProperties> batch =new HashMap<String, RabbitProperties>();

	public Map<String, RabbitProperties> getBatch() {
		return batch;
	}
	 
}
