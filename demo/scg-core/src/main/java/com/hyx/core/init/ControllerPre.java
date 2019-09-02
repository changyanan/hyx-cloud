package com.hyx.core.init;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;

@FunctionalInterface
public interface ControllerPre {
	void apply(ProceedingJoinPoint point, Method method);
}
