package com.sockjs.test.issapi.api;

import java.util.Map;

import com.sockjs.test.issapi.model.ResponseEntity;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface WmsInStockApi {
	
	@Headers({"Content-Type: application/json","Accept: text/html"})
	@POST("/wms/instock/other")
	ResponseEntity<Object> other(@Body Map<String, Object> body);
}
