package com.example.spring_boot_mode.flowable.service;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;

import java.util.List;
import java.util.Map;

public interface QuestionsFeedbackService {

    ResponseObjectEntity startFeedback(String id);

    ResponseObjectEntity completeTask(String id);

    ResponseObjectEntity suspedprocdef(String id);

    ResponseObjectEntity ativityprocdef(String id);

    ResponseObjectEntity suspedexecution(String id);

    ResponseObjectEntity ativityexecution(String id);

    List<Map<String, Object>> flowChart(String id);
}
