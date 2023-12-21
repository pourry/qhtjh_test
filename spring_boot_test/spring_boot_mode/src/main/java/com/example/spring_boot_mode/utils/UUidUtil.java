package com.example.spring_boot_mode.utils;


import java.util.UUID;

public class UUidUtil {

    //生成带-的uuid
    public static String getuuid(){
        return UUID.randomUUID().toString();
    }

    //生成不带-的uuid
    public static String getuuidSimple(){
        String uid = UUID.randomUUID().toString();
        uid = uid.replaceAll("-","");
        return uid;
    }
}
