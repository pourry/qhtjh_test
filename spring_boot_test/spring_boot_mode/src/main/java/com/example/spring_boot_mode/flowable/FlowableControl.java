package com.example.spring_boot_mode.flowable;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
@Component
public class FlowableControl {
    @Autowired
    ProcessEngine processEngine;
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;
    //流程部署
    public void deployFlow(){
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("flowable\\问题反馈.bpmn20.xml")
                .name("问题反馈流程图")
                .deploy();//部署方法
    }

    //创建流程实例act_ru_execution  中根据act_re_procdef new 一个流程实例
    public void startFeedback(String id) {
        String processId = id;
        runtimeService.startProcessInstanceById(processId);
    }
    //挂起流程定义act_re_procdef
    public void suspedprocdef(String id) {
        String processId = id;
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processId)
                .singleResult();
        boolean suspended = processDefinition.isSuspended();
        if (!suspended){
            repositoryService.suspendProcessDefinitionById(processId);
        }

    }

    //激活流程定义act_re_procdef
    public void ativityprocdef(String id) {
        String processId = id;
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processId)
                .singleResult();
        boolean suspended = processDefinition.isSuspended();
        if (suspended){
            repositoryService.activateProcessDefinitionById(processId);
        }
    }

    //挂起流程实例act_ru_execution
    public void suspedexecution(String executionid) {
        runtimeService.suspendProcessInstanceById(executionid);
    }
    //激活流程实例act_ru_execution
    public void ativityexecution(String executionid) {
    }

    //任务审批
    public void completeTask(String taskstaff,String id) {
        String taskid = id;
        //给节点添加 使用人
        if (taskstaff != null){
            taskService.setAssignee(taskid,taskstaff);
        }

        taskService.complete(taskid);
    }
    //通过 流程定义id 查询流程png图片act_re_procdef
    public void getImageActReProcdefId(String actReProcdefid, HttpServletResponse response){
        InputStream resourecAsStream = null;
        OutputStream outputStream = null;
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(actReProcdefid)
                    .singleResult();
            String resourecName = "";
            resourecName = processDefinition.getDiagramResourceName();
            resourecAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),resourecName)
            outputStream =response.getOutputStream();
            byte[] b = new byte[1024];
            int len = -1;
            while ((len = resourecAsStream.read(b, 0, 1024)) != -1) {
                outputStream.write(b, 0, len);
            }
            outputStream.flush();
        }catch (IOException ioException) {
            log.error(ioException.getMessage());
        }finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                }catch (IOException ioException) {
                    log.error(ioException.getMessage());
                }
            }
            if (resourecAsStream != null) {
                try {
                    resourecAsStream.close();
                }catch (IOException ioException) {
                    log.error(ioException.getMessage());
                }
            }
        }
    }
}
