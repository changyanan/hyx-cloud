package com.hyx.core.utils.serialize;

import java.util.LinkedList;
import java.util.List;

import org.nustaq.serialization.FSTConfiguration;

public class FstUtils {

	private final static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();

	private final static List<Class<?>> clazs = new LinkedList<Class<?>>();

	private final static void registerClass(Class<?> claz) {
		if (!clazs.contains(claz)) {
			clazs.add(claz);
			conf.registerClass(claz);
		}
	}

	public final static void registerClass(Class<?>... clazs) {
		for (Class<?> claz : clazs) {
			registerClass(claz);
		}
	}

	public final static void register(Object... obj) {
		for (Object object : obj) {
			registerClass(object.getClass());
		}
	}

	/**
	 * 序列化（对象 -> 字节数组）
	 */
	public final static <T> byte[] serialize(T obj) {
		register(obj);
		return conf.asByteArray(obj);
	}

	/**
	 * 反序列化（字节数组 -> 对象）
	 */
	@SuppressWarnings("unchecked")
	public final static <T> T deserialize(byte[] data) {
		return (T) conf.asObject(data);
	}
}
