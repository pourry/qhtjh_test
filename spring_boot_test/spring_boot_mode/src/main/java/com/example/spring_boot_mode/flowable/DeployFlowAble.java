/*
package com.example.spring_boot_mode.flowable;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeployFlowAble {
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    ProcessEngine processEngine;
    @Autowired
    RuntimeService runtimeService;
    */
/*
    * 流程部署操作
    * *//*

    public void  deployFlow(String path,String name){
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(path) //读取工作流 xml
                .name(name)  //流程名
                .deploy(); //部署
        log.info(deployment.getId()+"部署完成");
    }
    */
/*
    * 流程启动
    * *//*

    public void  startFlow(String processId,String processKey){
        //1.根据流程定义ID启动流程实例
        runtimeService.startProcessInstanceById(processId);
        //2.根据流程定义key启动流程实例
        runtimeService.startProcessInstanceByKey(processKey);
    }

}
*/
