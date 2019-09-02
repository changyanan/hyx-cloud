package com.hyx.core.model;

import com.hyx.core.exception.ExceptionLevel;
import com.hyx.core.exception.ExceptionStatus;
import com.hyx.core.exception.GlobalException;
import com.hyx.core.exception.GlobalExceptionStatus;

public class ResponseEntity<T> extends SysModel implements Entity<T> {

	private static final long serialVersionUID = 1L;

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

	public static final <T> ResponseEntity<T> success() {
		return ResponseEntity.success(null);
	}

	public static final <T> ResponseEntity<T> success(T data) {
		ResponseEntity<T> entity = new ResponseEntity<T>();
		entity.exceptionStatus(GlobalExceptionStatus.OK);
		entity.data = data;
		entity.success = true;
		return entity;
	}

	public static final <T> ResponseEntity<T> fail(ExceptionLevel level, int code, String msg) {
		ResponseEntity<T> entity = new ResponseEntity<T>();
		entity.exceptionStatus(level, code, msg);
		entity.success = false;
		return entity;
	}

	public static final <T> ResponseEntity<T> fail(int code, String msg) {
		return fail(GlobalExceptionStatus.FAIL.getLevel(), code, msg);
	}

	public static final <T> ResponseEntity<T> fail(String msg) {
		return fail(GlobalExceptionStatus.FAIL.getCode(), msg);
	}

	public static final <T> ResponseEntity<T> fail(GlobalException exception) {
		ResponseEntity<T> entity = new ResponseEntity<T>();
		entity.exceptionStatus(exception);
		entity.success = false;
		return entity;
	}

	public static final <T> ResponseEntity<T> fail(ExceptionStatus exceptionStatus) {
		return fail(exceptionStatus.getLevel(), exceptionStatus.getCode(), exceptionStatus.getMsg());
	}
	
	public static final <T> ResponseEntity<T> fail(Throwable throwable) {
		return fail(new GlobalException(GlobalExceptionStatus.FAIL).exception(throwable));
	}

	public ResponseEntity<T> exceptionStatus(ExceptionLevel level, int code, String message) {
		this.level = level;
		this.code = code;
		this.message = message;
		return this;
	}

	public ResponseEntity<T> exceptionStatus(GlobalException exception) {
		this.level = exception.getLevel();
		this.code = exception.getCode();
		this.message = exception.getMsg();
		this.details = exception.getDetails();
		return this;
	}

	public ResponseEntity<T> exceptionStatus(ExceptionStatus status) {
		this.level = status.getLevel();
		this.code = status.getCode();
		this.message = status.getMsg();
		return this;
	}
	
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
}
