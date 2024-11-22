package com.example.spring_boot_mode.model.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.spring_boot_mode.flowable.dao.FlowableDao;
import com.example.spring_boot_mode.model.dao.LoginDao;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.entity.SysUser;
import com.example.spring_boot_mode.exception.ThrowMsgException;
import com.example.spring_boot_mode.model.service.Loginservice;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.TokenUtill;
import com.example.spring_boot_mode.utils.UUidUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public ResponseObjectEntity tosignUp(SysUser sysUser) {
        if (StringUtils.isEmpty(sysUser.getUsername())){
            throw new ThrowMsgException("*用户名为空");
        }
        if (StringUtils.isEmpty(sysUser.getPassword())) {
            throw new ThrowMsgException("*密码为空");
        }
        if (StringUtils.isEmpty(sysUser.getNickName())) {
            throw new ThrowMsgException("*昵称不能为空");
        }
        int tocheckname = loginDao.tocheckname(sysUser.getUsername());
        if (tocheckname>0){
            return ResponseUtil.error("用户名重复");
        }
        sysUser.setId(UUidUtil.getuuid());
        int insert = loginDao.tosignUp(sysUser);
        if (insert >0){
            return ResponseUtil.success("创建成功");
        }
        return ResponseUtil.error("创建失败");
    }

    @Override
    public ResponseObjectEntity tocheckname(String username) {
        int  checkint =  loginDao.tocheckname(username);
        if (checkint >0){
            return ResponseUtil.error("该用户名已被注册");
        }
        return ResponseUtil.success("该用户名可用");
    }
}
