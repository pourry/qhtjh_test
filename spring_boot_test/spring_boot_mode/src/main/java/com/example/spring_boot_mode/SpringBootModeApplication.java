package com.example.spring_boot_mode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j


@MapperScan("com.example.spring_boot_mode.model.dao")
@SpringBootApplication(exclude = {
        SecurityAutoConfiguration.class,
        // 排除 Flowable 自动配置，使用自定义的 FlowableConfig
        // Flowable 6.7.2 自动配置会使用 @Primary 的 modeDataSource（连接 test_all）建表
        org.flowable.spring.boot.ProcessEngineAutoConfiguration.class,
        org.flowable.spring.boot.ProcessEngineServicesAutoConfiguration.class
})
//@EnableSwagger2
public class SpringBootModeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootModeApplication.class, args);
        System.out.println("启动成功");
        System.out.println("java.version:"+ System.getProperty("java.version"));
        System.out.println("java.vendor:"+ System.getProperty("java.vendor"));
        System.out.println("java.home:"+ System.getProperty("java.home"));
    }

}
