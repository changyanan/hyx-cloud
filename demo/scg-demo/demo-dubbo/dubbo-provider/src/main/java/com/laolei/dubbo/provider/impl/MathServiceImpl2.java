//package com.laolei.dubbo.provider.impl;
//
//import java.util.Collections;
//import java.util.LinkedList;
//import java.util.List;
//
//import javax.ws.rs.Consumes;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.QueryParam;
//import javax.ws.rs.core.MediaType;
//
//import com.alibaba.dubbo.config.annotation.Service;
//import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
//import com.laolei.dubbo.model.User;
//import com.laolei.dubbo.service.MathService;
//
//@Path("/math")
//@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
//@Produces({ ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8 })
//@Service(  validation = "true", protocol="rest")
//public class MathServiceImpl2 implements MathService {
//
//	@GET
//	@Override
//	@Path("/add")
//	public int add(@QueryParam("a") Integer a, @QueryParam("b") Integer b) {
//		System.err.println("请求到达  " + a + "+" + b + "=" + (a + b));
//		return a + b;
//	}
//
//	@Override
//	public List<Object> toList(Object... args) {
//		List<Object> list = new LinkedList<>();
//		Collections.addAll(list, args);
//		return list;
//	}
//
//	@GET
//	@Override
//	@Path("err")
//	public void throwThrowable() {
//		throw new RuntimeException("专门抛出一个异常试试异常时！");
//	}
//
//	@Override
//	public User getUser(User user) {
//		System.err.println(user);
//		return user;
//	}
//}
