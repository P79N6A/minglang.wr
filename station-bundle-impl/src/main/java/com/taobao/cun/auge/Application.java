package com.taobao.cun.auge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.taobao.hsf.app.spring.util.annotation.EnableHSFProvider;

@SpringBootApplication
@EnableHSFProvider
@EnableTransactionManagement
//@EnableIntegration
//@IntegrationComponentScan("com.taobao.cun")
@ImportResource("classpath*:application/application-context.xml")
public class Application extends SpringBootServletInitializer{

    public static void main(String[] args) {
    	ConfigurableApplicationContext ctx =
				SpringApplication.run(Application.class, args);
    	
    }

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}


}
