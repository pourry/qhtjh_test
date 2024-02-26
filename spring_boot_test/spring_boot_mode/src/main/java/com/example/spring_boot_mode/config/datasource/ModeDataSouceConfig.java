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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.example.spring_boot_mode.dao",
        sqlSessionFactoryRef = "modeSqlSessionFactory")
public class ModeDataSouceConfig {

        // 将这个对象放入Spring容器中
        @Bean(name = "modeDataSource")
        // 表示这个数据源是默认数据源
        @Primary
        // 读取application.properties中的配置参数映射成为一个对象
        // prefix表示参数的前缀
        @ConfigurationProperties(prefix = "spring.datasource.mode")
        public DataSource getDateSource1()
        {
            return DataSourceBuilder.create().build();
        }

        @Bean(name = "modeSqlSessionFactory")
        // 表示这个数据源是默认数据源
        @Primary
        // @Qualifier表示查找Spring容器中名字为test1DataSource的对象
        public SqlSessionFactory modeSqlSessionFactory(@Qualifier("modeDataSource") DataSource datasource)
                throws Exception
        {
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(datasource);
            bean.setMapperLocations(
                    // 设置mybatis的xml所在位置
                    new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/mode/*.xml"));
            return bean.getObject();
        }

        @Bean("modeSqlSessionTemplate")
        // 表示这个数据源是默认数据源
        @Primary
        public SqlSessionTemplate modeSqlSessionTemplate(
                @Qualifier("modeSqlSessionFactory") SqlSessionFactory sessionFactory)
        {
            return new SqlSessionTemplate(sessionFactory);
        }
}
