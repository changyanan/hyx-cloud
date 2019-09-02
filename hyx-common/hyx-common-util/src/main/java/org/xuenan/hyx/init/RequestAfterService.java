package org.xuenan.hyx.init;

/**
 * 响应完毕后执行
 * @author leige
 *
 */
@FunctionalInterface
public interface RequestAfterService {

	/**
	 * 响应完毕后执行的方法
	 * @param handler
	 * @param ex
	 */
	void completion(Object handler, Exception ex);

}
