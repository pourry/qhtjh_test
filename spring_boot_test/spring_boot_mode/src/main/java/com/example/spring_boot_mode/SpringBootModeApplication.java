package com.example.spring_boot_mode;

import com.example.spring_boot_mode.config.datasource.FlowableDataSouceConfig;
import org.flowable.spring.boot.FlowableAutoDeploymentProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure .SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j


@MapperScan("com.example.spring_boot_mode.model.dao")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
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
