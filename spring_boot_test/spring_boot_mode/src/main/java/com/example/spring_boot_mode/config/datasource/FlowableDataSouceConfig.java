package com.example.spring_boot_mode.config.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
@Configuration
@MapperScan(basePackages = "com.example.spring_boot_mode.flowable.dao",
            sqlSessionFactoryRef = "flowableSqlSessionFactory")
public class FlowableDataSouceConfig {


    // mapper.xml所在地址
    private static final String MAPPER_LOCATION = "classpath*:mapper/flowable/*.xml";

    /**
     * 数据源
     */
    /**
     * 主数据源，Primary注解必须增加，它表示该数据源为默认数据源
     * 项目中还可能存在其他的数据源，如获取时不指定名称，则默认获取这个数据源，如果不添加，则启动时候回报错
     */
    @Primary
    @Bean(name = "flowableDataSource")
    // 读取spring.datasource.flowable前缀的配置文件映射成对应的配置对象
    @ConfigurationProperties(prefix = "spring.datasource.flowable")
    public DataSource dataSource() {
        DataSource build = DataSourceBuilder.create().build();
        return build;
    }


    /**
     * 事务管理器
     */
    @Bean(name = "flowableTransactionManager")
    public PlatformTransactionManager dataSourceTransactionManager(@Qualifier("flowableDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * session工厂
     */

    @Bean(name = "flowableSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("flowableDataSource") DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(FlowableDataSouceConfig.MAPPER_LOCATION));
        return sessionFactoryBean.getObject();
    }
}
