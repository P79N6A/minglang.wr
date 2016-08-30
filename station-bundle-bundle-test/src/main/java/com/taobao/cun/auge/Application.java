package com.taobao.cun.auge;

import com.taobao.hsf.app.spring.util.annotation.EnableHSF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@SpringBootApplication
@EnableTransactionManagement
@EnableHSF
public class Application extends SpringBootServletInitializer{

	@Autowired
	private DataSource dataSource;
	
    public static void main(String[] args) {
    	SpringApplication application = new SpringApplication(Application.class);
    	application.run(args);
    }

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}



}
