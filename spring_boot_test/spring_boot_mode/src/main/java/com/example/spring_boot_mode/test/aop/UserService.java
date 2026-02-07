package com.example.spring_boot_mode.test.aop;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    @ValidateParams(notNull = true, minValue = 1, skipOnInvalid = true)
    public boolean getUserById(Long id) {
        System.out.println("执行查询用户，ID: " + id);
        return true;
    }

    @ValidateParams(regex = "^[a-zA-Z0-9]{6,20}$", skipOnInvalid = true)
    public boolean validateUsername(String username) {
        System.out.println("校验用户名: " + username);
        return true;
    }
}