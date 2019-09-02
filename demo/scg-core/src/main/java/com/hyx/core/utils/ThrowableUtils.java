package com.hyx.core.utils;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.hyx.core.web.config.WebProperties;

@Component
@EnableConfigurationProperties(WebProperties.class)
public class ThrowableUtils {
	private  static  WebProperties webProperties;
	
	public ThrowableUtils(WebProperties webProperties) {
		super();
		ThrowableUtils.webProperties = webProperties;
	}

	/**
	 * 在测试环境吧异常信息转化为字符串输出
	 * @param throwable
	 * @return
	 */
	public static final String toStringIsDebug(Throwable throwable) {
		if(!webProperties.isDebug()) {
			return  null;
		}
		return toString(throwable,0);
	}


	/**
	 * 输出异常堆栈信息
	 * @param throwable
	 * @return
	 */
	public static final String toString(Throwable throwable ) {
		return toString(throwable,0);
	}
	
	/**
	 *  输出异常堆栈信息
	 * @param throwable
	 * @param depth
	 * @return
	 */
	private static final String toString(Throwable throwable,int depth) {
		if(throwable==null) {
			return "";
		}
		String throwableMessage = throwable.getMessage();
		StackTraceElement[] traces = throwable.getStackTrace();
		StringBuffer message = new StringBuffer("异常堆栈信息:");
		message.append("[");
		message.append(throwableMessage);
		message.append("]");
		for (int i = 0; i < traces.length && i < 50; i++) {
			StackTraceElement trace = traces[i];
			message.append("\r\n,");
			message.append(trace.toString());
		}
		if(depth<10) {
			message.append(toString(throwable.getCause(),depth+1));
		}
		return message.toString();
	}
}
