package com.example.spring_boot_mode.model.entity;

/**
 * 综合收藏实体类
 * 对应数据库 other_collection 表
 */
public class OtherCollection {
    private String id;
    private String userId;
    private String typeValue;
    private String title;
    private String linkUrl;
    private String pictureUrl;
    private String note;
    private String tags;       // JSON array string
    private String status;     // wish / doing / done
    private Integer progress;
    private Boolean pinned;
    private Boolean share;
    private String shareTime;
    private String createTime;
    private String updateTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTypeValue() { return typeValue; }
    public void setTypeValue(String typeValue) { this.typeValue = typeValue; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLinkUrl() { return linkUrl; }
    public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }

    public String getPictureUrl() { return pictureUrl; }
    public void setPictureUrl(String pictureUrl) { this.pictureUrl = pictureUrl; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getProgress() { return progress; }
    public void setProgress(Integer progress) { this.progress = progress; }

    public Boolean getPinned() { return pinned; }
    public void setPinned(Boolean pinned) { this.pinned = pinned; }

    public Boolean getShare() { return share; }
    public void setShare(Boolean share) { this.share = share; }

    public String getShareTime() { return shareTime; }
    public void setShareTime(String shareTime) { this.shareTime = shareTime; }

    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }

    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
}
