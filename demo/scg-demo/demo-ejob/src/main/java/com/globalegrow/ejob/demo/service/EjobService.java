package com.globalegrow.ejob.demo.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

@Service
public class EjobService {

	private AtomicInteger count = new AtomicInteger(); 
	public EjobService() {
		System.out.println("spring init .....");
	}
	
	public String sayEjob(){
		
		return " count :"+count.getAndIncrement();
	}
}