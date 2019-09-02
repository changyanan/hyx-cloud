package org.xuenan.hyx;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author changyanan1
 * @version 1.0.0
 * @Description 精确计算
 * @date 2019年09月02日 15:27:00
 */
public class DecimalUtil {
    /**
     * 精确的加法
     *
     * @param x double类型的数字
     * @param y double类型的数字
     * @return
     */
    public static Double add(double x, double y) {
        return add(Double.toString(x), Double.toString(y));
    }

    /**
     * 精确的加法
     *
     * @param x String类型的数字
     * @param y String类型的数字
     * @return
     */
    public static Double add(String x, String y) {
        BigDecimal b1 = new BigDecimal(x);
        BigDecimal b2 = new BigDecimal(y);
        return b1.add(b2).doubleValue();
    }

    /**
     * 精确的减法
     *
     * @param x double类型的数字
     * @param y double类型的数字
     * @return
     */
    public static Double subtract(double x, double y) {
        return subtract(Double.toString(x), Double.toString(y));
    }

    /**
     * 精确的减法
     *
     * @param x String类型的数字
     * @param y String类型的数字
     * @return
     */
    public static Double subtract(String x, String y) {
        BigDecimal b1 = new BigDecimal(x);
        BigDecimal b2 = new BigDecimal(y);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 精确的乘法
     *
     * @param x double类型的数字
     * @param y double类型的数字
     * @return
     */
    public static Double multiply(double x, double y) {
        return multiply(Double.toString(x), Double.toString(y));
    }

    /**
     * 精确的乘法
     *
     * @param x String类型的数字
     * @param y String类型的数字
     * @return
     */
    public static Double multiply(String x, String y) {
        BigDecimal b1 = new BigDecimal(x);
        BigDecimal b2 = new BigDecimal(y);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 精确的除法
     *
     * @param x String类型的数字
     * @param y String类型的数字
     * @return
     */
    public static Double divide(double x, double y) {
        return divide(Double.toString(x), Double.toString(y));
    }

    /**
     * 精确的乘法
     *
     * @param x String类型的数字
     * @param y String类型的数字
     * @return
     */
    public static Double divide(String x, String y) {
        BigDecimal b1 = new BigDecimal(x);
        BigDecimal b2 = new BigDecimal(y);
        // scale指的是小数点后的位数,这里的2表示精确到小数点后面的两位小数
        // roundingMode是小数的保留模式。它们都是BigDecimal中的常量字段,有很多种。
        // 比如：BigDecimal.ROUND_HALF_UP表示的就是4舍5入
        return b1.divide(b2, 8, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 四舍五入
     *
     * @param x     数字
     * @param scale 位数
     * @return
     */
    public static Double round(double x, int scale) {
        return round(Double.toString(x), scale);
    }

    /**
     * 四舍五入
     *
     * @param x     字符串的数字
     * @param scale 位数
     * @return
     */
    public static Double round(String x, int scale) {
        return new BigDecimal(x).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

}
