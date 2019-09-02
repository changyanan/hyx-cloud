package com.laolei.dubbo.consumer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.laolei.dubbo.model.User;
import com.laolei.dubbo.service.MathService;

/**
 * @author Jinkai.Ma
 */
@Component
public class DubboReferenceDemo implements CommandLineRunner {

	@Reference
	MathService bidService;

	Integer a=1;
	Integer b=2;
	Integer c=3;

	Integer d=2222;
	Integer e=2223;
	Integer f=2224;

	public void run(String... args) throws Exception {
		
		System.err.println(a+" "+b+" "+c+" "+d+" "+e+" "+f);
//
		System.err.println("---------------->> "+bidService.add(3, 2));
		System.err.println(a + "+" + b + "=" + bidService.add(a, b));
		System.err.println(a + "+" + b + "=" + bidService.add(a, b));
		System.err.println(a + "+" + b + "=" + bidService.add(a, b));
		System.err.println(a + "+" + b + "=" + bidService.add(a, b));
		System.err.println(a + "+" + b + "=" + bidService.add(a, b));
		System.err.println(a + "+" + b + "=" + bidService.add(a, b));
		System.err.println(a + "+" + b + "=" + bidService.add(a, b));
		System.err.println("list=" + bidService.toList(1, "22", true, 'b'));
		for (int i = 0; i < 10; i++) {
			User user = bidService.getUser(new User(11,"张三","张三的密码"));
			System.err.println(user);
		}
//		this.add(11, bidService.add(a, b));
		try {
			bidService.throwThrowable();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

//	private void add(Integer a, Integer b) {
//		System.err.println("求和  " + a + "+" + b);
//		String url = "http://192.168.1.2:60020/api/math/add.json";
//		Client client = ClientBuilder.newClient();
//		WebTarget target = client.target(url);
//
//		try {
//			Integer sum = target.queryParam("a", a).queryParam("b", b).request().get(Integer.class);
//			System.err.println("求和成功,求和结果: " + (int) sum);
//		} finally {
//			// client.close();
//		}
//
//		Integer sum = target.queryParam("a", a).queryParam("b", b).request().get(Integer.class);
//		System.err.println("求和成功,求和结果: " + (int) sum);
//	}

}
