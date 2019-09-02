package com.sockjs.test.issapi.api;

import java.util.List;

import com.sockjs.test.issapi.Const;
import com.sockjs.test.issapi.model.PageInfo;
import com.sockjs.test.issapi.model.ResponseEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QueryApi {

	@GET(Const.API_PREFIX+"/test")
	ResponseEntity<Object> test( );

	@GET(Const.API_PREFIX+"/query/bySkus")
	ResponseEntity<PageInfo<Object>> bySkus(@Query("skus") List<String> skus);
	
	@GET(Const.API_PREFIX+"/query/bySkusAndOwner")
	ResponseEntity<PageInfo<Object>> bySkusAndOwner(@Query("skus") List<String> skus,@Query("ownerCode")String ownerCode);
	
	@GET(Const.API_PREFIX+"/query/bySkusAndStock")
	ResponseEntity<PageInfo<Object>> bySkusAndStock(@Query("skus") List<String> skus,@Query("stockCode")String stockCode);
	
	@GET(Const.API_PREFIX+"/query/bySkusAndStockAndOwner")
	ResponseEntity<PageInfo<Object>> bySkusAndStockAndOwner(@Query("skus") List<String> skus,@Query("ownerCode")String ownerCode,@Query("stockCode")String stockCode);

	@GET(Const.API_PREFIX+"/tool/syncStockInfoBYRoutingKeyAndSkus")
	ResponseEntity<Integer> syncStockInfoBYRoutingKeyAndSkus(@Query("skus") List<String> skus,@Query("routingKey") String routingKey,@Query("password") String password );
}
