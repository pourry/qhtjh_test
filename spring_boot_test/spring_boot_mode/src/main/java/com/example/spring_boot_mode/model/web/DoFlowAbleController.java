package com.example.spring_boot_mode.model.web;


import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.flowable.service.DeployFlowAble;
import com.example.spring_boot_mode.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/doflowable")
public class DoFlowAbleController {

    @Autowired
    DeployFlowAble deployFlowAble;

    @GetMapping("/public/testFlowAble")
    public ResponseObjectEntity testFlowAble(){
       List<Object> objects =  deployFlowAble.testFlowAble();
        return ResponseUtil.success(objects);
    }

//    @GetMapping("deployFlow")
//    public ResponseObjectEntity deployFlow(){
//        deployFlowAble.deployFlow("flowable\\test01.bpmn20.xml","test01");
//        return ResponseUtil.success("部署成功");
//    }
//    @GetMapping("startFlow")
//    public ResponseObjectEntity startFlow(){
//        deployFlowAble.startFlow("test01:1:349fddb8-2d15-11ef-acb3-c8215847b729",null);
//        return ResponseUtil.success("启动成功");
//    }
//    @GetMapping("findFlow")
//    public ResponseObjectEntity findFlow(){
//        List<Task> list = deployFlowAble.finFlow();
//        return ResponseUtil.success(list);
//    }
}
