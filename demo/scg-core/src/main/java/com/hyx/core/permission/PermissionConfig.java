package com.hyx.core.permission;

import java.lang.reflect.Method;

import com.hyx.core.Context;
import com.hyx.core.init.ControllerPre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;

import com.hyx.core.exception.GlobalException;
import com.hyx.core.exception.GlobalExceptionStatus;

@Configuration
public class PermissionConfig {

	@Autowired(required = false)
	private PermissionCheck permissionCheck;
	private boolean check(Method method, Class<?> thisClass) {

		Permission permissionClaz = AnnotationUtils.findAnnotation(thisClass, Permission.class);
		Permission permissionMethod = AnnotationUtils.findAnnotation(method, Permission.class);
		if(permissionClaz==null&&permissionMethod==null)
			return true;
		String[] groups = null;
		String name = null;
		if(permissionClaz!=null){
			groups=permissionClaz.group();
			name=permissionClaz.value();
		}
		if(permissionMethod!=null){
			groups=permissionMethod.group();
			name=permissionMethod.value();
		}
		for (String group : groups) {
			return permissionCheck.check(group, name);
		}
		return true;
	}

	@Bean
    ControllerPre controllerPre() {
		GlobalException globalException = new GlobalException(GlobalExceptionStatus.UNAUTHORIZED);
		return (point, method) -> {
			if (permissionCheck != null && Context.getRequest() != null) {
				if (!this.check(method, point.getThis().getClass())) {
					throw globalException;
				}
			}
		};
	}
}
