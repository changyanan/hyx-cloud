package com.hyx.core.utils.serialize;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.objenesis.strategy.StdInstantiatorStrategy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Kryo.DefaultInstantiatorStrategy;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoUtil {
	private final static Kryo kryo = new Kryo();
	private final static Map<Class<?>, Registration> cachedSchema = new ConcurrentHashMap<>();

	static {
		kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
	}

	public static Registration registerClass(Class<?> cls) {
		if (!cachedSchema.containsKey(cls))
			cachedSchema.put(cls, kryo.register(cls));
		return cachedSchema.get(cls);
	}

	public static Registration register(Object obj) {
		return registerClass(obj.getClass());
	}

	/**
	 * 序列化（对象 -> 字节数组）
	 */
	public static <T> byte[] serialize(T obj) {
		register(obj);
		Output output = new Output(1, 4096);
		try {
			kryo.writeObject(output, obj);
			output.flush();
			return output.toBytes();
		} finally {
			output.close();
		}
	}

	/**
	 * 反序列化（字节数组 -> 对象）
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(byte[] data, Class<T> cls) {
		Input input = new Input(data);
		try {
			return (T) kryo.readObject(input, registerClass(cls).getType());
		} finally {
			input.close();
		}
	}

	/**
	 * 序列化（对象 -> 字节数组）
	 */
	public static <T> byte[] serializeAndType(T obj) {
		register(obj);
		Output output = new Output(1, 4096);
		try {
			kryo.writeClassAndObject(output, obj);
			output.flush();
			return output.toBytes();
		} finally {
			output.close();
		}
	}

	/**
	 * 反序列化（字节数组 -> 对象）
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserializeAndType(byte[] data) {
		Input input = new Input(data);
		try {
			return (T) kryo.readClassAndObject(input);
		} finally {
			input.close();
		}
	}
}
