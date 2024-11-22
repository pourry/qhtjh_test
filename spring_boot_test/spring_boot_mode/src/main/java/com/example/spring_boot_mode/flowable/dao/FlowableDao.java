package com.example.spring_boot_mode.flowable.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.spring_boot_mode.flowable.entity.Flowable;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface FlowableDao extends BaseMapper<Flowable> {

    List<Object> testFlowAble();
}
