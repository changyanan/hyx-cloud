package org.xuenan.hyx.init;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

@FunctionalInterface
public interface ControllerPost {
	void apply(ProceedingJoinPoint point, Method method, Object relust, Throwable throwable);
}
