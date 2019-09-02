package org.xuenan.hyx.exception;

/**
 * @author changyanan1
 * @version 1.0.0
 * @Description 异常状态
 * @date 2019年09月02日 15:58:00
 */
public interface ExceptionStatus {

    int digit = 100000000;

    /**
     * 获取编码
     *
     * @return
     */
    int getCode();

    /**
     * 异常类型
     *
     * @return
     */
    ExceptionLevel getLevel();

    /**
     * 异常信息
     *
     * @return
     */
    String getMsg();
}
