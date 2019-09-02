package demo.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.globalegrow.core.code.IdWorker;


@SpringBootApplication
public class DemoRedisApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(DemoRedisApplication.class, args);
	}
	
	
	@Autowired IdWorker idWorker;

	@Override
	public void run(String... args) throws Exception {
		
		System.err.println(idWorker.nextId());
		System.err.println(idWorker.nextId());
		System.err.println(idWorker.nextId());
		System.err.println(idWorker.nextId());
		
	}
	
 
}
