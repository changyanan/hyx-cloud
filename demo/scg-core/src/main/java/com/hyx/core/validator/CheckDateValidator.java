package com.hyx.core.validator;

import com.hyx.core.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.util.Date;

/**
 * 自定义日期校验类型
 *
 * @author wuminjian
 */
public class CheckDateValidator implements ConstraintValidator<CheckDateTimeFormat, Date> {

    private static Logger logger = LoggerFactory.getLogger(CheckDateValidator.class);

    private int min;
    private int max;

    @Override
    public void initialize(CheckDateTimeFormat checkDateTimeFormat) {
        this.min = checkDateTimeFormat.min();
        this.max = checkDateTimeFormat.max();
    }

    @Override
    public boolean isValid(Date param, ConstraintValidatorContext constraintValidatorContext) {
        try {
            long receTime = DateUtils.date2Int(param).longValue();
            long minTime = DateUtils.getTimestamp(-min);
            long maxTime = DateUtils.getTimestamp(max);
            return receTime >= minTime && receTime < maxTime;
        } catch (ParseException e) {
            logger.error("【CheckDateValidator】时间转换异常", e);
        }
        return false;

    }
}
