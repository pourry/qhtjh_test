package com.example.spring_boot_mode.config.encrypt.sm3;


import cn.hutool.crypto.SmUtil;
import java.util.Map;

/*
* SM3算法：SM3杂凑算法是我国自主设计的密码杂凑算法，适用于商用密码应用中的数字签名和验证消息认证码的生成与验证以及随机数的生成，
* 可满足多种密码应用的安全需求。
* 为了保证杂凑算法的安全性，其产生的杂凑值的长度不应太短，例如MD5输出128比特杂凑值，输出长度太短，影响其安全性。
* SHA-1算法的输出长度为160比特，SM3算法的输出长度为256比特，因此SM3算法的安全性要高于MD5算法和SHA-1算法。
* */

//摘要签名算法   以及防止重复攻击
public class SM3Util {



    //将获取到的数据进行 摘要加密
    public static String toSM3(Object object){
        String sObject  = "";
        //将对像拼接为String类型对象
        if (object instanceof Map) {
            sObject = object.toString();
        }else if (object instanceof String){
            sObject = object.toString();
        }
        sObject=  SmUtil.sm3(sObject);
        return sObject;
    }

}
