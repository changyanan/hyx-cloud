package com.hyx.core.permission;

@FunctionalInterface
public interface PermissionCheck {
	
	boolean check(String group, String name);
	
}
