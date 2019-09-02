package demo.validated;

import javax.validation.Valid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.globalegrow.core.model.ResponseEntity;

import io.swagger.annotations.ApiOperation;

@RestController
@SpringBootApplication
public class TestValidatedApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(TestValidatedApplication.class, args);
	}
	
	
	@ApiOperation("测试")
	@PostMapping("/test")
	public ResponseEntity<Abc> test(@Valid @RequestBody Abc abc,@RequestParam String a) {
		return ResponseEntity.success(abc);
	}
}
