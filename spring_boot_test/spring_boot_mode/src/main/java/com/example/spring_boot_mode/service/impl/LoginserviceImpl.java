package com.example.spring_boot_mode.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.example.spring_boot_mode.dao.flowable.FlowableDao;
import com.example.spring_boot_mode.dao.mode.LoginDao;
import com.example.spring_boot_mode.entity.flowable.Flowable;
import com.example.spring_boot_mode.entity.mode.SysUser;
import com.example.spring_boot_mode.service.Loginservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@DS("mode")
@Service
public class LoginserviceImpl implements Loginservice {
    @Autowired
    private LoginDao loginDao;
    @Autowired
    private FlowableDao flowableDao;

    @Override
    public List<SysUser> getSysUser() {
        return loginDao.selectList(null);
    }

    @DS("flowable")
    @Override
    public List<Flowable> getflowable() {
        return flowableDao.selectList(null);
    }
}
