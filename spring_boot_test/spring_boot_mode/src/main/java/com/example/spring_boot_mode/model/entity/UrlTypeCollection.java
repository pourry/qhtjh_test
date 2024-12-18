package com.example.spring_boot_mode.model.entity;

import java.util.List;

public class UrlTypeCollection {
    private String id;
    private String typename;
    private String sscollector;
    private String createTime;
    private List<UrlCollection> children;
    private int sort;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getSscollector() {
        return sscollector;
    }

    public void setSscollector(String sscollector) {
        this.sscollector = sscollector;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<UrlCollection> getChildren() {
        return children;
    }

    public void setChildren(List<UrlCollection> children) {
        this.children = children;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
