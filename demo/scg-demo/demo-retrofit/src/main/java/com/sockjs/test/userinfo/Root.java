package com.sockjs.test.userinfo;

public class Root<T> {
	private long curtime;
	private T data;
	private String details;
	private String errorCode;
	private String goUrl;
	private String level;
	private String message;
	private boolean succeed;
	private String token;

	public void setCurtime(long curtime) {
		this.curtime = curtime;
	}

	public long getCurtime() {
		return this.curtime;
	}

	public void setData(T data) {
		this.data = data;
	}

	public T getData() {
		return this.data;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getDetails() {
		return this.details;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public void setGoUrl(String goUrl) {
		this.goUrl = goUrl;
	}

	public String getGoUrl() {
		return this.goUrl;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getLevel() {
		return this.level;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

	public void setSucceed(boolean succeed) {
		this.succeed = succeed;
	}

	public boolean getSucceed() {
		return this.succeed;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return this.token;
	}

}