package com.hyx.core.messaging;

import java.util.concurrent.Executors;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

public class Producer {
	private static final EventBus eventBus = new EventBus();
	private static final AsyncEventBus asyncEventBus = new AsyncEventBus(Executors.newFixedThreadPool(16)); 

	/**
	 * 发布异步消息
	 * @param event
	 */
	public static final void asyncPublish(Message event){
		asyncEventBus.post(event);
	}
	
	/**
	 * 发布同步消息
	 * @param event
	 */
	public static final void publish(Message event){
		eventBus.post(event);
	}
	
	protected static final <Observer> void register(Observer observer) {
		asyncEventBus.register(observer);
		eventBus.register(observer);
	}
}
