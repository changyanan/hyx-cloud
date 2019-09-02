package org.xuenan.hyx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author changyanan1
 * @version 1.0.0
 * @Description 数值转换
 * @date 2019年09月02日 15:25:00
 */
public abstract class NumberUtils {
    private static final Logger log = LoggerFactory.getLogger(NumberUtils.class);

    public static boolean isNumber(String source) {
        if (StringUtils.isEmpty(source)) {
            return false;
        }
        try {
            Long.parseLong(source);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDouble(String source) {
        if (StringUtils.isEmpty(source)) {
            return false;
        }
        try {
            Double.parseDouble(source);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Long parseLong(String source) {
        if(StringUtils.isEmpty(source)) {
            return null;
        }
        try {
            return Long.parseLong(source);
        } catch (Exception e) {
            log.warn("将{}转化为Long失败", source);
        }
        return null;
    }

    public static Integer parseInt(String source) {
        if(StringUtils.isEmpty(source)) {
            return null;
        }
        try {
            return Integer.parseInt(source);
        } catch (Exception e) {
            log.warn("将{}转化为Integer失败", source);
        }
        return null;
    }

    public static Double parseDouble(String source) {
        if(StringUtils.isEmpty(source)) {
            return null;
        }
        try {
            return Double.parseDouble(source);
        } catch (Exception e) {
            log.warn("将{}转化为Double失败", source);
        }
        return null;
    }

    public static Float parseFloat(String source) {
        if(StringUtils.isEmpty(source)) {
            return null;
        }
        try {
            return Float.parseFloat(source);
        } catch (Exception e) {
            log.warn("将{}转化为Float失败", source);
        }
        return null;
    }
}
