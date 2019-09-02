package com.globalegrow.async.serialize;

/**
 * 序列化接口，用于拓展序列化方式
 * 
 * @author hulei
 *
 */
public interface Serialization {
	byte[] empty=null;

	String getContentType();

	byte[] serialize(Object obj);

	<T> T deserialize(byte[] data, Class<T> claz);
}
