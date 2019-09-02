package com.hyx.core.utils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

public class AnnotatedTypeScanner implements ResourceLoaderAware, EnvironmentAware {

	private final Iterable<Class<? extends Annotation>> annotationTypess;
	private final boolean considerInterfaces;

	private ResourceLoader resourceLoader;
	private Environment environment;

	@SafeVarargs
	public AnnotatedTypeScanner(Class<? extends Annotation>... annotationTypes) {
		this(true, annotationTypes);
	}

	@SafeVarargs
	public AnnotatedTypeScanner(boolean considerInterfaces, Class<? extends Annotation>... annotationTypes) {
		this.annotationTypess = Arrays.asList(annotationTypes);
		this.considerInterfaces = considerInterfaces;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public Set<Class<?>> findTypes(String... basePackages) {
		return findTypes(Arrays.asList(basePackages));
	}

	public Set<Class<?>> findTypes(Iterable<String> basePackages) {
		InterfaceAwareScanner provider = new InterfaceAwareScanner(considerInterfaces);
		if (resourceLoader != null) {
			provider.setResourceLoader(resourceLoader);
		}
		if (environment != null) {
			provider.setEnvironment(environment);
		} 
		for (Class<? extends Annotation> annotationType : annotationTypess) {
			provider.addIncludeFilter(new AnnotationTypeFilter(annotationType, true, considerInterfaces));
		}

		Set<Class<?>> types = new HashSet<Class<?>>();
		for (String basePackage : basePackages) {
			for (BeanDefinition definition : provider.findCandidateComponents(basePackage)) {
				try { 
					types.add(ClassUtils.forName(definition.getBeanClassName(), resourceLoader == null ? null : resourceLoader.getClassLoader()));
				} catch (ClassNotFoundException o_O) {
					throw new IllegalStateException(o_O);
				}
			}
		}

		return types;
	}

	private static class InterfaceAwareScanner extends ClassPathScanningCandidateComponentProvider {

		private final boolean considerInterfaces;

		public InterfaceAwareScanner(boolean considerInterfaces) {
			super(false);
			this.considerInterfaces = considerInterfaces;
		}

		@Override
		protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
			return super.isCandidateComponent(beanDefinition) || considerInterfaces && beanDefinition.getMetadata().isInterface();
		}
	}
}
