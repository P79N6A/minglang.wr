package com.taobao.cun.auge.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TransactionManagementConfigurer implements org.springframework.transaction.annotation.TransactionManagementConfigurer{

	@Autowired
	private DataSource dataSource;
	
	@Bean
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		 return new DataSourceTransactionManager(dataSource);
	}


}
