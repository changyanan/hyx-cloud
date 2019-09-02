package com.hyx.core.logging;

import java.lang.reflect.Method;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyx.core.Context;
import com.hyx.core.init.ControllerPost;
import com.hyx.core.init.ControllerPre;
import com.hyx.core.utils.ClasseUtils;

/**
 * Aop 访问日志
 * 
 * @author lenovo
 *
 */
@Aspect
@Component
public class ControllerLogAop implements ApplicationContextAware {

	private volatile Map<String, ControllerPre> preMaps;
	private volatile Map<String, ControllerPost> postMaps;

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Around("within(@org.springframework.web.bind.annotation.RestController *)")
	public Object restRecordSysLog(ProceedingJoinPoint point) throws Throwable {
		Method method = ((MethodSignature) point.getSignature()).getMethod();
		Context.isRest(true);
		return recordSysLog(point, method);
	}

	@Around("within(@org.springframework.stereotype.Controller *)")
	public Object otherRecordSysLog(ProceedingJoinPoint point) throws Throwable {
		Method method = ((MethodSignature) point.getSignature()).getMethod();
		Context.isRest(ClasseUtils.hasAnnotation(method, ResponseBody.class));
		return recordSysLog(point, method);
	}

	public Object recordSysLog(ProceedingJoinPoint point, Method method) throws Throwable {
		Context.put("ControllerMethod", method);
		boolean now = Context.now();
		this.pre(point, method);
		String methodName = ClasseUtils.getMethodName(method);
		RunTimeLog.bedin();
		try {
			Object relust = point.proceed();
			this.post(point, method, relust, null);
			return relust;
		} catch (Throwable e) {
			this.post(point, method, null, e);
			throw e;
		} finally {
			RunTimeLog.log("方法执行结束:{} ,[参数]:{}", methodName, new Object() {
				@Override
				public String toString() {
					StringBuffer stringBuffer = new StringBuffer();
					for (Object object : point.getArgs()) {
						stringBuffer.append(" , ");
						stringBuffer.append(String.valueOf(object));
					}
					return stringBuffer.toString();
				}
			});
			if (now) {
				Context.remove();
			}
		}
	}

	private void pre(ProceedingJoinPoint point, Method method) {
		if (preMaps == null)
			preMaps = applicationContext.getBeansOfType(ControllerPre.class);
		preMaps.values().forEach(cp -> cp.apply(point, method));
	}

	private void post(ProceedingJoinPoint point, Method method, Object relust, Throwable throwable) {
		if (postMaps == null)
			postMaps = applicationContext.getBeansOfType(ControllerPost.class);
		postMaps.values().forEach(cp -> cp.apply(point, method, relust, throwable));
	}
}
