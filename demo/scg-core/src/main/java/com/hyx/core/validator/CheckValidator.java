package com.hyx.core.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckValidatorImpl.class)
@Documented
public @interface CheckValidator {
	
	/**
	 * 校验使用的类
	 * @return
	 */
	Class<? extends Validator> checkValid() ;

    /**
     * 校验时使用的参数
     * @return
     */
    String[] args() default {};

	
    String message() default "校验失败";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
