 package com.globalegrow.scg.rabbitmq.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySources;
import org.springframework.util.Assert;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.SystemPropertyUtils;

public class ConfigUtils {
		
		private static final Logger log = LoggerFactory.getLogger(ConfigUtils.class);

		private static PropertySources getSources(Environment environment) {
			Assert.notNull(environment, "Environment must not be null");
			Assert.isInstanceOf(ConfigurableEnvironment.class, environment,
					"Environment must be a ConfigurableEnvironment");
			return ((ConfigurableEnvironment) environment).getPropertySources();
		}
		@SuppressWarnings("unchecked")
		public static <T> T getConfig(Environment environment,String prefix, Class<T> claz) {
			try {
				try {
					Class<?> binderClass = Class.forName("org.springframework.boot.context.properties.bind.Binder");
					Class<?> prClass = Class.forName("org.springframework.boot.context.properties.bind.PlaceholdersResolver");
					Class<?> psprClass = Class.forName("org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver");
					Class<?> cpsClass = Class.forName("org.springframework.boot.context.properties.source.ConfigurationPropertySources");
					PropertyPlaceholderHelper helper=new PropertyPlaceholderHelper(SystemPropertyUtils.PLACEHOLDER_PREFIX,
							SystemPropertyUtils.PLACEHOLDER_SUFFIX,
							SystemPropertyUtils.VALUE_SEPARATOR, true);
					PropertySources sources = getSources(environment);
					
					Object placeholdersResolver = psprClass.getConstructor(Iterable.class, helper.getClass() ).newInstance(sources,helper);
					Object bindObject = binderClass.getConstructor(Iterable.class,prClass).newInstance(cpsClass.getMethod("get", Environment.class).invoke(cpsClass, environment),placeholdersResolver);
					Object bindResult = bindObject.getClass().getMethod("bind", String.class, Class.class).invoke(bindObject, prefix, claz);
					return (T) bindResult.getClass().getMethod("get").invoke(bindResult);
				} catch (ClassNotFoundException e) {
					log.trace("以springboot2.0方式获取配置失败", e);
					try {
						Class<?> propClass = Class.forName("org.springframework.boot.bind.PropertySourcesBinder");
						Object propObject = propClass.getConstructor(ConfigurableEnvironment.class).newInstance(environment);
						T retObject = claz.newInstance();
						propClass.getMethod("bindTo", String.class, Object.class).invoke(propObject, prefix, retObject);
						return retObject;
					} catch (ClassNotFoundException e2) {
						log.trace("不支持的spring-boot配置环境", e2);
						throw new RuntimeException(e2);
					}
				}
			} catch (Exception e) {
				log.trace("解析配置参数失败", e);
				throw new RuntimeException(e);
			}

		}
}
