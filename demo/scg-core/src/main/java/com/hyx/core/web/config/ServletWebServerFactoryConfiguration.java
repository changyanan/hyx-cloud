package com.hyx.core.web.config;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.ProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.embedded.jetty.JettyWebServer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.util.ReflectionUtils;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.ConnectorStatistics;
import io.undertow.server.handlers.GracefulShutdownHandler;

@Configuration
class ServletWebServerFactoryConfiguration   {

	private static final Logger log = LoggerFactory.getLogger(ServletWebServerFactoryConfiguration.class);

	private static final int waitTime=10;
	
	private static void stop( ApplicationContext applicationContext) {
		try {
			log.info("开始取消Eureka注册的服务");
			Class<?> claz = Class.forName("com.netflix.discovery.EurekaClient");
			Method method = ReflectionUtils.findMethod(claz, "shutdown");
			applicationContext.getBeansOfType(claz).values().forEach(eurekaClient->{
				try {
					method.invoke(eurekaClient);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					log.info("关闭注册服务失败",e);
				}
			}); 
			Thread.sleep(10*1000);
			log.info("取消Eureka注册的服务完毕");
		} catch (Exception e) {
			log.info("取消Eureka注册的服务失败  ",e );
		}
	}
	
	@Configuration
	@ConditionalOnClass(name={ "javax.servlet.Servlet", "org.apache.catalina.startup.Tomcat", "org.apache.coyote.UpgradeProtocol" })
//	@ConditionalOnMissingBean(value = ServletWebServerFactory.class, search = SearchStrategy.CURRENT)
	public static class EmbeddedTomcat implements  ApplicationListener<ContextClosedEvent>{
		private Connector connector;
		
		@Bean
		public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
			log.info("使用tomcat启动服务");
			TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
	        tomcat.addConnectorCustomizers(connector-> this.connector=connector);
	        return tomcat;
		}
		
		@Override
		public void onApplicationEvent(ContextClosedEvent event) {
			log.info("服务开始关闭");
			ApplicationContext context= event.getApplicationContext();
			stop(context);
			if(this.connector==null) {
				log.warn("无法完成服务的优雅关机。。。");
				return;
			}
			ProtocolHandler protocolHandler = this.connector.getProtocolHandler();
			if(protocolHandler==null) {
				log.warn("无法完成服务的优雅关机。。。");
				return;
			}
	        Executor executor= protocolHandler.getExecutor();
			if(executor==null) {
				log.warn("无法完成服务的优雅关机。。。");
				return;
			}
	        if (executor instanceof ExecutorService) {
	        	ExecutorService executorService = (ExecutorService) executor;
	        	executorService.shutdown();
	            try {
					if (!executorService.awaitTermination(waitTime, TimeUnit.MINUTES)) {
	            		throw new Exception("优雅关机失败，等待时间超过 "+waitTime+"分钟");
					}
					log.info("服务关闭完成！！");
				} catch (InterruptedException e) {
					log.info("关闭服务收到中断信息",e);
					Thread.currentThread().interrupt();
				}catch (Exception e){
		            log.warn("tomcat优雅关机失败", e);
		        }
	        }

		}

	}

	@Configuration
	@ConditionalOnClass(name={ "javax.servlet.Servlet", "org.eclipse.jetty.server.Server", "org.eclipse.jetty.util.Loader","org.eclipse.jetty.webapp.WebAppContext" })
//	@ConditionalOnMissingBean(value = ServletWebServerFactory.class, search = SearchStrategy.CURRENT)
	public static class EmbeddedJetty implements  ApplicationListener<ContextClosedEvent>{

		@Bean
		public JettyServletWebServerFactory JettyServletWebServerFactory() {
			log.info("使用jetty启动服务");
			return new JettyServletWebServerFactory();
		}

		@Override
		public void onApplicationEvent(ContextClosedEvent event) {
			log.info("服务开始关闭");
			ApplicationContext context= event.getApplicationContext();
			stop(context);
			JettyWebServer webServer = (JettyWebServer)((ServletWebServerApplicationContext)context).getWebServer();
			webServer.stop();
			log.info("服务关闭完成！！");
		}

	}

	@Configuration
	@ConditionalOnClass(name={ "javax.servlet.Servlet", "io.undertow.Undertow", "org.xnio.SslClientAuthMode" })
//	@ConditionalOnMissingBean(value = ServletWebServerFactory.class, search = SearchStrategy.CURRENT)
	public static class EmbeddedUndertow implements ApplicationListener<ContextClosedEvent> {
		
		private GracefulShutdownHandler gracefulShutdownHandler;
	    
		@Bean
		public UndertowServletWebServerFactory undertowServletWebServerFactory() {
			log.info("使用undertow启动服务");
			UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
	        factory.addDeploymentInfoCustomizers(deploymentInfo -> deploymentInfo.addOuterHandlerChainWrapper(handler->this.gracefulShutdownHandler = new GracefulShutdownHandler(handler)));
	        factory.addBuilderCustomizers(builder -> builder.setServerOption(UndertowOptions.ENABLE_STATISTICS, true));
	        return factory;
		}
		
	    @Override
	    public void onApplicationEvent(ContextClosedEvent event){
	        try {
				log.info("服务开始关闭");
				ApplicationContext context= event.getApplicationContext();
				stop(context);
				if(gracefulShutdownHandler==null) {
					log.warn("无法完成服务的优雅关机。。。");
					return;
				}
	        	gracefulShutdownHandler.shutdown();
	            UndertowServletWebServer webServer = (UndertowServletWebServer)((ServletWebServerApplicationContext)context).getWebServer();
	            Field field =ReflectionUtils.findField(webServer.getClass(), "undertow") ;
	            field.setAccessible(true);
	            Undertow undertow =  (Undertow) ReflectionUtils.getField(field, webServer);
	            List<Undertow.ListenerInfo> listenerInfo = undertow.getListenerInfo();
	            Undertow.ListenerInfo listener = listenerInfo.get(0);
	            ConnectorStatistics connectorStatistics = listener.getConnectorStatistics();
	            long start=System.currentTimeMillis();
	            while (connectorStatistics!=null&&connectorStatistics.getActiveConnections() > 0){
	            	if(System.currentTimeMillis()-start>TimeUnit.MINUTES.toMillis(waitTime)) {
	            		throw new Exception("优雅关机失败，等待时间超过 "+waitTime+"分钟");
	            	}
	            	Thread.sleep(10);
	            }
				log.info("服务关闭完成！！");
	        }catch (InterruptedException e) {
				log.info("服务关闭发生中断",e);
				Thread.currentThread().interrupt();
			}catch (Exception e){
	            log.warn("undertow优雅关机失败", e);
	        }
	    }

	}

}