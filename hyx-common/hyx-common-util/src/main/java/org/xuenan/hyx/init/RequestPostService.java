package org.xuenan.hyx.init;

import org.springframework.web.servlet.ModelAndView;

/**
 * 控制器执行完毕，响应开始前执行
 * @author leige
 *
 */
@FunctionalInterface
public interface RequestPostService {

	/**
	 * 控制器执行完毕，响应开始前执行
	 * @param handler
	 * @param modelAndView
	 */
	void handle(Object handler, ModelAndView modelAndView);
	
}
