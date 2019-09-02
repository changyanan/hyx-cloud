package com.globalegrow.async.core;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 异步操作
 * @author hulei
 *
 */
@Inherited
@Target(METHOD)
@Retention(RUNTIME)
public @interface Async {
	
	/**
	 * 不可以重复，用于确定被调用的方法，如果为空时，将使用完整方法名
	 * @return
	 */
	String value() default "";
	
	/**
	 * 数据传递使用的序列化方式
	 * @return
	 */
	 String serialize() default "x-application/nativejava";
	 
	 /**
	  * 数据传输协议
	  * @return
	  */
	 String protocol();
	 
	 /**
	  * 发送数据时，失败重试次数,重试超过次数时，将走同步调用
	  * @return
	  */
	 int sendRetry() default 1;
	 
	 /**
	  * 调用业务方法出现异常时重试次数
	  * @return
	  */
	 int retry() default 1;
	 
	 /**
	  * 
	  * 调用业务重试完毕后依然失败，是否需要ack
	  * @return
	  */
	 boolean failedAck() default false;
}
