package com.example.spring_boot_mode.model.entity;

/**
 * 底部快速链接实体类
 * 对应数据库 footer_link 表
 * 用于管理底部快速链接
 */
public class FooterLink {
    /** 主键ID */
    private String id;
    /** 链接名称（如：首页、关于我们） */
    private String name;
    /** 链接地址 */
    private String url;
    /** 排序序号，数值越小越靠前 */
    private int sort;
    /** 是否启用：true-显示，false-隐藏 */
    private Boolean enabled = true;
    /** 创建时间 */
    private String createTime;
    /** 更新时间 */
    private String updateTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public int getSort() { return sort; }
    public void setSort(int sort) { this.sort = sort; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }

    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
}
