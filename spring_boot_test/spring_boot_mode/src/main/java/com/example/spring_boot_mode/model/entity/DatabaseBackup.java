package com.example.spring_boot_mode.model.entity;

import java.util.Date;

/**
 * 数据库备份实体类
 * 对应数据库 database_backup 表
 * 用于记录数据库备份文件信息
 */
public class DatabaseBackup {
    /** 主键ID */
    private Long id;
    /** 备份文件名 */
    private String fileName;
    /** 备份文件存储路径 */
    private String filePath;
    /** 备份文件大小（单位：字节） */
    private Long fileSize;
    /** 创建时间 */
    private Date createTime;
    /** 备份状态：success-成功、failed-失败、processing-进行中 */
    private String status;
    /** 备注信息 */
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
