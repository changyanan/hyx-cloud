package com.hyx.core.utils.serialize;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

public abstract class ProtostuffUtil {

	private final static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<Class<?>, Schema<?>>();

	@SuppressWarnings("unchecked")
	public static <T> Schema<T> registerClass(Class<T> cls) {
		if (!cachedSchema.containsKey(cls))
			cachedSchema.put(cls, RuntimeSchema.createFrom(cls));
		return (Schema<T>) cachedSchema.get(cls);
	}

	@SuppressWarnings("unchecked")
	public static <T> Schema<T> register(T obj) {
		return registerClass((Class<T>) obj.getClass());
	}

	/**
	 * 序列化（对象 -> 字节数组）
	 */
	public static <T> byte[] serialize(T obj) {
		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		try {
			Schema<T> schema= register(obj);
			return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		} finally {
			buffer.clear();
		}
	}

	/**
	 * 反序列化（字节数组 -> 对象）
	 */
	public static <T> T deserialize(byte[] data, Class<T> cls) {
		try {
			Schema<T> schema = registerClass(cls);
			T message =schema.newMessage();
			ProtostuffIOUtil.mergeFrom(data, message, schema);
			return message;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}
}