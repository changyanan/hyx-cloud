package com.globalegrow.scg.rabbitmq.autoconfigure;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

@Configurable
class BeanRegisterUtlis implements BeanDefinitionRegistryPostProcessor {
	private static ConfigurableListableBeanFactory beanFactory;
	private static BeanDefinitionRegistry registry;
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		BeanRegisterUtlis.beanFactory = beanFactory;
	}
	
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		BeanRegisterUtlis.registry=registry;
	}

	public final static void register(String beanName,Object object) {
		beanFactory.registerSingleton((beanName), object);
	}
	
	public final static void register(String beanName,Class<?> beanClass, String factoryMethodName,Object... argBeanNames) {
		BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.rootBeanDefinition( beanClass,factoryMethodName);
		for (Object arg : argBeanNames) {
			if(arg !=null && arg.getClass().equals(String.class)) {
				definitionBuilder.addConstructorArgReference((String)arg);
			}else {
				definitionBuilder.addConstructorArgValue(arg);
			}
		}
		registry.registerBeanDefinition((beanName), definitionBuilder.getRawBeanDefinition());
	}
 
}
