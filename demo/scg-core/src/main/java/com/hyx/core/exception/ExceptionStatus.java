package com.hyx.core.exception;

public interface ExceptionStatus {
	
	int digit =100000000;
	
	int getCode();

	ExceptionLevel getLevel();

	String getMsg();
}
