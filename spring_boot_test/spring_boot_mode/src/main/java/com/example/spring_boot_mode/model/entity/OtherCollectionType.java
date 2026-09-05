package com.example.spring_boot_mode.model.entity;

/**
 * 综合收藏类型实体类
 * 对应数据库 other_collection_type 表
 * 每个用户独立的自定义类型
 */
public class OtherCollectionType {
    private String id;
    private String userId;
    private String typeValue;
    private String label;
    private String icon;
    private String color;
    private Integer sort;
    private String createTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTypeValue() { return typeValue; }
    public void setTypeValue(String typeValue) { this.typeValue = typeValue; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Integer getSort() { return sort; }
    public void setSort(Integer sort) { this.sort = sort; }

    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
}
