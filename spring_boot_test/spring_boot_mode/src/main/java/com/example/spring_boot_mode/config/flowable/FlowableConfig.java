package com.example.spring_boot_mode.config.flowable;

import org.flowable.engine.*;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Flowable 手动配置类
 * 
 * 由于 Flowable 6.7.2 不支持 flowable.datasource.* 独立数据源配置，
 * 这里手动指定 flowableDataSource，确保 Flowable 只在 flowable 数据库中建表。
 */
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
    public SpringProcessEngineConfiguration processEngineConfiguration() {
        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();

        // 关键：使用 flowableDataSource，确保只在 flowable 数据库中创建表
        config.setDataSource(flowableDataSource);
        config.setTransactionManager(flowableTransactionManager);

        // 解决流程图片中文乱码问题
        config.setActivityFontName("宋体");
        config.setLabelFontName("宋体");
        config.setAnnotationFontName("宋体");

        // 数据库配置 - 自动创建/更新表结构
        config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        // 关闭定时任务
        config.setAsyncExecutorActivate(false);

        // 历史记录配置
        config.setDbHistoryUsed(true);
        config.setHistory("full");

        return config;
    }

    @Bean
    public ProcessEngine processEngine() {
        return processEngineConfiguration().buildProcessEngine();
    }

    // ========= 手动创建 Flowable Service Bean，替代自动配置 =========

    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    @Bean
    public FormService formService(ProcessEngine processEngine) {
        return processEngine.getFormService();
    }

    @Bean
    public DynamicBpmnService dynamicBpmnService(ProcessEngine processEngine) {
        return processEngine.getDynamicBpmnService();
    }

}