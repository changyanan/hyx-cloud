package com.sockjs.test.issapi.model;

import com.google.gson.Gson;

public class ResponseEntity<T>   {
	/**
	 * 返回 错误代码
	 */
	private int code;

	/**
	 * 返回是否操作成功
	 */
	private Boolean success;

	/**
	 * 返回的异常消息
	 */
	private String message;

	/**
	 * 返回的异常级别
	 */
	private ExceptionLevel level;

	/**
	 * 出现异常时返回异常详细信息
	 */
	private String details;

	/**
	 * 返回服务器时间戳
	 */
	private long curtime = System.currentTimeMillis();

	/**
	 * 返回token
	 */
	private String token;

	/**
	 * 返回的业务数据
	 */
	private T data;

	public ResponseEntity<T> message(String message) {
		this.message = message;
		return this;
	}

	public boolean getSuccess() {
		if (success != null) {
			return success;
		}
		return this.getCode() == 0;
	}

	public T getData() {
		return data;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ExceptionLevel getLevel() {
		return level;
	}

	public long getCurtime() {
		return curtime;
	}

	public String getToken() {
		return token;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setLevel(ExceptionLevel level) {
		this.level = level;
	}

	public void setCurtime(long curtime) {
		this.curtime = curtime;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
