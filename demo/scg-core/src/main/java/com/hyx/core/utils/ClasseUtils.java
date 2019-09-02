package com.hyx.core.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;

public abstract class ClasseUtils {

	final static LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
	private static final Map<Method, String> cacheName = new HashMap<>();
	private static final Map<Method, Boolean> cacheState = new HashMap<>();

	/**
	 * 
	 * 获取方法的参数名
	 * 
	 * @param m
	 * @return
	 */
	public final static String[] getMethodParamNames(final Method method) {
		return discoverer.getParameterNames(method);
	}

	/**
	 * 获取实例化方法参数名
	 * 
	 * @param method
	 * @return
	 */
	public final static String[] getConstructorParamNames(final Constructor<?> ctor) {
		return discoverer.getParameterNames(ctor);
	}

	/**
	 * 获取方法完整描述的名字
	 * 
	 * @param method
	 * @return
	 */
	public static String getMethodName(Method method) {
		if (method == null)
			return null;
		if (cacheName.containsKey(method))
			return cacheName.get(method);
		Class<?> clazz = method.getDeclaringClass();
		String methodName = method.getName();
		Class<?>[] types = method.getParameterTypes();
		String pname = ListUtils.<Class<?>>n().a(types).list(t -> t.getSimpleName()).join(",");
		String name = clazz.getName() + "." + methodName + "(" + pname + ")";
		cacheName.put(method, name);
		return name;
	}

	/**
	 * 判断类上或者方法上是否加油某个注解
	 * 
	 * @param method
	 * @param annotation
	 * @return
	 */
	public static boolean hasAnnotation(Method method, Class<? extends Annotation> annotation) {
		if (method == null)
			return false;
		if (cacheState.containsKey(method))
			return cacheState.get(method);
		Class<?> targetClass = method.getDeclaringClass();
		Annotation classannotation = AnnotationUtils.findAnnotation(targetClass, annotation);
		Annotation methodannotation = AnnotationUtils.findAnnotation(method, annotation);
		boolean state = methodannotation != null || classannotation != null;
		cacheState.put(method, state);
		return state;
	}
}