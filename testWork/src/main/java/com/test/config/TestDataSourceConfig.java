package com.test.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * test库配置类
 * */
@Configuration
@MapperScan(basePackages = "com.test.dao.test",sqlSessionFactoryRef = "testSqlSessionFactory")
public class TestDataSourceConfig {
	@Primary
    @Bean(name = "testDataSource")
    @ConfigurationProperties("spring.test")
    public DataSource masterDataSource(){
        return DataSourceBuilder.create().build();
    }
 
    @Bean(name = "testSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("testDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
//        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
//                .getResources("classpath:com/test/dao/test/*.java"));
        return sessionFactoryBean.getObject();
    }

    /**
     * 返回test数据库的会话模板
     * @param sessionFactory
     * @return
     * @throws Exception
     */
    @Bean(name = "testSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("testSqlSessionFactory") SqlSessionFactory sessionFactory) throws  Exception{
        return  new SqlSessionTemplate(sessionFactory);
    }

    /**
     * 返回test数据库的事务
     * @param ds
     * @return
     */
    @Bean(name = "testTransactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier("testDataSource") DataSource ds){
        return new DataSourceTransactionManager(ds);
    }


}
