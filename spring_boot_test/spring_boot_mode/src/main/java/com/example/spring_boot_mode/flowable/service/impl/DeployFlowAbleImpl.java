package com.example.spring_boot_mode.flowable.service.impl;

import com.example.spring_boot_mode.flowable.dao.FlowableDao;
import com.example.spring_boot_mode.flowable.service.DeployFlowAble;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeployFlowAbleImpl implements DeployFlowAble {

    @Autowired
    FlowableDao flowableDao;

    @Override
    public List<Object> testFlowAble() {
        return flowableDao.testFlowAble();
    }
}
