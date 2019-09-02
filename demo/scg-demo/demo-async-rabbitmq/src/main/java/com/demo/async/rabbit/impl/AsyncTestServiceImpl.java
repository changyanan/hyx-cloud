package com.demo.async.rabbit.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.demo.async.rabbit.AsyncTestService;
import com.globalegrow.async.core.Async;

@Service
public class AsyncTestServiceImpl implements AsyncTestService{
	
	private static final Logger log = LoggerFactory.getLogger(AsyncTestServiceImpl.class);
	AtomicLong count=new AtomicLong();
	@Override
	@Async(protocol="x-application/rabbitmq" )
	public void testAsync(int id) {
//		log.info("----------------->>> {}",id);
		testAsync1(  id);
	}

//	@Async(value="testAsync1",protocol="x-application/rabbitmq")
	public void testAsync1(int id) {
//		try {
//			TimeUnit.MILLISECONDS.sleep(500);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		log.info("----------------->>>1 {}",count.incrementAndGet());
	}
}
