package com.example.spring_boot_mode.entity.mode;

public class UrlCollection {
    private String id;
    private String urlname;
    private String url;
    private String urllogopath;
    private String ssurltypeid ;
    private String notes;
    private String sscollector;
    private String createTime;

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
}
