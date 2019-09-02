package com.hyx.core.messaging;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;

@Configuration
public class ObserverAutoConfiguration implements BeanPostProcessor,InitializingBean{
	
	private static final Logger log=LoggerFactory.getLogger(ObserverAutoConfiguration.class);
	
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if(filterClaz(bean.getClass())){
			log.debug("注册观察者bean ‘{}’ 开始",beanName);
			Producer.register(bean);
			log.debug("注册观察者bean ‘{}’ 完毕",beanName);
		}
		return bean;
	}
	
	private static final boolean filterClaz(Class<?> claz) {
		Method[] methods = claz.getMethods();
		for (Method method : methods) {
			Subscribe subscribe= AnnotationUtils.findAnnotation(method, Subscribe.class);
			if(subscribe!=null)
				return true;
		}
		return false;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Producer.register(new Object(){
			@Subscribe
			public void deadEvent(DeadEvent event) {
				log.warn("事件没有找到匹配得消费者，事件被丢弃,Message: {} ,MessageType: {}", event.getEvent(),event.getEvent().getClass());
			}
		});
	} 
	
}
