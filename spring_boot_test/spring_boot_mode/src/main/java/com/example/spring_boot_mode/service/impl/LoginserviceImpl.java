package com.example.spring_boot_mode.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.spring_boot_mode.dao.flowable.FlowableDao;
import com.example.spring_boot_mode.dao.mode.LoginDao;
import com.example.spring_boot_mode.entity.flowable.Flowable;
import com.example.spring_boot_mode.entity.mode.SysUser;
import com.example.spring_boot_mode.exception.ThrowMsgException;
import com.example.spring_boot_mode.service.Loginservice;
import com.example.spring_boot_mode.utils.TokenUtill;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DS("mode")
@Service
public class LoginserviceImpl implements Loginservice {
    @Autowired
    private LoginDao loginDao;
    @Autowired
    private FlowableDao flowableDao;

    @Override
    public List<SysUser> getSysUser() {
         String test = "345";
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        String name = null;

        return loginDao.selectList(null);
    }

    @DS("flowable")
    @Override
    public List<Flowable> getflowable() {
        return flowableDao.selectList(null);
    }

    @Override
    public Map<String, Object> tologin(SysUser sysUser) {
        if (StringUtils.isEmpty(sysUser.getUsername())){
           throw new ThrowMsgException("*用户名为空");
        }
        if (StringUtils.isEmpty(sysUser.getPassword())) {
            throw new ThrowMsgException("*密码为空");
        }
        Map<String, Object> remap = new HashMap<>();
        SysUser tologin = loginDao.tologin(sysUser.getUsername());
        if (tologin == null) {
            throw new ThrowMsgException("*未查询到该用户");
        }
        if (!sysUser.getPassword().equals(tologin.getPassword())) {
            throw new ThrowMsgException("*密码输入错误");
        }

        remap.put("nickName", tologin.getNickName());
        remap.put("token", TokenUtill.generateToken(tologin));
        return remap;
    }
}
