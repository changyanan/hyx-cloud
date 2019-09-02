package com.laolei.dubbo;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.dubbo.config.annotation.Reference;
import com.laolei.dubbo.service.MathService;

@SpringBootApplication 
public class SpringDubboxConfigApplication implements CommandLineRunner {

	@Reference
	MathService bidService;
	public static void main(String[] args) {
		SpringApplication.run(SpringDubboxConfigApplication.class, args);
		System.err.println("------>>启动完毕");
	}
	public void run(String... args) throws Exception {
//		System.err.println("---------------->> "+bidService.add(3, 2));
	}
	
//	@Autowired
//	CuratorFramework curatorFramework;
}
