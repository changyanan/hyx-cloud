package org.xuenan.hyx.model;

import org.xuenan.hyx.exception.ExceptionLevel;

/**
 * @author changyanan1
 * @version 1.0.0
 * @Description TODO
 * @date 2019年09月02日 14:54:00
 */
public interface Entity<T> {
    /**
     * 请求是否响应成功
     *
     * @return
     */
    boolean getSuccess();

    /**
     * 请求响应的数据报文体
     *
     * @return
     */
    T getData();

    /**
     * 请求的响应码
     *
     * @return
     */
    int getCode();

    /**
     * 请求响应的消息字符串
     *
     * @return
     */
    String getMessage();

    /**
     * 请求响应的消息的级别
     *
     * @return
     */
    ExceptionLevel getLevel();

    /**
     * 服务器响应的时间
     *
     * @return
     */
    long getCurtime();
}
