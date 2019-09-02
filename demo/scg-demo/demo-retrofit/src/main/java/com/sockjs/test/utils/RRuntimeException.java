package com.sockjs.test.utils;

import java.io.IOException;

import retrofit2.Response;

public class RRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final int code;
	private final String body;
	
	public RRuntimeException(Response<Object> re) throws IOException {
		super(re.message());
		this.code = re.code();
		this.body = re.errorBody().string();
	}
	
	public int getCode() {
		return code;
	}

	public String getBody() {
		return body;
	}

	@Override
	public String toString() {
		return "RRuntimeException [code=" + code + ", message=" + getMessage() + ", body=" + body + "]";
	}
}
