package com.aliexpress.boot.mybatis;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import tk.mybatis.spring.mapper.MapperScannerConfigurer;

/**
 * mybatis auto mapper configuration
 *
 * @author leijuan
 */
@Configuration
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class MybatisMapperConfiguration implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    @ConditionalOnProperty(value = "spring.mybatis.scan-base-package", matchIfMissing = false)
    @ConditionalOnMissingBean(MapperScannerConfigurer.class)
	public MapperScannerConfigurer mapperScannerConfigurer() {
		Environment env = applicationContext.getEnvironment();
		String scanBasePackage = env.getProperty("spring.mybatis.scan-base-package");
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
		mapperScannerConfigurer.setBasePackage(scanBasePackage);
		Properties properties = new Properties();
		properties.setProperty("notEmpty", "false");
		properties.setProperty("IDENTITY", "MYSQL");
		mapperScannerConfigurer.setProperties(properties);
		return mapperScannerConfigurer;
	}
}