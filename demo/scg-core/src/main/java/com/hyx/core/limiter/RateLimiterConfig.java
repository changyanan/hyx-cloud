package com.hyx.core.limiter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;

import com.hyx.core.exception.GlobalException;
import com.hyx.core.exception.GlobalExceptionStatus;
import com.hyx.core.init.ControllerPre;
import com.hyx.core.web.config.WebProperties;
import com.google.common.util.concurrent.RateLimiter;

@Configuration
@EnableConfigurationProperties(WebProperties.class)
public class RateLimiterConfig {

	private static final Logger log = LoggerFactory.getLogger(RateLimiterConfig.class);

	private final Map<Method, RateLimiter> map = new HashMap<>();

	private RateLimiter getRateLimiter(Method method) {
		if (map.containsKey(method)) {
			return map.get(method);
		}
		com.hyx.core.limiter.RateLimiter rateLimiter = AnnotationUtils.findAnnotation(method,
				com.hyx.core.limiter.RateLimiter.class);
		if (rateLimiter == null) {
			return null;
		}
		RateLimiter limiter = RateLimiter.create(rateLimiter.value());
		RateLimiter reLimiter = map.putIfAbsent(method, limiter);
		if (reLimiter != null) {
			return reLimiter;
		}
		return limiter;
	}

	@Bean
	public ControllerPre limiter(WebProperties webProperties) {
		return (point, method) -> {
			if(!webProperties.getLimiter().isEnabled()) {
				log.info("未开启流控限制");
				return;
			}
			
			RateLimiter limiter = getRateLimiter(method);
			if (limiter != null && !limiter.tryAcquire()) {
				log.warn("{}.{} 触发流量控制", method.getDeclaringClass(), method.getName());
				GlobalException.exception(GlobalExceptionStatus.FLOW_CONTROL);
			}
		};
	}
}
