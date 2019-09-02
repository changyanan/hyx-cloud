package com.globalegrow.async.core;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.globalegrow.async.protocol.Protocol;
import com.globalegrow.async.serialize.Serialization;

abstract class AsyncCore {
	
	private static final ThreadLocal<Boolean> asyncCall=new ThreadLocal<Boolean>() {@Override protected Boolean initialValue() { return false; }};
	private static final Map<String , Pack> packs=new ConcurrentHashMap<>();
	private static final Map<String , Protocol> protocols=new ConcurrentHashMap<>();
	private static final Map<String , Serialization> serializes=new ConcurrentHashMap<>();
	
	private static final Logger log = LoggerFactory.getLogger(AsyncCore.class);

	/**
	 * 判断当前调用是否是需要异步调用
	 * @return
	 */
	public static boolean isAsyncCall() {
		if(asyncCall.get()) {
			asyncCall.set(false);
			return true;
		}
		return false;
	}
	
	/**
	 * 设置接下来遇到的异步aop执行不通过外部执行
	 */
	public static void setAsyncCall() {
		asyncCall.set(true);
	}
	
	private static String getName(Class<?> targetClass, Method method, Async async) {
		if(StringUtils.hasText(async.value())) {
			return async.value();
		}
		return String.format("%s.%s", targetClass.getName(),method.getName());
	}
	public static void register(Class<?> targetClass, Object bean, String beanName, Method method, Async async) {
		String name=getName(targetClass, method, async);
		Assert.isTrue(!packs.containsKey(name), name+"是已经存在的，请修改名字");
		packs.putIfAbsent(name, new Pack(targetClass, bean, beanName, method, async));
	}
	
	public static void putProtocol(Protocol protocol) {
		protocols.put(protocol.getName(), protocol);
	}
	
	public static void putSerialize(Serialization serialization) {
		serializes.put(serialization.getContentType(), serialization);
	}
	
	public final static void sead(Class<?> targetClass, Method method,Object[] args) {
		Async async=AnnotationUtils.findAnnotation(method, Async.class);
		String name=getName(targetClass, method, async);
		Protocol protocol = protocols.get(async.protocol());
		Assert.notNull(protocol,"不存在的协议"+async.protocol());
		Serialization serialize = serializes.get(async.serialize());
		Assert.notNull(serialize,"不存在的序列化方式"+async.serialize());
		byte[][] byteArgs=new byte[args.length+2][];
		byteArgs[0]=name.getBytes();
		byteArgs[1]=async.serialize().getBytes();
		for (int i = 0; i < args.length; i++) {
			Object arg = args[i];
			byteArgs[i+2]=serialize.serialize(arg);
		}
		byte[] bytes=Codes.encode(byteArgs);
		int i = 0;
		for (; i < async.sendRetry(); i++) {
			try {
				protocol.send(bytes);
				break;
			} catch (Exception e) {
				log.info("协议{}出现异常,将使用同步调用",async.protocol(),e);
			}
		}
		if(i==async.sendRetry()) {
			log.info("发送数据重试超过，将走同步调用" );
			Pack pack = packs.get(name);
			ReflectionUtils.invokeMethod(method, pack.getBean(), args);
		}
	}
	
	public final static void receive() {
		protocols.values().forEach(protocol -> protocol.receive(AsyncCore::handle));
	}
	
	private final static void handle(byte[] byteArg) {

		byte[][] byteArgs = Codes.decode(byteArg);
		String name = new String(byteArgs[0]);
		if(!packs.containsKey(name)) {
			log.error("发现了不支持的调用请求 {}", name);
			System.err.println(packs.keySet());
			throw new NotAckException();
		}
		Pack pack = packs.get(name);
		String serializeName = new String(byteArgs[1]);
		Serialization serialize = serializes.get(serializeName);
		Assert.notNull(serialize, "不支持这种序列化方式" + serializeName);
		Object[] args = new Object[byteArgs.length - 2];
		Class<?>[] argTypes = pack.getMethod().getParameterTypes();
		for (int i = 0; i < args.length; i++) {
			byte[] bs = byteArgs[i + 2];
			args[i] = serialize.deserialize(bs, argTypes[i]);
		}
		Async async = pack.getAsync();
		for (int i = 0; i < async.retry() + 1; i++) {
			try {
				ReflectionUtils.invokeMethod(pack.getMethod(), pack.getBean(), args);
				break;
			} catch (Exception e) {
				if (i == async.retry()) {
					if(async.failedAck()) {
						log.warn("调用方法{}出现异常", name,e);
						break;
					}else {
						log.error("出现异常，将进行Nack操作 {}", name);
						throw new NotAckException();
					}
				}
			}
		}
	}
}
