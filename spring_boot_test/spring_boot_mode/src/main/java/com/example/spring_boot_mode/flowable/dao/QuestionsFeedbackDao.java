package com.example.spring_boot_mode.flowable.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QuestionsFeedbackDao {
    List<Map<String, Object>> flowChart(String id);
}
