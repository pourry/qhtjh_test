package com.example.spring_boot_mode.listener;

import com.example.spring_boot_mode.flowable.FlowableControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyApplicationContextInitializer implements ApplicationRunner {

    @Autowired
    FlowableControl flowableDeploy;
    @Override
    public void run(ApplicationArguments args) throws Exception {
//        flowableDeploy.deployFlow();
//        System.out.println("工作流实例部署完成");
        System.out.println("通过实现ApplicationRunner接口，在spring boot项目启动后打印参数");
    }
}
