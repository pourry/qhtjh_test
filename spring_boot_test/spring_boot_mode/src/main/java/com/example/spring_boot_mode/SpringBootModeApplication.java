package com.example.spring_boot_mode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot .SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@MapperScan("com/example/spring_boot_mode/dao")
@SpringBootApplication
//@EnableSwagger2
public class SpringBootModeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootModeApplication.class, args);
        System.out.println("启动成功");
    }

}
