package com.example.spring_boot_mode.model.entity;

public class GamePictures {
    private String id;
    private String ssgameid;
    private String pictureUrl;
    private String pictureName;
    private String pictureLogic;
    private String picturePath;
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

    public String getSsgameid() {
        return ssgameid;
    }

    public void setSsgameid(String ssgameid) {
        this.ssgameid = ssgameid;
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
