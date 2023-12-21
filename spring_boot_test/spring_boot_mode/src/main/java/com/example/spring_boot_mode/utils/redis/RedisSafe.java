package com.example.spring_boot_mode.utils.redis;

import com.example.spring_boot_mode.utils.UUidUtil;
import liquibase.pro.packaged.L;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisSafe {
    @Autowired
    RedisTemplate redisTemplate;

    //创建用户的token 并作为key 存入redis中  并设置存储时间
    public  String createtoken(long time) {
        String token = "token" + UUidUtil.getuuid();
        Map<String, Object> tokenMap = new HashMap<>();
        //                              key     value   存储时间(s)-1为永久   返回boolean
        redisTemplate.opsForValue().set(token, tokenMap, time, TimeUnit.SECONDS);
        return token;
    }

    //根据token 获取用户的登录信息
    public Map getMesage(String key) {
        Map<String, Object> tokenMap = null;
        if (key != null){
            tokenMap  = (Map<String, Object>) redisTemplate.opsForValue().get(key);
        }else {
            log.info("未传入token："+ key);
        }

        return tokenMap;
    }

    //删除 redis 中的存储的 用户信息以及 token
    public boolean remove(String key){
        Boolean hasdel = false;
        if (key != null){
            hasdel = redisTemplate.delete(key);
        }else {
            log.info("未传入token："+ key);
        }
        return hasdel;
    }

    //查看token 以及用户信息是否存在
    public boolean hasObject(String key){
        Boolean has = false;
        if (key != null) {
           has  =  redisTemplate.hasKey(key);
        }else {
            log.info("未传入token："+ key);
        }
        return has;
    }

    //获取 过期时间
    public long getExpire(String key){
        if (key != null) {
            return redisTemplate.getExpire(key);
        }else {
            log.info("未传入key："+ key);
            return -1;
        }

    }


}
