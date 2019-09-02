package demo.cos;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;

@SpringBootApplication
public class CosApplication implements ApplicationRunner {
	
	public static void main(String[] args) throws Exception {
		  ConfigurableApplicationContext ctx = SpringApplication.run(CosApplication.class, args);
	}

	private String bucketName = "myoss-1256188644";
	
	@Bean
	public COSCredentials cosCredentials() {
		return new BasicCOSCredentials("AKID95lASU7xLbhZDcCJZ2NfkaCj8PepaSJu", "TirlPn5C3dx9C0ZszqWw97pIWbR2NTN1");
	}
	
	@Bean
	public Region region() {
		return new Region("ap-guangzhou");
	}
	
	@Bean(destroyMethod="shutdown")
	public COSClient cosClient() {
		ClientConfig clientConfig = new ClientConfig(region());
		return new COSClient(cosCredentials(), clientConfig);// 3 生成cos客户端
	}

	@Autowired COSClient cosClient;
	@Autowired ConfigurableApplicationContext applicationContext;
	@Override
	public void run(ApplicationArguments arguments) throws Exception {
		// 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
		// 大文件上传请参照 API 文档高级 API 上传
		Resource res = applicationContext.getResource("file:C:/Users/leige/Downloads/test.docx");
		File localFile =res.getFile();
		System.err.println(localFile);
		
		// 指定要上传到 COS 上的路径
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, res.getFilename(), localFile);
		PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
		System.err.println(putObjectResult);
		
		applicationContext.close();
		
	}
}
