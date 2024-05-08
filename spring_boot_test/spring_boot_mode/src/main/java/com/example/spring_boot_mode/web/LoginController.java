package com.example.spring_boot_mode.web;

import com.example.spring_boot_mode.config.encrypt.sm2.SM2Util;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.service.Loginservice;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.redis.RedisSafe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

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
        List<Map<String,String>> relist= loginservice.getalltest();
        return ResponseUtil.success(relist);
    }
}
