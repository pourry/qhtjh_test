package com.example.spring_boot_mode.model.entity;

/**
 * 网址收藏实体类
 * 对应数据库 url_collection 表
 * 用于存储用户收藏的网址信息
 */
public class UrlCollection {
    /** 主键ID */
    private String id;
    /** 网址名称 */
    private String urlname;
    /** 网址地址 */
    private String url;
    /** 网站Logo路径 */
    private String urllogopath;
    /** 所属分类ID */
    private String ssurltypeid;
    /** 备注说明 */
    private String notes;
    /** 创建人ID（关联用户） */
    private String sscollector;
    /** 创建时间 */
    private String createTime;
    /** 排序序号，数值越小越靠前 */
    private int sort;
    /** 是否分享：true-分享到首页展示，false-仅自己可见 */
    private Boolean share = false;
    /** 分享时间 */
    private String shareTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlname() {
        return urlname;
    }

    public void setUrlname(String urlname) {
        this.urlname = urlname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrllogopath() {
        return urllogopath;
    }

    public void setUrllogopath(String urllogopath) {
        this.urllogopath = urllogopath;
    }

    public String getSsurltypeid() {
        return ssurltypeid;
    }

    public void setSsurltypeid(String ssurltypeid) {
        this.ssurltypeid = ssurltypeid;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
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
}
