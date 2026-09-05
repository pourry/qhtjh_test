package com.example.spring_boot_mode.config.datasource;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
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
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = "com.example.spring_boot_mode.model.dao",
        sqlSessionFactoryRef = "modeSqlSessionFactory")
public class ModeDataSouceConfig {


    // mapper.xml所在地址
    private static final String MAPPER_LOCATION = "classpath*:mapper/**/*.xml";

    /**
     * 主数据源，Primary注解必须增加，它表示该数据源为默认数据源
     * 项目中还可能存在其他的数据源，如获取时不指定名称，则默认获取这个数据源，如果不添加，则启动时候回报错
     */
    @Bean(name = "modeDataSource")
    @Primary
    // 读取spring.datasource.mode前缀的配置文件映射成对应的配置对象
    @ConfigurationProperties(prefix = "spring.datasource.mode")
    public DataSource dataSource() {
        DataSource build = DataSourceBuilder.create().build();
        return build;
    }

    /**
     * 事务管理器，Primary注解作用同上
     */
    @Bean(name = "modeTransactionManager")
    @Primary
    public PlatformTransactionManager dataSourceTransactionManager(@Qualifier("modeDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * session工厂，Primary注解作用同上
     */

    @Bean(name = "modeSqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("modeDataSource") DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(ModeDataSouceConfig.MAPPER_LOCATION));

        // 显式设置 MyBatis Configuration（双数据源模式下 application.yml 里的 mybatis-plus.configuration 不会自动生效）
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setLogImpl(org.apache.ibatis.logging.slf4j.Slf4jImpl.class);
        // 允许驼峰命名
        Properties props = new Properties();
        props.setProperty("mapUnderscoreToCamelCase", "true");
        configuration.setVariables(props);
        sessionFactoryBean.setConfiguration(configuration);

        // 注入 MyBatis-Plus 拦截器（包含分页）
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // MySQL 分页拦截器
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(com.baomidou.mybatisplus.annotation.DbType.MYSQL));
        sessionFactoryBean.setPlugins(interceptor);

        return sessionFactoryBean.getObject();
    }
}
