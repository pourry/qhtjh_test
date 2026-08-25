package com.example.spring_boot_mode.model.entity;

/**
 * 底部内容板块实体类
 * 对应数据库 footer_section 表
 * 用于管理底部简介、联系方式、备案等内容
 */
public class FooterSection {
    /** 主键ID */
    private String id;
    /** 板块类型：intro-简介、contact-联系方式、record-备案 */
    private String type;
    /** 板块标题（如：次元收藏夹、快速链接、联系方式） */
    private String title;
    /** 板块内容（简介为文本，联系方式为多行文本） */
    private String content;
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

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getSort() { return sort; }
    public void setSort(int sort) { this.sort = sort; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }

    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
}
