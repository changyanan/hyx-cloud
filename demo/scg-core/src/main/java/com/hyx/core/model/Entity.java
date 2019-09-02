package com.hyx.core.model;

import com.hyx.core.exception.ExceptionLevel;

public interface Entity<T> {
	
	/**
	 * 请求是否响应成功
	 * @return
	 */
	boolean getSuccess();

	/**
	 * 请求响应的数据报文体
	 * @return
	 */
	public T getData();
	
	/**
	 * 请求的响应码
	 * @return
	 */
	public int getCode();
	
	/**
	 * 请求响应的消息字符串
	 * @return
	 */
	public String getMessage();
	
	/**
	 * 请求响应的消息的级别
	 * @return
	 */
	public ExceptionLevel getLevel();
	
	/**
	 * 服务器响应的时间
	 * @return
	 */
	public long getCurtime();
}
