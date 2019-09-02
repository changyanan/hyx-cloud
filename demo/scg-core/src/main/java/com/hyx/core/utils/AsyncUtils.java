package com.hyx.core.utils;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.hyx.core.task.TaskProperties;

@Component
@ComponentScan
@EnableConfigurationProperties(TaskProperties.class)
public class AsyncUtils {
	static final AtomicInteger id=new AtomicInteger();
	static TaskExecutor executor;

	
	public AsyncUtils(TaskProperties taskProperties) {
		executor=taskProperties.getTask();
	}

	public static <T> void execute(AsyncEvent<T> event, T t) {
		executor.execute(()->{
			event.apply(t);
		});
	}
	
	public static <T> void execute(AsyncEventPart<T> event) {
		executor.execute(()->{
			event.apply();
		});
	}

	public static interface AsyncEvent<T> {
		void apply(T t);
	}

	public static interface AsyncEventPart<T> {
		void apply();
	}
}
