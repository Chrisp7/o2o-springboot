package com.imooc.o2o.config.service;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

@Configuration
@EnableTransactionManagement
public class TransactionManagementConfig implements TransactionManagementConfigurer {

	@Autowired
	private DataSource dataSource;

	@Override
	/**
	 * 关于事物管理，需要返回PlatformTransactionManager的实现
	 * 这和之前spring-dao里面的配置transactionManager是一样的
	 */
	public TransactionManager annotationDrivenTransactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}

}
