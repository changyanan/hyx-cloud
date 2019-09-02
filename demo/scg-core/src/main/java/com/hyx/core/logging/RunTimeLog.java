package com.hyx.core.logging;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.hyx.core.exception.ExceptionLevel;
import com.hyx.core.logging.RunTimeLog.TimeScale;
import com.hyx.core.utils.JSONUtils;
import com.hyx.core.utils.ListUtils;
import com.hyx.core.utils.MapUtils;

@Component
@EnableConfigurationProperties({ TimeScale.class })
public class RunTimeLog implements DisposableBean{

	private final static Logger log = LoggerFactory.getLogger("globalegrow.time.log");

	private static Map<ExceptionLevel, Integer> timeScale = MapUtils.<ExceptionLevel, Integer>n().a(ExceptionLevel.OFF, 1000).a(ExceptionLevel.TRACE, 10).a(ExceptionLevel.DEBUG, 100).a(ExceptionLevel.INFO, 200).a(ExceptionLevel.WARN, 2000).a(ExceptionLevel.ERROR, 8000).to();
	private static Entry<ExceptionLevel, Integer>[] timeScaleList;
	private static int offtime;
	private static ThreadLocal<Stack<Long>> timeStack=new ThreadLocal<Stack<Long>>() {protected java.util.Stack<Long> initialValue() {return new Stack<>();};};
	private static ScheduledExecutorService executor=  Executors.newScheduledThreadPool(4,r->{
		Thread thread=new Thread(r);
		thread.setName("run-time-log-task-"+thread.getId());
		return thread;
	});
	
	private static void init(Map<ExceptionLevel, Integer> map) {
		timeScale = MapUtils.n(map).filter((k, v) -> v != null).to();
		Integer offtime_ = timeScale.remove(ExceptionLevel.OFF);
		offtime = offtime_ == null ? -1 : offtime_;
		@SuppressWarnings("unchecked")
		Entry<ExceptionLevel, Integer> [] entrys=new Entry[timeScale.size()] ;
		timeScaleList = ListUtils.n(timeScale.entrySet()).filter(t -> t.getValue() != null).order(t -> t.getValue()).to().toArray(entrys);
	}
	
	public static final void bedin() {
		timeStack.get().push(System.currentTimeMillis());
	}

	private static final long getRuntime() {
		Long bedin = timeStack.get().pop();
		if(bedin==null) {
			return 0;
		}
		long end = System.currentTimeMillis();
		return end - bedin;
	}

	public static final void log(String f, Object... args) {
		log(getRuntime(), f, args);
	}
	
	public static final void log(long runtime, String f, Object... args) {
//		executor.execute(()->{
			logSync(runtime, f, args);
//		});
	}
	
	private static final void logSync(long runtime, String f, Object... args) {
		if (runtime == 0 || f == null || offtime > runtime || timeScaleList.length==0) {
			return;
		}
		Entry<ExceptionLevel, Integer> suitmin = null;
		for (int i = 0; i < timeScaleList.length; i++) {
			Entry<ExceptionLevel, Integer> entry = timeScaleList[i];
			if (entry.getValue() > runtime)
				break;
			suitmin = entry;
		}
		StringBuffer format = new StringBuffer("执行时间:").append(runtime).append("毫秒   ").append(f);
		output(suitmin, format.toString(), args);
	}

	private static void output(Entry<ExceptionLevel, Integer> suitmin, String f, Object... args) {
		if (suitmin == null) {
			if(log.isDebugEnabled()) {
				log.debug(f, args);
			}
		} else {
			switch (suitmin.getKey()) {
			case TRACE:
				if(log.isTraceEnabled()) {
					log.trace(f, args);
				}
				break;
			case DEBUG:
				if(log.isDebugEnabled()) {
					log.debug(f, args);
				}
				break;
			case WARN:
				if(log.isWarnEnabled()) {
					log.warn(f, args);
				}
				break;
			case ERROR:
				if(log.isWarnEnabled()) {
					log.error(f, args);
				}
				break;
			case INFO:
			default:
				if(log.isInfoEnabled()) {
					log.info(f, args);
				}
				break;
			}
		}
	}

	static {
		init(timeScale);
	}

	@ConfigurationProperties("logging")
	public class TimeScale implements InitializingBean {

		public Map<ExceptionLevel, Integer> getTimeScale() {
			return timeScale;
		}

		public void setTimeScale(Map<ExceptionLevel, Integer> timeScale) {
			RunTimeLog.timeScale = timeScale;
		}

		@Override
		public void afterPropertiesSet() throws Exception {
			init(timeScale);
			log.info("api调用日志输出时间参数配置信息{} 日志最少输出的时间 {}", JSONUtils.toJSONString(timeScaleList), offtime);
		}
	}

	@Override
	public void destroy() throws Exception {
		executor.shutdownNow();
	}
}
