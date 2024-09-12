package com.example.spring_boot_mode.entity.mode;

import lombok.Data;

@Data
public class SysUser {
    private String id;
    private String username;
    private String password;
    private String nickName;
    private String email;

}
