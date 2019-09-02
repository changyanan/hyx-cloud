package com.sockjs.test.utils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import okhttp3.CookieJar;
import okhttp3.Interceptor.Chain;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utils {

	private static final Logger log = LoggerFactory.getLogger(Utils.class);

//	public static String appKey;
//	public static String appSecret;

	public static Retrofit create(String apiUrl) {
		return new Retrofit.Builder().baseUrl(apiUrl).addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(new CallAdapter.Factory() {

					@Override
					public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
						return new CallAdapter<Object, Object>() {
							@Override
							public Type responseType() {
								return returnType;
							}

							@Override
							public Object adapt(Call<Object> call) {
								try {
									Response<Object> re = call.execute();
									if (!re.isSuccessful()) {
										try {
											return new Gson().fromJson(re.errorBody().string(), returnType);
										} catch (Exception e) {
											throw new RRuntimeException(re);
										}
									}
									return re.body();
								} catch (IOException e) {
									throw new RuntimeException(e);
								}
							}
						};
					}
				})
				.client(new OkHttpClient.Builder().readTimeout(15, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS)
						.connectTimeout(10, TimeUnit.SECONDS).cookieJar(CookieJar.NO_COOKIES)
						.addInterceptor((Chain chain) -> {
//							if(appKey==null) {
							return chain.proceed(chain.request());
//							}
//							return chain.proceed(sign(chain.request()));
						}).build())
				.build();
	}

//	private static Request sign(Request request) {
//		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date());
//		HttpUrl url = request.url();
//		Map<String, List<String>> paramMap = new HashMap<>();
//		url.queryParameterNames().forEach(name -> {
//			paramMap.put(name, url.queryParameterValues(name));
//		});
//		paramMap.put("appKey", Arrays.asList(appKey));
//		paramMap.put("timestamp", Arrays.asList(timestamp));
//		RequestBody body = request.body();
//		requestBodyToParamMap(body, paramMap);
//		HttpUrl.Builder urlBuilder = url.newBuilder();
//		urlBuilder.addEncodedQueryParameter("appKey", appKey);
//		urlBuilder.addEncodedQueryParameter("timestamp", timestamp);
//		urlBuilder.addEncodedQueryParameter("sign", sign(paramMap,appSecret));
////		log.info("发请求的地址 {}" ,urlBuilder.build());
//		return request.newBuilder().url(urlBuilder.build()).build();
//	}
//	private static Pattern pattern=Pattern.compile("name=\"(.*)\"");

//	private static void requestBodyToParamMap(RequestBody body,Map<String, List<String>> paramMap) {
//		if(body==null) {
//			return;
//		}else if(body instanceof FormBody) {
//			FormBody formBody=(FormBody) body;
//			int size = formBody.size();
//			for (int i = 0; i < size; i++) {
//				paramMap.put(formBody.name(i), Arrays.asList(formBody.value(i)));
//			}
//		}else if(body instanceof MultipartBody) {
////			MultipartBody formBody=(MultipartBody) body;
////			formBody.parts().forEach(part->{
////				try {
////					Field h = part.getClass().getDeclaredField("headers");
////					Field b = part.getClass().getDeclaredField("body");
////					h.setAccessible(true);
////					b.setAccessible(true);
////					Headers headers=(Headers) h.get(part);
////					RequestBody rbody=(RequestBody) b.get(part);
////					String cd = headers.get("Content-Disposition");
////					if(cd!=null&&!cd.contains("\"; filename=\"")) {
////						Matcher m = pattern.matcher(cd);
////						if(m.find()) {
////							Buffer sink=new Buffer();
////							rbody.writeTo(sink);
////							paramMap.put(m.group(1), Arrays.asList(new Gson().fromJson(sink.readUtf8(), String.class)));
////						}
////					}
////				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException | IOException e) {
////					log.info("获取part异常" , e);
////				}
////			});
//		}else if(body.getClass().getSimpleName().equals("ContentTypeOverridingRequestBody")) {
//			try {
//				requestBodyToParamMap((RequestBody) body.getClass().getField("delegate").get(body), paramMap);
//			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
//				log.info("获取RequestBody异常" , e);
//			}
//		}else {
////			log.info("未被处理的RequestBody {}" ,body.getClass());
//		}
//	}
//appKeypms-test-app-keytimestamp2018-06-19 20:45:28.600pms-test-app-secret
//appKeypms-test-app-keycycleBegin2018-06-19 19timestamp2018-06-19 20:45:28.600pms-test-app-secret
//	public static String sign(Map<String, List<String>> params,String appSecret) {
//		String paramStr = ListUtils.n(params.keySet()).order(key -> key).list(key -> {
//			String vals = ListUtils.n(params.get(key)).order(val -> val).join();
//			return key + vals;
//		}).join();
//		String signStr = paramStr + appSecret;
//		String sign=DigestUtils.md5DigestAsHex(signStr.getBytes());
////		log.info("签名的字符串是  	{}" ,signStr);
////		log.info("签名是 		{}" ,sign);
//		return sign;
//	}
}
