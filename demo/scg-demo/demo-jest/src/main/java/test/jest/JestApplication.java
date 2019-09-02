package test.jest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.client.JestResultHandler;
import io.searchbox.indices.IndicesExists;

@SpringBootApplication
public class JestApplication implements CommandLineRunner{
	public static void main(String[] args) throws Exception {
		SpringApplication.run(JestApplication.class, args);
	}
	@Autowired JestClient jestClient;
	@Override
	public void run(String... args) throws Exception {
		jestClient.executeAsync(new IndicesExists.Builder("test").build(), new JestResultHandler<JestResult>() {

			@Override
			public void completed(JestResult result) {
				System.err.println(result);
			}

			@Override
			public void failed(Exception ex) {
				ex.printStackTrace();
			}
		});
	}
	
}
