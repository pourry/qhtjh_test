package com.example.spring_boot_mode.model.entity;

import lombok.Data;

/**
 * 用户实体类
 * 对应数据库 sys_user 表
 */
@Data
public class SysUser {
    /** 主键ID */
    private String id;
    /** 登录用户名 */
    private String username;
    /** 登录密码 */
    private String password;
    /** 昵称 */
    private String nickName;
    /** 邮箱 */
    private String email;
    /** 手机号 */
    private String phone;
    /** 生日 */
    private String birthday;
    /** 性别：male/female/other */
    private String gender;
    /** 头像路径 */
    private String avatar;
    /** 用户主题 */
    private String theme;
    /** 登录验证码开关：true-启用验证码，false-不启用 */
    private Boolean captchaEnabled = false;
    /** 创建时间 */
    private String createTime;
    /** 更新时间 */
    private String updateTime;
    /** 在线状态：true-在线，false-离线 */
    private Boolean onlineStatus = false;
    /** 用户角色ID */
    private String roleId;

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Boolean getCaptchaEnabled() {
        return captchaEnabled;
    }

    public void setCaptchaEnabled(Boolean captchaEnabled) {
        this.captchaEnabled = captchaEnabled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }
}
