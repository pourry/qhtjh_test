package com.example.spring_boot_mode.flowable.service.impl;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.flowable.FlowableControl;
import com.example.spring_boot_mode.flowable.dao.QuestionsFeedbackDao;
import com.example.spring_boot_mode.flowable.service.QuestionsFeedbackService;
import com.example.spring_boot_mode.utils.ResponseUtil;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class QuestionsFeedbackServiceImpl implements QuestionsFeedbackService {
    @Autowired
    TaskService taskService;
    @Autowired
    FlowableControl flowableControl;
    @Autowired
    QuestionsFeedbackDao questionsFeedbackDao;

    @Override
    public ResponseObjectEntity startFeedback(String id) {
        String processId = id;
        flowableControl.startFeedback(processId);
        return ResponseUtil.success("成功");
    }

    @Override
    public ResponseObjectEntity completeTask(String id) {
        String taskid = id;
        taskService.setAssignee(taskid,"lisi");
        taskService.complete(taskid);
        return ResponseUtil.success("成功");
    }

    @Override
    public ResponseObjectEntity suspedprocdef(String id) {
        String processId = id;
        flowableControl.suspedprocdef(processId);
        return ResponseUtil.success("成功");
    }

    @Override
    public ResponseObjectEntity ativityprocdef(String id) {
        String processId = id;
        flowableControl.ativityprocdef(processId);
        return ResponseUtil.success("成功");
    }

    @Override
    public ResponseObjectEntity suspedexecution(String id) {
        String executionid = id;
        flowableControl.suspedexecution(executionid);
        return ResponseUtil.success("成功");
    }

    @Override
    public ResponseObjectEntity ativityexecution(String id) {
        String executionid = id;
        flowableControl.ativityexecution(executionid);
        return ResponseUtil.success("成功");
    }

    @Override
    public List<Map<String, Object>> flowChart(String id) {
        List<Map<String, Object>> relist = questionsFeedbackDao.flowChart(id);



        return relist;
    }

}
