package com.hyx.core.utils;

import com.hyx.core.validator.Validator;
import org.springframework.stereotype.Component;

/**
 * @author changyanan1
 * @version 1.0.0
 * @Description TODO
 * @date 2019年09月02日 17:19:00
 */
@Component
public class CheckedValidator implements Validator {
    @Override
    public boolean isValid(Object value, String[] args) {
        if (value == null || StringUtils.isEmpty(String.valueOf(value))) {
            return true;
        }
        return ListUtils.n(args).cv(String.valueOf(value));

    }
}
