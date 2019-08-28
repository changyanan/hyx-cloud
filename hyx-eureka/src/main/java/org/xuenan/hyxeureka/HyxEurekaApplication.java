package org.xuenan.hyxeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class HyxEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(HyxEurekaApplication.class, args);
	}

}
