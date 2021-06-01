package com.test;

import com.spring4all.swagger.EnableSwagger2Doc;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

//不是用springboot自带的数据源和mybatis配置
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, MybatisAutoConfiguration.class
        /*,DataSourceTransactionManagerAutoConfiguration.class*/})
@EnableSwagger2Doc
@EnableScheduling
//@EnableOAuth2Sso //开启权限
@MapperScan("com.test.dao")
@EnableAsync
//配置加密注解
@EnableEncryptableProperties
public class TestApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
