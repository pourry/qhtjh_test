package com.example.spring_boot_mode.dao.flowable;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.spring_boot_mode.entity.flowable.Flowable;
import org.mapstruct.Mapper;

@Mapper
public interface FlowableDao extends BaseMapper<Flowable> {

}
