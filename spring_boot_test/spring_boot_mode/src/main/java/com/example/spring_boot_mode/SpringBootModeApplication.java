package com.example.spring_boot_mode;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot .SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@Slf4j


@MapperScan("com/example/spring_boot_mode/dao")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
//@EnableSwagger2
public class SpringBootModeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootModeApplication.class, args);
        System.out.println("启动成功");
    }

}
