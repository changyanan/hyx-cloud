package com.test;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.test.model.User;
import com.test.repo.UserRepo;

@SpringBootApplication 
@EnableJpaRepositories(basePackages="com.test.repo")
public class SpringJpaConfigApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringJpaConfigApplication.class, args);
		System.err.println("------>>启动完毕");
	}
	
	@Autowired UserRepo userRepo;
	
	@Transactional
	public void run(String... args) throws Exception {
		
		User user =new User();
		for (int i = 1; i < 10; i++) {
			user.setName("Name-"+i);
			userRepo.save(user );
		}
		
		System.err.println(userRepo.findByIdOrNameOrPassword(2L, null, null));
	}
	
}
