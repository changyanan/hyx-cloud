package com.hyx.core.web.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyx.core.Context;
import com.hyx.core.init.RequestAfterService;
import com.hyx.core.init.RequestPostService;
import com.hyx.core.init.RequestPreService;
import com.hyx.core.logging.RunTimeLog;
import com.hyx.core.model.ResponseEntity;
import com.hyx.core.utils.HttpOutJson;
import com.hyx.core.utils.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hyx.core.exception.GlobalException;
import com.hyx.core.exception.GlobalExceptionStatus;

/**
 * 记录收到的http请求.
 * 
 * @author Administrator
 *
 */
@Component
public class LoggerInterceptor extends HandlerInterceptorAdapter implements ApplicationContextAware {
	private final static Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);

	private Map<String, RequestAfterService> requestAfters;
	private Map<String, RequestPostService> requestPosts;
	private Map<String, RequestPreService> requestPres;
	private ApplicationContext applicationContext;
	private ResponseEntity<?> access_denied = ResponseEntity.fail("不允许访问")
			.exceptionStatus(GlobalExceptionStatus.UNAUTHORIZED);

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		logger.debug("LoggerInterceptor 开始初始化 .....");
		this.applicationContext = applicationContext;
	}

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws Exception {
		Context.init(request, response);
		String url = Context.getRequestUri();
		logger.debug("请求到达....  {}", url);
		Boolean state = this.handlePre(handler);
		if (state != null) {
			if (state == true) {
				logger.debug("已接受到请求:{}", url);
//				Context.sendUserId();
				return true;
			} else {
				logger.info("资源{}不允许访问!", url);
				if (!HttpOutJson.isOutEd())
					HttpOutJson.out(access_denied);
			}
		}
		Context.remove();
		return false;
	}

	/**
	 * 返回true时表示校验成功，返回false是表示不允许访问，返回null表示校验失败不需要再次响应
	 * 
	 * @param handler
	 * @return
	 */
	private Boolean handlePre(final Object handler) {
		if (requestPres == null) {
			this.requestPres = applicationContext.getBeansOfType(RequestPreService.class);
		}
		if (MapUtils.isEmpty(requestPres)) {
			return true;
		}
		try {
			for (RequestPreService requestPreService : requestPres.values()) {
				if (!requestPreService.handle(handler)) {
					return false;
				}
			}
			return true;
		} catch (GlobalException e) {
			HttpOutJson.out(ResponseEntity.fail(e));
		} catch (Exception e) {
			HttpOutJson.out(ResponseEntity.fail(e));
			logger.error("权限请求处理前出现位未知异常", e);
		}
		return null;
	}

	@Override
	public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
			final ModelAndView modelAndView) throws Exception {
		if (requestPosts == null) {
			this.requestPosts = applicationContext.getBeansOfType(RequestPostService.class);
		}
		if (MapUtils.isNotEmpty(requestPosts)) {
			requestPosts.values().forEach(requestPostService -> requestPostService.handle(handler, modelAndView));
		}
	}

	@Override
	public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
			final Object handler, final Exception ex) throws Exception {
		try {
			if (requestAfters == null) {
				this.requestAfters = applicationContext.getBeansOfType(RequestAfterService.class);
			}

			if (MapUtils.isNotEmpty(requestAfters)) {
				requestAfters.values().forEach(requestAfterService -> requestAfterService.completion(handler, ex));
			}
			String url = request.getRequestURI();
			long runtime = (System.currentTimeMillis() - Context.getBeginTime());
			RunTimeLog.log(runtime, "请求已结束:{}", url);
			if (ex != null) {
				logger.error("请求{}出错 : {}", url, ex.getMessage());
			}
		} finally {
			Context.remove();
		}
	}
}