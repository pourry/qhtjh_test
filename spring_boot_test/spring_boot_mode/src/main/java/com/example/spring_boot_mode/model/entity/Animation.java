package com.example.spring_boot_mode.model.entity;

import java.util.List;

/**
 * 动画实体类
 * 对应数据库 animation 表
 * 用于存储动画收藏信息
 */
public class Animation {

    /** 主键ID */
    private String id;
    /** 动画名称 */
    private String name;
    /** 动画地址（播放链接或资源地址） */
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
    /** 动画图片列表（关联查询） */
    private List<AnimationPictures> pictures;
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
    /** 是否开启消息提醒（前端传来的临时字段，不保存到animation表） */
    private Boolean remindopen;
    /** 提醒时间（前端传来的临时字段） */
    private String remindtime;
    /** 提醒消息（前端传来的临时字段） */
    private String remindmsg;

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

    public List<AnimationPictures> getPictures() {
        return pictures;
    }

    public void setPictures(List<AnimationPictures> pictures) {
        this.pictures = pictures;
    }

    public String getHasendLabel() {
        return hasendLabel;
    }

    public void setHasendLabel(String hasendLabel) {
        this.hasendLabel = hasendLabel;
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

    public Boolean getRemindopen() {
        return remindopen;
    }

    public void setRemindopen(Boolean remindopen) {
        this.remindopen = remindopen;
    }

    /**
     * 处理FormData传递的字符串类型转换
     */
    public void setRemindopen(String remindopen) {
        if (remindopen != null && ("true".equalsIgnoreCase(remindopen) || "1".equals(remindopen))) {
            this.remindopen = Boolean.TRUE;
        } else {
            this.remindopen = Boolean.FALSE;
        }
    }

    public String getRemindtime() {
        return remindtime;
    }

    public void setRemindtime(String remindtime) {
        this.remindtime = remindtime;
    }

    public String getRemindmsg() {
        return remindmsg;
    }

    public void setRemindmsg(String remindmsg) {
        this.remindmsg = remindmsg;
    }

    @Override
    public String toString() {
        return "Animation{" +
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
