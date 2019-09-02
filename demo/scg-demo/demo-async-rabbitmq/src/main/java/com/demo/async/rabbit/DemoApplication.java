package com.demo.async.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.globalegrow.core.utils.AsyncUtils;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner{
	
	private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) throws Exception {
		SpringApplication.run(DemoApplication.class, args);
	}
	@Autowired AsyncTestService asyncTestService;
	@Override
	public void run(String... arg0) throws Exception {
		asyncTestService.testAsync(1000);
		log.info("---->>1");
		AsyncUtils.execute(()->{
			for (int i = 0; i < 5000000; i++) {
				asyncTestService.testAsync(i);
			}
		});
	}
}
