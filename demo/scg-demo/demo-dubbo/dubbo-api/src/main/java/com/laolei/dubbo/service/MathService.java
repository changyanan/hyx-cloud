package com.laolei.dubbo.service;

import java.util.List;

import com.laolei.dubbo.model.User;
import com.globalegrow.core.exception.GlobalException;

public interface MathService {
	
	/**
	 * 简单示例
	 * @param a
	 * @param b
	 * @return
	 */
	int add(Integer a,Integer b);
	
	/**
	 * 复杂对象示例
	 * @param args
	 * @return
	 */
	List<Object> toList(Object ... args);
	
	/**
	 * 抛出异常测试
	 */
	void throwThrowable()throws GlobalException;
	
	User getUser(User user);
}
