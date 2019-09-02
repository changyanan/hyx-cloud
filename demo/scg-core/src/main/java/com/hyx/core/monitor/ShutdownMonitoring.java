package com.hyx.core.monitor;

public interface ShutdownMonitoring {
	/**
	 * 添加关机监控任务
	 * @param taskId
	 */
	void addTask(String taskId);
	
	/**
	 * 移除关机监控任务
	 * @param taskId
	 */
	void delTask(String taskId);
	
	/**
	 * 判断应用是否已进入关机状态
	 * @return
	 */
	boolean isShutdown();
}
