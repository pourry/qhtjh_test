package com.example.spring_boot_mode.web;

import com.example.spring_boot_mode.config.encrypt.sm2.SM2Util;
import com.example.spring_boot_mode.config.encrypt.sm3.SM3Util;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("totest")
    public  String test(String value){
        System.out.println(SM2Util.decrypt(value));;
        System.out.println(SM3Util.toSM3("qwe123二套房"));
        return "test";
    }
}
