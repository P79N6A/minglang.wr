package com.taobao.cun.crius;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

import com.taobao.hsf.app.spring.util.annotation.EnableHSFProvider;

@SpringBootApplication
@EnableHSFProvider
public class Application extends SpringBootServletInitializer {


    public static void main(String[] args) {
    	SpringApplication application = new SpringApplication(Application.class);
    	application.run(args);
    }

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}


}
