package com.example.spring_boot_mode.flowable.web;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.flowable.FlowableControl;
import com.example.spring_boot_mode.flowable.service.QuestionsFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/questionsFeedback")
//问题反馈
public class QuestionsFeedbackController {
    @Autowired
    QuestionsFeedbackService questionsFeedbackService;
    @Autowired
    FlowableControl flowableControl;
    //部署
    @GetMapping("deploy")
    public void deploy(){
        flowableControl.deployFlow();
    }
    //创建流程实例
    @GetMapping("startFeedback")
    public ResponseObjectEntity startFeedback(String id,HttpServletRequest request){
//        SysUser sysUser = TokenUtill.getSysUser(request);
//        if (Objects.isNull(sysUser)){
//            return ResponseUtil.tokenExpire("token失效，请重新登录");
//        }
        ResponseObjectEntity responseObjectEntity = questionsFeedbackService.startFeedback(id);
        return responseObjectEntity;
    }
    //挂起流程定义act_re_procdef
    @GetMapping("suspedprocdef")
    public ResponseObjectEntity suspedprocdef(String id){
        ResponseObjectEntity responseObjectEntity = questionsFeedbackService.suspedprocdef(id);
        return responseObjectEntity;
    }
    //激活流程定义act_re_procdef
    @GetMapping("ativityprocdef")
    public ResponseObjectEntity ativityprocdef(String id){
        ResponseObjectEntity responseObjectEntity = questionsFeedbackService.ativityprocdef(id);
        return responseObjectEntity;
    }
    //挂起流程实例act_ru_execution
    @GetMapping("suspedexecution")
    public ResponseObjectEntity suspedexecution(String id){
        ResponseObjectEntity responseObjectEntity = questionsFeedbackService.suspedexecution(id);
        return responseObjectEntity;
    }
    //激活流程实例act_ru_execution
    @GetMapping("ativityexecution")
    public ResponseObjectEntity ativityexecution(String id){
        ResponseObjectEntity responseObjectEntity = questionsFeedbackService.ativityexecution(id);
        return responseObjectEntity;
    }
    //任务审批
    @GetMapping("/completeTask")
    public ResponseObjectEntity completeTask(String id,HttpServletRequest request){
        //        SysUser sysUser = TokenUtill.getSysUser(request);
//        if (Objects.isNull(sysUser)){
//            return ResponseUtil.tokenExpire("token失效，请重新登录");
//        }
        ResponseObjectEntity responseObjectEntity = questionsFeedbackService.completeTask(id);
        return responseObjectEntity;
    }
    //查询流程图  通过查询数据库中blob返回png图片act_re_deployment 部署id
    @GetMapping("/flowChart")
    public void flowChart(String id, HttpServletRequest request, HttpServletResponse response){
        List<Map<String, Object>> relist = questionsFeedbackService.flowChart(id);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=test.png" );
        relist.forEach(item->{
            if(item.get("png") != null){
                byte[] imageData = (byte[]) item.get("png");
                //直接组装成 string返回给前端
                //前端 通过直接scr接即可
//                String base64Image = Base64.getEncoder().encodeToString(imageData);
//                String imgSrc = "data:image/jpeg;base64,"+base64Image;
//                item.put("png",imgSrc);
                InputStream inputStream = null;
                OutputStream outputStream =null;
                try {
                    inputStream = new ByteArrayInputStream(imageData);
                    outputStream= response.getOutputStream();
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer))!= -1){
                        outputStream.write(buffer,0,bytesRead);
                    }
                    outputStream.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        });
//        return responseObjectEntity;
    }
    //通过 流程定义id 查询流程png图片act_re_procdef
    public void getImageActReProcdefId(String actReProcdefid ,HttpServletResponse response){
        flowableControl.getImageActReProcdefId(actReProcdefid,response);
    }

}
