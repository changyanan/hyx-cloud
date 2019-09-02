package com.globalegrow.async.serialize.impl;

import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import com.globalegrow.async.serialize.Serialization;


/**
 * 使用原生jdk方式序列化，反序列化
 * @author hulei
 *
 */
@Component
public class NativeJavaSerialization implements Serialization {

	@Override
	public String getContentType() {
		return "x-application/nativejava";
	}

	@Override
	public byte[] serialize(Object object) {
		if(object==null) {
			return empty;
		}
		return SerializationUtils.serialize(object);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T deserialize(byte[] bytes, Class<T> claz) {
		if(bytes==null) {
			return null;
		}
		return (T) SerializationUtils.deserialize(bytes);
	}

}
