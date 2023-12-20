package com.example.spring_boot_mode.utils;


import com.example.spring_boot_mode.entity.ResponseObjectEntity;

public class ResponseUtil {


    public static ResponseObjectEntity success(Object object){
        ResponseObjectEntity responseObjectEntity = new ResponseObjectEntity(200,true,object);

        return  responseObjectEntity;
    }

    public static ResponseObjectEntity error(Object object){
        if (object == null){
            object = "请求错误，请稍后重试。。。";
        }
        ResponseObjectEntity responseObjectEntity = new ResponseObjectEntity(403, false, object);
        return responseObjectEntity;
    }
}
