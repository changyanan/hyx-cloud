package com.globalegrow.async.core;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 注解的方法，调用时会切换到指定的数据源
 *@author leige
 */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AsyncAspect {
	
	@Around(value = "@annotation(com.globalegrow.async.core.Async)", argNames = "point")
	public Object doAround(final ProceedingJoinPoint point) throws Throwable {
		if(AsyncCore.isAsyncCall()) {
			return point.proceed();
		}
		Method method=((MethodSignature)point.getSignature()).getMethod();
		Object[] args=point.getArgs();
		Class<?> targetClass=AopUtils.getTargetClass(point.getTarget());
		AsyncCore.sead(targetClass, method, args);
		return null;
	}

}
