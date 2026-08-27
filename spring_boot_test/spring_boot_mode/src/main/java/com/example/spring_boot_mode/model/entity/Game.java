package com.example.spring_boot_mode.model.entity;

import java.util.List;

/**
 * 游戏实体类
 * 对应数据库 game 表
 * 用于存储游戏收藏信息
 */
public class Game {

    /** 主键ID */
    private String id;
    /** 游戏名称 */
    private String name;
    /** 游戏地址（下载链接或资源地址） */
    private String address;
    /** 备注说明 */
    private String notes;
    /** 别名/其他名称 */
    private String alias;
    /** 是否完结：yes-已完结、no-连载中 */
    private String hasend;
    /** 封面图URL */
    private String pictureURL;
    /** 创建人ID（关联用户） */
    private String sscollector;
    /** 游戏图片列表（关联查询） */
    private List<GamePictures> pictures;
    /** 创建时间 */
    private String createTime;
    /** 额外对象数据 */
    private Object object;
    /** 完结状态标签（显示用） */
    private String hasendLabel;
    /** 是否分享：true-分享到首页，false-仅自己可见 */
    private Boolean share;
    /** 分享时间 */
    private String shareTime;

    public String getHasendLabel() {
        return hasendLabel;
    }

    public void setHasendLabel(String hasendLabel) {
        this.hasendLabel = hasendLabel;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getHasend() {
        return hasend;
    }

    public void setHasend(String hasend) {
        this.hasend = hasend;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getSscollector() {
        return sscollector;
    }

    public void setSscollector(String sscollector) {
        this.sscollector = sscollector;
    }

    public List<GamePictures> getPictures() {
        return pictures;
    }

    public void setPictures(List<GamePictures> pictures) {
        this.pictures = pictures;
    }

    public Boolean getShare() {
        return share;
    }

    public void setShare(Boolean share) {
        this.share = share;
    }

    public String getShareTime() {
        return shareTime;
    }

    public void setShareTime(String shareTime) {
        this.shareTime = shareTime;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", notes='" + notes + '\'' +
                ", alias='" + alias + '\'' +
                ", hasend='" + hasend + '\'' +
                ", pictureURL='" + pictureURL + '\'' +
                '}';
    }
}
