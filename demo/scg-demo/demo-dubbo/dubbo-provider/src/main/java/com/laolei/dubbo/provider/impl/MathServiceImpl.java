package com.laolei.dubbo.provider.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;
import com.laolei.dubbo.model.User;
import com.laolei.dubbo.service.MathService;
import com.globalegrow.core.utils.Assert;

@Service
public class MathServiceImpl implements MathService {

	public int add(  Integer a,  Integer b) {
		System.err.println("请求到达  " + a + "+" + b + "=" + (a + b));
		return a + b;
	}

	public List<Object> toList(Object... args) {
		List<Object> list = new LinkedList<Object>();
		Collections.addAll(list, args);
		return list;
	}

	public void throwThrowable()  {
		Assert.isTrue(false, "专门抛出一个异常试试异常时！");
	}

	public User getUser(User user) {
		System.err.println(user);
		return user;
	}
}
