package com.hyx.core.task;

import java.util.concurrent.ThreadPoolExecutor;

import com.hyx.core.model.SysModel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@ConfigurationProperties(TaskProperties.prefix)
public class TaskProperties extends SysModel {

	private static final long serialVersionUID = 1L;
	static final String prefix="globalegrow";
	private ThreadPoolTaskExecutor task  ;
	
	public TaskProperties() {
		super();
		task = new ThreadPoolTaskExecutor();
		task.setThreadNamePrefix("SCG-TASK-");
		task.setMaxPoolSize(1000);  
		task.setCorePoolSize(1000);
		task.setKeepAliveSeconds(3600);
        // 使用预定义的异常处理类
		task.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
	}
	
	public ThreadPoolTaskExecutor getTask() {
		return task;
	}
	public void setTask(ThreadPoolTaskExecutor task) {
		this.task = task;
	} 
}
