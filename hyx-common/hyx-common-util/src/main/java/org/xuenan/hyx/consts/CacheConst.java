package org.xuenan.hyx.consts;

/**
 * 
 * @Package: com.tomtop._const
 * @ClassName: CacheConst
 * @Description: 缓存KEY获取公共类
 *
 * @author: lei
 * @date: 2016年8月18日 下午4:54:47
 * @version V1.0
 * 
 * 
 */
public interface CacheConst {
	static StringBuffer generate() {
		return new StringBuffer("globalegrow");
	};

	static String generateMethodCallCacheKey(String type, String method, Object... args) {
		StringBuffer rediskey = generate().append(":").append(type).append(":").append(method);
		for (Object object : args) {
			rediskey.append("[");
			rediskey.append(object);
			rediskey.append("]");
		}
		return rediskey.toString();
	}

	static String generateMethodCacheCacheKey(String method, Object... args) {
		return CacheConst.generateMethodCallCacheKey("METHODCACHE", method, args);
	};

	static String generateDoHttpRequestCacheKey(String method, Object... args) {
		return CacheConst.generateMethodCallCacheKey("DOHTTPREQUEST", method, args);
	};
}
