package com.globalegrow.async.core;

import java.lang.reflect.Method;

class Pack{
		Class<?> targetClass;
		Object bean;
		String beanName; 
		Method method; 
		Async async;
		public Pack(Class<?> targetClass, Object bean, String beanName, Method method, Async async) {
			super();
			this.targetClass = targetClass;
			this.bean = bean;
			this.beanName = beanName;
			this.method = method;
			this.async = async;
		}
		public Class<?> getTargetClass() {
			return targetClass;
		}
		public void setTargetClass(Class<?> targetClass) {
			this.targetClass = targetClass;
		}
		public Object getBean() {
			return bean;
		}
		public void setBean(Object bean) {
			this.bean = bean;
		}
		public String getBeanName() {
			return beanName;
		}
		public void setBeanName(String beanName) {
			this.beanName = beanName;
		}
		public Method getMethod() {
			return method;
		}
		public void setMethod(Method method) {
			this.method = method;
		}
		public Async getAsync() {
			return async;
		}
		public void setAsync(Async async) {
			this.async = async;
		}
	}