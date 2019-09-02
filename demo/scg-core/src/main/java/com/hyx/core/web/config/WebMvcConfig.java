package com.hyx.core.web.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyx.core.utils.ListUtils;
import com.hyx.core.web.interceptor.LoggerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hyx.core.web.config.WebProperties.Cors;

@Configuration
@EnableConfigurationProperties(WebProperties.class)
public class WebMvcConfig implements WebMvcConfigurer {

	private static final Logger log = LoggerFactory.getLogger(WebMvcConfig.class);

	@Autowired private WebProperties corsProperties;
	@Autowired private LoggerInterceptor loggerInterceptor;
	
	public WebMvcConfig() {
		log.debug("WebMvcConfig 开始初始化....");
	}
	/**
	 * 请求拦截
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loggerInterceptor).addPathPatterns("/**");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addStatusController("/health", HttpStatus.OK);
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	}
//	/**
//	 * jsonp支持
//	 * 
//	 */
//	@ControllerAdvice
//	@EnableConfigurationProperties(WebProperties.class)
//	public static class JsonpResponseBodyAdvice extends AbstractJsonpResponseBodyAdvice {
//		public JsonpResponseBodyAdvice(WebProperties webProperties) {
//			super(webProperties.getJsonpQueryParamNames()==null?new String[]{"jsonpCallback"}:webProperties.getJsonpQueryParamNames());
//		}
//	}

	@SuppressWarnings("rawtypes")
	@Bean
	public FilterRegistrationBean corsFilter() {
		Map<String, CorsConfiguration> corsConfigurations=new HashMap<String, CorsConfiguration>();
		ListUtils.n(corsProperties.getCors()).each(cors->{
			corsConfigurations.put(cors.getPathPattern(), this.corsProperties(cors));
		});
		UrlBasedCorsConfigurationSource configSource=new UrlBasedCorsConfigurationSource();
		configSource.setAlwaysUseFullPath(true);
		configSource.setCorsConfigurations(corsConfigurations);
		@SuppressWarnings("unchecked")
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean(new CorsFilter(configSource));
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns("/*");
		return filterRegistration;
	}
	
	private CorsConfiguration corsProperties(Cors cors) {
		CorsConfiguration corsConfiguration = corsConfiguration();
		corsConfiguration.setAllowCredentials(cors.getAllowCredentials());
		corsConfiguration.setAllowedOrigins(cors.getAllowedOrigins());
		corsConfiguration.setAllowedHeaders(cors.getAllowedHeaders());
		corsConfiguration.setAllowedMethods(ListUtils.n(cors.getAllowedMethods()).list(m->m.name()).to());
		corsConfiguration.setExposedHeaders(cors.getExposedHeaders());
		corsConfiguration.setMaxAge(cors.getMaxAge());
		return corsConfiguration;
	}
	
	private CorsConfiguration corsConfiguration(){
		CorsConfiguration corsConfiguration=new CorsConfiguration(){
			@Override
			public String checkOrigin(String requestOrigin) {
				String origin = super.checkOrigin(requestOrigin);
				if(origin!=null)
					return requestOrigin;
				List<String> origins = getAllowedOrigins();
				for (String pattern : origins) {
					if(requestOrigin.contains(pattern)||pattern.matches(requestOrigin))
						return requestOrigin;
				}
				return null;
			}
		};
		return corsConfiguration;
	}

	
}
