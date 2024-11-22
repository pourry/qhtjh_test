package com.example.spring_boot_mode.model.web;

import com.example.spring_boot_mode.config.encrypt.sm2.SM2Util;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.entity.SysUser;
import com.example.spring_boot_mode.exception.ThrowMsgException;
import com.example.spring_boot_mode.model.service.Loginservice;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.redis.RedisSafe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    static {
        System.out.println("静态语句块执行了");
    }
    @Autowired
    RedisSafe redisSafe;
    @Autowired
    private Loginservice loginservice;

    //获取sm2 秘钥
    @GetMapping("togetSm2")
    public ResponseObjectEntity togetSm2(){
        //每次重启都会自动生成新的sm2
        String publicKey = SM2Util.getPublicKey();
        return ResponseUtil.success(publicKey);
    }
    @GetMapping("getalltest")
    public ResponseObjectEntity getalltest(){
        List<SysUser> relist= loginservice.getSysUser();
        return ResponseUtil.success(relist);
    }

    @PostMapping("/login")
    public ResponseObjectEntity tologin(SysUser sysUser){
        try{
            Map<String,Object> remap = loginservice.tologin(sysUser);
            return ResponseUtil.success(remap);
        }catch (ThrowMsgException e){
            return ResponseUtil.error(e.getMessage());
        }

    }
    //验证 注册的用户名是否重复
    @PostMapping("/tocheckname")
    public ResponseObjectEntity tocheckname(String username){
        try{
            ResponseObjectEntity responseObjectEntity = loginservice.tocheckname(username);
            return responseObjectEntity;
        }catch (ThrowMsgException e){
            return ResponseUtil.error(e.getMessage());
        }

    }

    @PostMapping("/signUp")
    public ResponseObjectEntity tosignUp(SysUser sysUser){
        try{
            ResponseObjectEntity responseObjectEntity = loginservice.tosignUp(sysUser);
            return responseObjectEntity;
        }catch (ThrowMsgException e){
            return ResponseUtil.error(e.getMessage());
        }

    }

}
