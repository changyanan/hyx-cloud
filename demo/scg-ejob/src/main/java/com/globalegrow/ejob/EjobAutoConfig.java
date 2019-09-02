package com.globalegrow.ejob;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@EnableConfigurationProperties(EjobProperties.class)
@ImportResource("classpath:/META-INF/EjobApplicationContext.xml")
public class EjobAutoConfig {
	
}
