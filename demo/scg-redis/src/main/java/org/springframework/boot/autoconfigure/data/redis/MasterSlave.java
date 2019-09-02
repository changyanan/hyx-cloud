package org.springframework.boot.autoconfigure.data.redis;


public interface MasterSlave {
	ThreadLocal<Boolean> threadLocal=new ThreadLocal<Boolean>() {
		   protected Boolean initialValue() {
		        return true;
		    }
	};
	/**
	 * 接下来只能在主库执行
	 */
	static void master() {
		threadLocal.set(true);
	}
	
	/**
	 * 接下来可以在从库执行
	 */
	static void slave() {
		threadLocal.set(false);
	}

	/**
	 * 判断是否可以使用从库
	 * @return
	 */
	static boolean isSlave() {
		return !threadLocal.get();
	}
	/**
	 * 判断是否必须使用主库
	 * @return
	 */
	static boolean isMaster() {
		return threadLocal.get();
	}
	/**
	 * 重置
	 */
	static void reset() {
		threadLocal.remove();
	}
}
