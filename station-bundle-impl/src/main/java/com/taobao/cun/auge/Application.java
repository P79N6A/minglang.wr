package com.taobao.cun.auge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.taobao.cun.auge.common.MultipleConsumersStreams;
import com.taobao.hsf.app.spring.util.annotation.EnableHSF;

@SpringBootApplication
@EnableHSF
@EnableTransactionManagement
@EnableAspectJAutoProxy
//@EnableBinding({Source.class, MultipleConsumersStreams.class})
@ImportResource("classpath*:application/application-context.xml")
public class Application{

    public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
    }



}
