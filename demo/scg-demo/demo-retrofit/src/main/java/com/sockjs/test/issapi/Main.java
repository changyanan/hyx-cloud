package com.sockjs.test.issapi;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import com.google.gson.Gson;
import com.sockjs.test.issapi.api.CheckAccountsApi;
import com.sockjs.test.issapi.api.OmsApi;
import com.sockjs.test.issapi.api.PmsApi;
import com.sockjs.test.issapi.api.QueryApi;
import com.sockjs.test.issapi.model.ResponseEntity;
import com.sockjs.test.utils.MapUtils;
import com.sockjs.test.utils.Utils;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Part;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class Main {
	private static final Retrofit retrofit = Utils.create(Const.API_HOST);

//	private static final QueryApi queryApi = retrofit.create(QueryApi.class);
//	private static final CheckAccountsApi checkAccountsApi = retrofit.create(CheckAccountsApi.class);
//	private static final PmsApi pmsApi = retrofit.create(PmsApi.class);
	private static final OmsApi omsApi = retrofit.create(OmsApi.class);

	
//	static {
//		Utils.appKey = "pms-test-app-key";
//		Utils.appSecret = "pms-test-app-secret";
//	}

	public static void main(String[] args) throws InterruptedException {
		AtomicLong atomicLong=new AtomicLong(0);
		Long ss=System.currentTimeMillis();
		CountDownLatch latch=new CountDownLatch(20);
		ExecutorService executors = Executors.newCachedThreadPool();
		for (int i = 0; i < latch.getCount(); i++)  
		executors.execute(()->{
			while (atomicLong.get()<Long.MAX_VALUE) {
//				queryApi.test();
//				queryApi.bySkus(Arrays.asList("sku1"));
//				queryApi.bySkusAndStockAndOwner(Arrays.asList("sku1"), "NONE", "sc1");
				
//				System.err.println(checkAccountsApi.submitFileName("test.txt", "2018-06-19 19"));
				
				String json="{" + 
						"  \"goodList\": [" + 
						"    {" + 
						"      \"ownerCode\": \"ow\"," + 
						"      \"qty\": "+Thread.currentThread().getId() +"," + 
						"      \"sku\": \"sku-"+(atomicLong.getAndIncrement()+"-"+Thread.currentThread().getId())+"\"," + 
						"      \"stockCode\": \"22\"" + 
						"    }" + 
						"  ]," + 
						"  \"omsUniqueId\": \""+(UUID.randomUUID())+"\"," + 
						"  \"operationTime\": \"2018-08-16T08:58:03.318Z\"," + 
						"  \"orderSn\": \""+(UUID.randomUUID())+"\"," + 
						"  \"platform\": \"string\"," + 
						"  \"siteCode\": \"string\"" + 
						"}";
				ResponseEntity s = omsApi.receive(new Gson().fromJson(json, Map.class));
				if(!s.getSuccess()&&s.getCode()!=900000006&&s.getCode()!=900000001&&s.getCode()!=900000012) {
					System.err.println(s);
				}
//				System.err.println(pmsApi.recieveProvider(MapUtils.<String,Object>n()
//						.a("providerChName",  UUID.randomUUID().toString())
//						.a("providerSn",  UUID.randomUUID().toString())
//						.a("providerStatus", 0)
//						.a("providerEnName", UUID.randomUUID().toString())
//						.to()));
			}
			latch.countDown();
		});
//		File file = new File("C:\\Users\\Administrator\\Downloads\\kibana-6.3.2-x86_64.rpm");
//		Part part = MultipartBody.Part.createFormData("checkFile", file.getName(), RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), file));
//		System.err.println(checkAccountsApi.submit(part, "2018-06-19 19"));
		latch.await();
		System.err.println(System.currentTimeMillis()-ss);
	}

}
