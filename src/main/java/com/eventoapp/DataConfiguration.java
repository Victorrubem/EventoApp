package com.eventoapp;


import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;


@Configuration
public class DataConfiguration {
	
	@Bean
	public DataSource dataSource() {
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://www.db4free.net:3306/eventoapp?useTimezone=true&serverTimezone=UTC");//?createDatabaseIfNotExist=true&useTimezone=true&serverTimezone=UTC
		dataSource.setUsername("victorrubem");
		dataSource.setPassword("Radix4512");
		Properties connectionProperties = new Properties();
		connectionProperties.setProperty("spring.jpa.hibernate.ddl-auto", "update");
		dataSource.setConnectionProperties(connectionProperties);
		//dataSource.setSchema("eventoapp");
		
	
		
		return dataSource;
	}
	
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.MYSQL);
		adapter.setShowSql(true);
		adapter.setGenerateDdl(true);
		adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
		adapter.setPrepareConnection(true);
		
		return adapter;
	}
	
}
