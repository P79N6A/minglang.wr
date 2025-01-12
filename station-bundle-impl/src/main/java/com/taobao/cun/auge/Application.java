package com.taobao.cun.auge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.taobao.cun.diamond.AugeDiamondListener;
import com.taobao.hsf.app.spring.util.annotation.EnableHSF;

@SpringBootApplication
@EnableHSF
@EnableTransactionManagement
@EnableAspectJAutoProxy
@ImportResource({"classpath*:application/application-context.xml","classpath*:applicationContext-statemachine.xml"})
@Import(AugeDiamondListener.class)
public class Application{

    public static void main(String[] args) {
    	System.setProperty("kfc.enableSlsLog", "false");
		SpringApplication.run(Application.class, args);
    }

}
