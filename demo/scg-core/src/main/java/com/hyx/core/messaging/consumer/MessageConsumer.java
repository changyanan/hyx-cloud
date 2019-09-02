package com.hyx.core.messaging.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.hyx.core.messaging.Message;

public class MessageConsumer {
	private static final Logger logger=LoggerFactory.getLogger(MessageConsumer.class);
	
	@Subscribe
	public void message(Message message) {
		logger.debug("收到消息 : {}", message);
	}
}
