package com.milton.spring.firstwebapp.dbsource;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.milton.spring.firstwebapp.log.AppContextRefreshedEventPropertiesPrinter;

//@Configuration
public class DataSourceConfig {
  private static final Logger LOGGER = LoggerFactory
      .getLogger(AppContextRefreshedEventPropertiesPrinter.class);

  @Value("${DB_PW:default_value}")
  private String dbPassword;
  
  @Bean
  public DataSource getDataSource() {
    DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
    dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
    dataSourceBuilder.url("jdbc:mysql://db4free.net:3306/miltondb");
    dataSourceBuilder.username("miltonchang");
    dataSourceBuilder.password(dbPassword);
    
    LOGGER.info("DB_PW: " + dbPassword);
    return dataSourceBuilder.build();
  }
}
