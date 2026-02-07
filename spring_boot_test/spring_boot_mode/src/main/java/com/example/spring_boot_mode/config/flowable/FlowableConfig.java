package com.example.spring_boot_mode.config.flowable;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class FlowableConfig {

    private final DataSource flowableDataSource;
    private final PlatformTransactionManager flowableTransactionManager;

    public FlowableConfig(@Qualifier("flowableDataSource") DataSource flowableDataSource,
                          @Qualifier("flowableTransactionManager") PlatformTransactionManager flowableTransactionManager) {
        this.flowableDataSource = flowableDataSource;
        this.flowableTransactionManager = flowableTransactionManager;
    }

    @Bean
    @Primary
    public SpringProcessEngineConfiguration processEngineConfiguration() {
        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();

        // 设置数据源
        config.setDataSource(flowableDataSource);
        config.setTransactionManager(flowableTransactionManager);

        // 解决流程图片中文乱码问题
        config.setActivityFontName("宋体");
        config.setLabelFontName("宋体");
        config.setAnnotationFontName("宋体");

        // 数据库配置
        config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        config.setAsyncExecutorActivate(false);
        config.setDbHistoryUsed(true);

        return config;
    }

    @Bean
    @Primary
    public ProcessEngine processEngine() {
        return processEngineConfiguration().buildProcessEngine();
    }
}