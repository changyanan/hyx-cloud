package com.hyx.core.init;

/**
 * 控制器开始执行前执行
 * @author leige
 *
 */
@FunctionalInterface
public interface RequestPreService {

	/**
	 * 控制器开始执行前执行,如果返回false，请求将不会进入控制执行
	 * @param handler
	 * @return
	 */
	boolean handle(Object handler);

}
