package com.hyx.core.model;

import java.util.Optional;

public interface SysUser {

	public final static Optional<SysUser> empty = Optional.empty();

	boolean isLogin();

	String getUserId();
}
