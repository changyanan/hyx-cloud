package com.sockjs.test.issapi.api;

import java.util.Map;

import com.sockjs.test.issapi.Const;
import com.sockjs.test.issapi.model.ResponseEntity;

import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PmsApi {

	@POST(Const.API_PREFIX+"/provider/recieve")
	ResponseEntity<Object> recieveProvider(@Body Map<String,Object> providerBasicInVo);
}
