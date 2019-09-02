package com.globalegrow.async.protocol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(RabbitMqProperties.prefix)
public class RabbitMqProperties implements Serializable {

	private static final long serialVersionUID = 1L;
	static final String prefix = "globalegrow.rabbit";
	
	/**
	 * 使用的rabbitTemplate的beanName
	 */
	private String rabbitTemplateBeanName = "rabbitTemplate";
	/**
	 * 传达数据使用的队列名列表
	 */
	private List<String> queueNames = new ArrayList<>();
	/**
	 * 队列所属的交换机
	 */
	private String exchangeName;
	/**
	 * 每个队列开多少个线程进行监听
	 */
	private int concurrency=1;
	
	/**
	 * 单服务启动消费者客户端数量
	 */
	private int clientCount=1;

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public List<String> getQueueNames() {
		return queueNames;
	}

	public void setQueueNames(List<String> queueNames) {
		this.queueNames = queueNames;
	}

	public String getRabbitTemplateBeanName() {
		return rabbitTemplateBeanName;
	}

	public void setRabbitTemplateBeanName(String rabbitTemplateBeanName) {
		this.rabbitTemplateBeanName = rabbitTemplateBeanName;
	}

	public int getConcurrency() {
		return concurrency;
	}

	public void setConcurrency(int concurrency) {
		this.concurrency = concurrency;
	}

	public int getClientCount() {
		return clientCount;
	}

	public void setClientCount(int clientCount) {
		this.clientCount = clientCount;
	}
	
}
