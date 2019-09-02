package com.laolei;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication 
public class SpringDubboxConfigApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringDubboxConfigApplication.class, args);
		System.err.println("------>>启动完毕");
	}
	@Override
	public void run(String... args) throws Exception {
//		System.err.println("---------------->> "+mathService.add(3, 2));
	}
	
}