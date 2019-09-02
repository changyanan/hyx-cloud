package com.sockjs.test.issapi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.StreamUtils;
import com.sockjs.test.issapi.api.QueryApi;
import com.sockjs.test.issapi.model.PageInfo;
import com.sockjs.test.issapi.model.ResponseEntity;
import com.sockjs.test.utils.ListUtils;
import com.sockjs.test.utils.Utils;

import retrofit2.Retrofit;

public class Main2 {
	private static final Retrofit retrofit = Utils.create(Const.API_HOST);

	private static final QueryApi queryApi = retrofit.create(QueryApi.class);
//	private static final CheckAccountsApi checkAccountsApi = retrofit.create(CheckAccountsApi.class);
//	private static final PmsApi pmsApi = retrofit.create(PmsApi.class);
//	private static final OmsApi omsApi = retrofit.create(OmsApi.class);

	
//	static {
//		Utils.appKey = "pms-test-app-key";
//		Utils.appSecret = "pms-test-app-secret";
//	}
	static String skus="C:\\Users\\Administrator\\Desktop\\sku.csv";

	public static void main(String[] args) throws InterruptedException, FileNotFoundException, IOException { 
		String ss = StreamUtils.copyToString(new FileInputStream(skus), Charset.defaultCharset());
		ListUtils.n(ss.split(",")).each(sku->{
			System.err.println(sku);
			ResponseEntity<Integer> res = queryApi.syncStockInfoBYRoutingKeyAndSkus(Arrays.asList(sku),"iss_stock_info_sync_to_WEB_SD","scg.iss" );
			System.err.println(res);
		
		});
		
	}

}
