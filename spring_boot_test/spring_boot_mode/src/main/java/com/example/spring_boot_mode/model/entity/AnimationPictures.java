package com.example.spring_boot_mode.model.entity;

/**
 * 动画图片实体类
 * 对应数据库 animation_pictures 表
 * 用于存储动画的封面/插图信息
 */
public class AnimationPictures {
    /** 主键ID */
    private String id;
    /** 所属动画ID */
    private String ssanimationid;
    /** 图片完整URL（映射后的访问地址） */
    private String pictureUrl;
    /** 图片名称 */
    private String pictureName;
    /** 图片逻辑文件名（含后缀，如 uuid.png） */
    private String pictureLogic;
    /** 图片存储路径（如 D:\picture\animation） */
    private String picturePath;
    /** 创建时间 */
    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSsanimationid() {
        return ssanimationid;
    }

    public void setSsanimationid(String ssanimationid) {
        this.ssanimationid = ssanimationid;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPictureLogic() {
        return pictureLogic;
    }

    public void setPictureLogic(String pictureLogic) {
        this.pictureLogic = pictureLogic;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

}
