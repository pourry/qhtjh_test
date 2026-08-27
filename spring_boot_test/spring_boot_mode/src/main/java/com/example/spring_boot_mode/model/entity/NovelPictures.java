package com.example.spring_boot_mode.model.entity;

/**
 * 小说图片实体类
 * 对应数据库 novel_pictures 表
 * 用于存储小说的封面/插图信息
 */
public class NovelPictures {
    /** 主键ID */
    private String id;
    /** 所属小说ID */
    private String ssnovelid;
    /** 图片完整URL（映射后的访问地址） */
    private String pictureUrl;
    /** 图片名称 */
    private String pictureName;
    /** 图片逻辑文件名（含后缀，如 uuid.png） */
    private String pictureLogic;
    /** 图片存储路径（如 D:\picture\novel） */
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

    public String getSsnovelid() {
        return ssnovelid;
    }

    public void setSsnovelid(String ssnovelid) {
        this.ssnovelid = ssnovelid;
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
