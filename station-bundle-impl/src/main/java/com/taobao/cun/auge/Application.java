package com.taobao.cun.auge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.taobao.cun.diamond.AlilangDiamondConfiguration;
import com.taobao.hsf.app.spring.util.annotation.EnableHSF;

@SpringBootApplication
@EnableHSF
@EnableTransactionManagement
@EnableAspectJAutoProxy
@ImportResource("classpath*:application/application-context.xml")
@Import(AlilangDiamondConfiguration.class)
public class Application extends SpringBootServletInitializer{

    public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
    }

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}


}
