package com.hyx.core.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.hyx.core.utils.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckValidatorImpl implements ConstraintValidator<CheckValidator, Object>{
	
	private static final Logger log = LoggerFactory.getLogger(CheckValidatorImpl.class);

	private Validator validator;
	private String[] args;
	private Class<? extends Validator> validatorClaz;
	@Override
	public void initialize(CheckValidator constraintAnnotation) {
		this.validatorClaz = constraintAnnotation.checkValid();
		this.args = constraintAnnotation.args();
		this.validator = SpringContextHolder.getBean(validatorClaz);
		if(this.validator==null) {
			log.warn("spring上下文未找到校验类{}的实例，将使用创建实例", this.validatorClaz);
			try {
				this.validator = this.validatorClaz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException("初始化校验类("+ this.validatorClaz +")失败", e);
			}
		}
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		try {
			return validator.isValid(value, args);
		} catch (Exception e) {
			log.info("校验类执行出错,{}", this.validatorClaz,e);
			return false;
		}
	}

}
