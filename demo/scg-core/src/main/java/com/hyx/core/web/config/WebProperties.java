package com.hyx.core.web.config;

import java.util.ArrayList;
import java.util.List;

import com.hyx.core.model.SysModel;
import com.hyx.core.utils.ListUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;

@ConfigurationProperties(WebProperties.prefix)
public class WebProperties extends SysModel {

	private static final long serialVersionUID = 1L;
	static final String prefix="globalegrow";
	
	/**
	 * 是否调试模式,为true时会输出较多异常信息
	 */
	private boolean debug=false;
	
	private Limiter limiter=new Limiter();
	/**
	 * 跨域设置的参数
	 */
	private List<Cors> cors;
	
	/**
	 * jsonP允许的请求参数名
	 */
	private String[] jsonpQueryParamNames;
	
	public List<Cors> getCors() {
		return cors;
	}

	public void setCors(List<Cors> cors) {
		this.cors = cors;
	}
	
	public String[] getJsonpQueryParamNames() {
		return jsonpQueryParamNames;
	}

	public void setJsonpQueryParamNames(String[] jsonpQueryParamNames) {
		this.jsonpQueryParamNames = jsonpQueryParamNames;
	}


	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public Limiter getLimiter() {
		return limiter;
	}

	public void setLimiter(Limiter limiter) {
		this.limiter = limiter;
	}



	public static class Limiter extends SysModel{

		private static final long serialVersionUID = 1L;
		
		private boolean enabled =true;

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}

	public static class Cors extends SysModel{
		 
		private static final long serialVersionUID = 1L;

		/**
		 * 匹配的请求参数，默认*
		 */
		private String pathPattern="*";

		/**
		 * 用来指定本次预检请求的有效期，单位为秒。默认值24小时
		 */
		private Long maxAge=24L*60*60;
		
		/**
		 * 允许跨域的域名
		 */
		private List<String> allowedOrigins=new ArrayList<>();
		
		/**
		 * 请求允许跨域的方法,默认HEAD，OPTIONS
		 */
		private List<HttpMethod> allowedMethods= ListUtils.n(HttpMethod.HEAD,HttpMethod.OPTIONS).to();
		
		/**
		 * 允许跨域的请求头,默认值 Accept,Accept-Encoding,Accept-Language,Connection,Content-Length,Content-Type,Cookie,Host,Origin,Referer,User-Agent,timestamp,sign
		 */
 
		private List<String> allowedHeaders=ListUtils.n("Accept,Accept-Encoding,Accept-Language,Connection,Content-Length,Content-Type,Cookie,Host,Origin,Referer,User-Agent,timestamp,sign,curPlatform".split(",")).to();
		
		/**
		 * 不允许跨域的请求头
		 */
		private List<String> exposedHeaders=new ArrayList<>();
		
		/**
		 * 手否接收Cookie，默认值是false
		 */
		private Boolean allowCredentials=false;

		public String getPathPattern() {
			return pathPattern;
		}

		public void setPathPattern(String pathPattern) {
			this.pathPattern = pathPattern;
		}

		public List<String> getAllowedOrigins() {
			return allowedOrigins;
		}

		public void setAllowedOrigins(List<String> allowedOrigins) {
			this.allowedOrigins = allowedOrigins;
		}

		public List<HttpMethod> getAllowedMethods() {
			return allowedMethods;
		}

		public void setAllowedMethods(List<HttpMethod> allowedMethods) {
			this.allowedMethods = allowedMethods;
		}

		public List<String> getAllowedHeaders() {
			return allowedHeaders;
		}

		public void setAllowedHeaders(List<String> allowedHeaders) {
			this.allowedHeaders = allowedHeaders;
		}

		public List<String> getExposedHeaders() {
			return exposedHeaders;
		}

		public void setExposedHeaders(List<String> exposedHeaders) {
			this.exposedHeaders = exposedHeaders;
		}

		public Boolean getAllowCredentials() {
			return allowCredentials;
		}

		public void setAllowCredentials(Boolean allowCredentials) {
			this.allowCredentials = allowCredentials;
		}

		public Long getMaxAge() {
			return maxAge;
		}

		public void setMaxAge(Long maxAge) {
			this.maxAge = maxAge;
		}
	}
}
