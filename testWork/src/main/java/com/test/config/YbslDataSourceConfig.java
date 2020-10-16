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
 * ysbl 库配置类
 * @author
 * */
@Configuration
@MapperScan(basePackages = "com.test.dao.ybsl",sqlSessionFactoryRef = "ybslSqlSessionFactory")
public class YbslDataSourceConfig {
	@Primary
    @Bean(name = "ybslDataSource")
    @ConfigurationProperties("spring.ybsl")
    public DataSource masterDataSource(){
        return DataSourceBuilder.create().build();
    }
 
    @Bean(name = "ybslSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("ybslDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:com/test/dao/ybsl/*.java"));
        return sessionFactoryBean.getObject();
    }

    /**
     * 返回 ybsl 数据库的会话模板
     * @param sessionFactory
     * @return
     * @throws Exception
     */
    @Bean(name = "ybslSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("ybslSqlSessionFactory") SqlSessionFactory sessionFactory) throws  Exception{
        return  new SqlSessionTemplate(sessionFactory);
    }

    /**
     * 返回test数据库的事务
     * @param ds
     * @return
     */
    @Bean(name = "ybslTransactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier("ybslDataSource") DataSource ds){
        return new DataSourceTransactionManager(ds);
    }

}
