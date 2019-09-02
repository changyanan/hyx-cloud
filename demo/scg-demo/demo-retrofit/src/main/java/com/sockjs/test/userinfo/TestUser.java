package com.sockjs.test.userinfo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.Interceptor.Chain;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class TestUser {
	public static final String API_URL = "http://test-apiv2.globalegrow.com";
	public static final String LOCAL_API_URL = "http://pingpp-callback.tunnel.qydev.com/";

	public static void main( String... args) throws IOException {
		OkHttpClient client= new OkHttpClient.Builder()
		.cache(new Cache(new File( "http_cache"), 1024 * 1024 * 100))                
		.readTimeout(15, TimeUnit.SECONDS) 
		.writeTimeout(10, TimeUnit.SECONDS) 
		.connectTimeout(10, TimeUnit.SECONDS)
		.cookieJar(CookieJar.NO_COOKIES)
		.addInterceptor((Chain chain)->{
			Request request = chain.request();
			okhttp3.Request.Builder builder= request.newBuilder();
			builder.addHeader("curUserId", "1")
			.addHeader("timestamp","1")
			.addHeader("curPlatform","ios")
			.addHeader("now","yes")
			.addHeader("sign", "test");
			return chain.proceed(builder.build());
		})
		.build(); 

		Gson gson = getGson();
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(API_URL)
//				.baseUrl(LOCAL_API_URL)
				.addConverterFactory(GsonConverterFactory.create(gson))
				.client(client)
				.build();

		UserService userService = retrofit.create(UserService.class);

		Call<Root<Data>> call = userService.getUserInfo(1, 1, 1);
		Root<Data> userinfo = call.execute().body();
		 System.err.println(gson.toJson(userinfo));
	}
	
	private static Gson getGson() {
		return new GsonBuilder()
				.registerTypeHierarchyAdapter(Root.class,  new JsonDeserializer<Root<?>>() {
					@Override
					public Root<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
						JsonObject jsonObject = json.getAsJsonObject();
						Root<Object>root=new Root<>();
						try { root.setCurtime(jsonObject.get("curtime").getAsLong()); } catch (Exception e) { }
						try { root.setLevel(jsonObject.get("level").getAsString()); } catch (Exception e) { }
						try { root.setMessage(jsonObject.get("message").getAsString()); } catch (Exception e) { }
						try { root.setSucceed(jsonObject.get("succeed").getAsBoolean()); } catch (Exception e) { }
						try { root.setToken(jsonObject.get("token").getAsString());} catch (Exception e) { }
						try { root.setGoUrl(jsonObject.get("goUrl").getAsString()); } catch (Exception e) { }
						try { root.setErrorCode(jsonObject.get("errorCode").getAsString()); } catch (Exception e) { }
						try { root.setDetails(jsonObject.get("details").getAsString()); } catch (Exception e) { }
						try { 
							Type datatype = ((ParameterizedType)typeOfT).getActualTypeArguments()[0];
							JsonElement data = jsonObject.get("data");
							root.setData(context.deserialize(data, datatype)); 
							} catch (Exception e) {
								e.printStackTrace();
							}
						return root;
					}
				})
				.create();
	}
	
//	public static void main1(String[] args) {
//		String json="{\"curtime\":1504084115800,\"data\":\"\",\"level\":\"DEBUG\",\"message\":\"操作成功\",\"succeed\":true,\"token\":\"\"}";
//		Gson gson = getGson();
//		Object d = gson.fromJson(json, type(Root.class, Data.class));
//		System.err.println(gson.toJson(d));
//	}
//	
//	static ParameterizedType type(final Class<?> raw, final Type... args) {
//		return new ParameterizedType() {
//			public Type getRawType() {
//				return raw;
//			}
//			public Type[] getActualTypeArguments() {
//				return args;
//			}
//			public Type getOwnerType() {
//				return null;
//			}
//		};
//	}
////	
}