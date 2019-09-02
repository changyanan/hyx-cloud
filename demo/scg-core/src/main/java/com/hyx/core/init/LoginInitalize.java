package com.hyx.core.init;

import com.hyx.core.model.SysUser;

/**
 * 系统获取当前登录用户的接口
 * @author leige
 *
 */
@FunctionalInterface
public interface LoginInitalize {

	/**
	 * 系统获取当前登录用户的接口
	 * @return
	 */
	SysUser findLoginUser();

}
