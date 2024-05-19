package com.example.spring_boot_mode.listener;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component

public class StartApplication {

    //       @EventListener(classes = {ContextRefreshedEvent.class})
    @PostConstruct
    void boothasstarted(){
        System.out.println("项目启动执行的代码-----PostConstruct");
    };
    @EventListener(classes = {ContextRefreshedEvent.class})
    void boothasstarted2(){
        System.out.println("项目启动执行的代码-----EventListener");
    };

}
