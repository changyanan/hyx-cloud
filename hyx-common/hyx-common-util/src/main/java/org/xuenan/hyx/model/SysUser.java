package org.xuenan.hyx.model;

import java.util.Optional;

/**
 * @author changyanan1
 * @version 1.0.0
 * @Description TODO
 * @date 2019年09月02日 16:33:00
 */
public interface SysUser {
    public final static Optional<SysUser> empty = Optional.empty();

    /**
     * 是否登录
     *
     * @return
     */
    boolean isLogin();

    /**
     * 获取用户Id
     *
     * @return
     */
    String getUserId();
}
