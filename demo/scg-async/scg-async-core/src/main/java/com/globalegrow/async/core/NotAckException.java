package com.globalegrow.async.core;

/**
 * 协议收到这个异常表示需要将消息重新放回队列
 * @author hulei
 *
 */
public class NotAckException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
}
