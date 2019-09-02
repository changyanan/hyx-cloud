package com.sockjs.test.issapi.api;

import java.util.Map;

import com.sockjs.test.issapi.Const;
import com.sockjs.test.issapi.model.ResponseEntity;

import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OmsApi {

	@POST(Const.API_PREFIX+"/oms/order/receive")
	ResponseEntity<Object> receive(@Body Map<String,Object> order);
}
