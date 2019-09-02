package com.hyx.core.limiter;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Inherited
@Target(METHOD)
@Retention(RUNTIME)
public @interface RateLimiter {
	
	/**
	 * 每秒允许该接口被调用次数
	 * @return
	 */
	double value() default 1000;
}
