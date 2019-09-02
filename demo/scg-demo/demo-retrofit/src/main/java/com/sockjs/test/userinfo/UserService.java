package com.sockjs.test.userinfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface UserService {
	
	@GET("/user/user/getUserInfo.do")
	@Headers({"Content-type:application/json;charset=UTF-8"})
	Call<Root<Data>> getUserInfo(@Query("userId") int userId, @Query("lawyerId") int lawyerId, @Query("curUserId") int curUserId);
}