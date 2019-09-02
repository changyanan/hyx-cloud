package com.sockjs.test.issapi.api;

import com.sockjs.test.issapi.Const;
import com.sockjs.test.issapi.model.ResponseEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface CheckAccountsApi {

	@Multipart
	@Headers({"client-origin:PMS"})
	@POST(Const.API_PREFIX+"/checkAccounts/submit")
	ResponseEntity<Object> submit(@Part okhttp3.MultipartBody.Part file,@Query("cycleBegin") String cycleBegin);
	
	@FormUrlEncoded
	@Headers({"client-origin:PMS"})
	@POST(Const.API_PREFIX+"/checkAccounts/submitFileName")
	ResponseEntity<Object> submitFileName(@Field("checkFileName") String checkFileName,@Field("cycleBegin") String cycleBegin);
}
