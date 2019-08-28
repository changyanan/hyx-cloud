package org.xuenan.hyxconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class HyxConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(HyxConfigApplication.class, args);
	}

}
