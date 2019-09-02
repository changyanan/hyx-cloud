package com.hyx.core.monitor;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;

@Configuration
public class ShutdownMonitoringImpl implements ShutdownMonitoring, ApplicationListener<ContextClosedEvent>{
	
	private static final Logger log = LoggerFactory.getLogger(ShutdownMonitoringImpl.class);

	private static final Set<String> TASK_IDS = new ConcurrentSkipListSet<>();
	private static final AtomicBoolean STOP_TASK = new AtomicBoolean(false);
	private static final AtomicBoolean STOP_OVER = new AtomicBoolean(false);
	
	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		STOP_TASK.set(true);
		try {
			int num=0;
			while (!TASK_IDS.isEmpty()) {
				if(num>3000) {
					log.error("超过{}秒任务依然未执行完毕 ,程序强制关机，任务ids{} ",num/10.0,TASK_IDS);
					break;
				}
				if(num>600) {
					log.warn("超过一分钟，仍然有{}任务未执行完毕",TASK_IDS);
				}
				num++;
				Thread.sleep(100L);
			}
		} catch (InterruptedException e) {
			log.warn("关机异常", e);
		}finally {
			STOP_OVER.set(true);
		}
	}
	
	@Override
	public boolean isShutdown() {
		return STOP_TASK.get();
	}

	@Override
	public void addTask(String taskId) {
		TASK_IDS.add(taskId);
	}

	@Override
	public void delTask(String taskId) {
		TASK_IDS.remove(taskId);
	}

}
