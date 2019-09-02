package com.hyx.core.logging;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.hyx.core.Context;
import com.hyx.core.utils.ClasseUtils;

/**
 * Aop 访问日志
 * 
 * @author lenovo
 *
 */
@Aspect
@Order(1)
@Component
public class ServiceRunTimeAop {

	@Pointcut("within(@org.springframework.stereotype.Service *)")
	public void serviceRunTime() {
	}

	@Pointcut("within(@org.springframework.stereotype.Repository *)")
	public void repositoryRunTime() {
	}

	@Around("repositoryRunTime() || serviceRunTime()")
	public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {

		boolean now = Context.now();
		Method method = ((MethodSignature) point.getSignature()).getMethod();
		String methodName = ClasseUtils.getMethodName(method);

		Object args = getParamObject(point.getArgs());
		RunTimeLog.bedin();
		try {
			return point.proceed();
		} finally {
			RunTimeLog.log(" 方法执行结束 {},[参数]:{}", methodName, args);
			if (now)
				Context.remove();
		}
	}

	private Object getParamObject(Object[] param) {
		return new Object() {
			@Override
			public String toString() {
				StringBuffer stringBuffer = new StringBuffer();
				for (Object object : param) {
					stringBuffer.append(" , ");
					stringBuffer.append(String.valueOf(object));
				}
				return stringBuffer.toString();
			}
		};
	}
}
