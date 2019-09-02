package com.globalegrow.async.protocol;

import java.util.function.Consumer;

/**
 * 传输数据
 * @author hulei
 *
 */
public interface Protocol {
	String getName();

	/**
	 * 发送数据
	 * @param args
	 */
	void send( byte[] args);

	/**
	 * 接受数据
	 * @param consumer
	 */
	void receive( Consumer<byte[]> consumer);
	 
}
