package com.example.spring_boot_mode.model.entity;

/**
 * 走马灯实体类
 * 对应数据库 carousel 表
 * 用于首页走马灯图片管理
 */
public class Carousel {
    /** 主键ID */
    private String id;
    /** 标题/描述 */
    private String title;
    /** 图片逻辑文件名（含后缀，如 uuid.png） */
    private String pictureLogic;
    /** 图片存储路径（如 D:\picture\carousel） */
    private String picturePath;
    /** 图片完整URL（映射后的访问地址） */
    private String pictureUrl;
    /** 跳转链接地址 */
    private String linkUrl;
    /** 跳转方式：blank-新窗口打开、self-本页面跳转、router-路由内部跳转 */
    private String linkTarget = "blank";
    /** 排序序号，数值越小越靠前 */
    private int sort;
    /** 是否启用：true-显示，false-隐藏 */
    private Boolean enabled = true;
    /** 图片填充样式：fill-拉伸填满、contain-保持比例完整显示、cover-裁剪填满、none-原始尺寸、scale-down-缩小到合适 */
    private String objectFit = "cover";
    /** 创建时间 */
    private String createTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getObjectFit() { return objectFit; }
    public void setObjectFit(String objectFit) { this.objectFit = objectFit; }

    public String getPictureLogic() { return pictureLogic; }
    public void setPictureLogic(String pictureLogic) { this.pictureLogic = pictureLogic; }

    public String getPicturePath() { return picturePath; }
    public void setPicturePath(String picturePath) { this.picturePath = picturePath; }

    public String getPictureUrl() { return pictureUrl; }
    public void setPictureUrl(String pictureUrl) { this.pictureUrl = pictureUrl; }

    public String getLinkUrl() { return linkUrl; }
    public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }

    public String getLinkTarget() { return linkTarget; }
    public void setLinkTarget(String linkTarget) { this.linkTarget = linkTarget; }

    public int getSort() { return sort; }
    public void setSort(int sort) { this.sort = sort; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
}
