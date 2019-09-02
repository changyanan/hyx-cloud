package com.hyx.core.utils;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

public abstract class CglibProxy {

	@SuppressWarnings("unchecked")
	public final static <T> T create(Class<T> clazz, MethodInterceptor callback) {
		Enhancer enhancer = new Enhancer();
		// 设置需要创建子类的类
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(callback);
		enhancer.setUseCache(false);
		enhancer.setUseFactory(false);
		enhancer.setClassLoader(clazz.getClassLoader());
		// 通过字节码技术动态创建子类实例
		return (T) enhancer.create();
	}

}