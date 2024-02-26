package com.example.spring_boot_mode.config.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
@Configuration
@MapperScan(basePackages = "com.example.spring_boot_mode.flowable",
            sqlSessionFactoryRef = "flowableSqlSessionFactory")
public class FlowableConfig {

        @Bean(name = "flowableDataSource")
        @ConfigurationProperties(prefix = "spring.datasource.flowable")
        public DataSource getDateSource2()
        {
            return DataSourceBuilder.create().build();
        }

        @Bean(name = "flowableSqlSessionFactory")
        public SqlSessionFactory flowableSqlSessionFactory(@Qualifier("flowableDataSource") DataSource datasource)
                throws Exception
        {
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(datasource);
            bean.setMapperLocations(
                    new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/flowable/*.xml"));
            return bean.getObject();
        }

        @Bean("flowableSqlSessionTemplate")
        public SqlSessionTemplate flowableSqlSessionTemplate(
                @Qualifier("flowableSqlSessionFactory") SqlSessionFactory sessionFactory)
        {
            return new SqlSessionTemplate(sessionFactory);
        }
}
