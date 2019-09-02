package com.hyx.core.logging;

import org.springframework.context.annotation.ComponentScan;

import com.hyx.core.permission.PermissionConfig;

@ComponentScan(basePackageClasses = { 
		ServiceRunTimeAop.class, 
		ControllerLogAop.class, 
		RunTimeLog.class,
		PermissionConfig.class
		})
public class LoggingAutoConfiguration {

}
