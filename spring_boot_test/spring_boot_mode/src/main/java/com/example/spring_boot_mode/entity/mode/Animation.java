package com.example.spring_boot_mode.entity.mode;

import java.util.List;
import java.util.Objects;

public class Animation {

    private String id;
    private String name;
    private String address;
    private String notes;
    private String alias;
    private String hasend;
    private String pictureURL;
    private String sscollector;
    private List<AnimationPictures> pictures;
    private String createTime;
    private Object object;
    private String hasendLabel;
    private Boolean share;
    private String shareTime;

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
