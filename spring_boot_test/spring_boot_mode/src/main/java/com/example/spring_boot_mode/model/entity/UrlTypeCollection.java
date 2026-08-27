package com.example.spring_boot_mode.model.entity;

import java.util.List;

/**
 * 网址分类实体类
 * 对应数据库 url_type_collection 表
 * 用于存储网址收藏的分类信息
 */
public class UrlTypeCollection {
    /** 主键ID */
    private String id;
    /** 分类名称 */
    private String typename;
    /** 创建人ID（关联用户） */
    private String sscollector;
    /** 创建时间 */
    private String createTime;
    /** 分类下的网址列表（关联查询） */
    private List<UrlCollection> children;
    /** 排序序号，数值越小越靠前 */
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
