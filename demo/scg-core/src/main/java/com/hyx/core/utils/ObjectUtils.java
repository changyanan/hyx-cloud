package com.hyx.core.utils;

public abstract class ObjectUtils {
	public static boolean isNotNull(Object... args) {
		for (Object object : args) {
			if (object == null)
				return false;
		}
		return true;
	}

	public static boolean isNull(Object... args) {
		return !isNotNull(args);
	}

	public static boolean isNotNull(ObjectSteam... args) {
		for (ObjectSteam objectSteam : args) {
			if (objectSteam.apply() == null)
				return false;
		}
		return true;
	}

	public static boolean isNull(ObjectSteam... args) {
		return !isNotNull(args);
	}

	@FunctionalInterface
	public static interface ObjectSteam {
		Object apply();
	}

}
