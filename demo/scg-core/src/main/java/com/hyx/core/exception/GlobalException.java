package com.hyx.core.exception;

import com.hyx.core.utils.ThrowableUtils;

/**
 * 业务异常类.
 * 
 * @author Administrator
 *
 */
public class GlobalException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;
	private ExceptionLevel level;
	private String details;

	public GlobalException(ExceptionStatus exceptionstatus) {
		super();
		this.code = exceptionstatus.getCode();
		this.msg = exceptionstatus.getMsg();
		this.level = exceptionstatus.getLevel();
	}

	public GlobalException(int code, String msg, ExceptionLevel level) {
		super();
		this.code = code;
		this.msg = msg;
		this.level = level;
	}

	private static String message(String message,Object... args) {
		if(message==null) {
			return null;
		}
		for (Object object : args) {
			message=message.replaceFirst("\\{\\}", String.valueOf(object));
		}
		return message;
	}
 
	@Override
	public String getMessage() {
		StringBuffer message = new StringBuffer();
		message.append("  异常级别:").append(this.level);
		message.append("  异常代码:").append(this.code);
		message.append("  异常消息:").append(msg);
		return message.toString();
	}

	@Override
	public String toString() {
		return this.getMessage();
	}

	public GlobalException exception(Throwable throwable) {
		this.details = ThrowableUtils.toStringIsDebug(throwable);
		return this;
	}

	public static void exception(ExceptionStatus exceptionstatus) {
		throw new GlobalException(exceptionstatus);
	}

	public static void error(int code, String msg,Object... args) {
		throw new GlobalException(code, message(msg, args), ExceptionLevel.ERROR);
	}

	public static void error(String msg,Object... args) {
		throw new GlobalException(GlobalExceptionStatus.FAIL.getCode(), message(msg, args), ExceptionLevel.ERROR);
	}

	public static void warn(int code, String msg,Object... args) {
		throw new GlobalException(code, message(msg, args), ExceptionLevel.WARN);
	}

	public static void warn(String msg,Object... args) {
		warn(GlobalExceptionStatus.WARN.getCode(), msg, args);
	}

	public static void info(int code, String msg,Object... args) {
		throw new GlobalException(code, message(msg, args), ExceptionLevel.INFO);
	}

	public static void info(String msg,Object... args) {
		info(GlobalExceptionStatus.INFO.getCode(), msg, args);
	}

	public static void debug(int code, String msg,Object... args) {
		throw new GlobalException(code, message(msg, args), ExceptionLevel.DEBUG);
	}

	public static void debug(String msg,Object... args) {
		debug(GlobalExceptionStatus.DEBUG.getCode(), msg, args);
	}

	public String getDetails() {
		if (details != null) {
			return details;
		}
		return ThrowableUtils.toStringIsDebug(this);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public ExceptionLevel getLevel() {
		return level;
	}

	public void setLevel(ExceptionLevel level) {
		this.level = level;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}