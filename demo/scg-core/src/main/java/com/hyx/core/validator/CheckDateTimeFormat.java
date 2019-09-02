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
@Constraint(validatedBy = CheckDateValidator.class)
@Documented
public @interface CheckDateTimeFormat {
    String message() default "时间有误";

    int min() default 3;

    int max() default 3;


    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
