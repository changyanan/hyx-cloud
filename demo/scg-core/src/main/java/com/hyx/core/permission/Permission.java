package com.hyx.core.permission;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解适用于控制器的类上和方法上
 * @author leige
 *
 */
@Documented
@Target({ TYPE, METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
	
	/**
	 * 权限被校验时，可以收到
	 * @return
	 */
	String value() default "";
	
	/**
	 * 权限所属的组,可以定义多个
	 * @return
	 */
	String[] group() default {} ; 
	
}
