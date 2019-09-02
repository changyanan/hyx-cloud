package org.xuenan.hyx.init;

import com.globalegrow.core.model.SysUser;

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
