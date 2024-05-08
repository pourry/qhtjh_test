package com.example.spring_boot_mode.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LoginDao {
    List<Map<String, String>> getalltest();
}
