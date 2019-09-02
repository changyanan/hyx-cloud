package com.globalegrow.async.core;

import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.AnnotationUtils;

import com.globalegrow.async.protocol.Protocol;
import com.globalegrow.async.serialize.Serialization;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.globalegrow.async")
public class AsyncAutoConfiguration implements BeanPostProcessor, InitializingBean, ApplicationContextAware,CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(AsyncAutoConfiguration.class);
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Class<?> targetClass = AopUtils.getTargetClass(bean);
		Method[] methods = targetClass.getMethods();
		for (Method method : methods) {
			Async async = AnnotationUtils.findAnnotation(method, Async.class);
			if (async != null) {
				AsyncCore.register(targetClass, bean, beanName, method, async);
			}
		}
		return bean;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, Protocol> protocols = applicationContext.getBeansOfType(Protocol.class);
		protocols.values().forEach(AsyncCore::putProtocol);
		Map<String, Serialization> serializes = applicationContext.getBeansOfType(Serialization.class);
		serializes.values().forEach(AsyncCore::putSerialize);
	}

	@Bean
	public AsyncAspect asyncAspect() {
		return new AsyncAspect();
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("开始接收异步数据。。。");
		AsyncCore.receive();
	}
}
