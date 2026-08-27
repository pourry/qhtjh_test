package com.example.spring_boot_mode.model.entity.Vo;

import com.example.spring_boot_mode.model.entity.Animation;

/**
 * 漫画视图对象（VO）
 * 继承 Animation，增加分页查询参数
 * 用于漫画列表的分页查询
 */
public class ComicVo extends Animation {
    /** 当前页码，从1开始 */
    private int pageNumber = 1;
    /** 每页显示条数 */
    private int pageSize = 10;
    /** 总记录数 */
    private int total;
    /** 偏移量（计算值：(pageNumber-1)*pageSize） */
    private int passOver;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPassOver() {
        return (this.pageNumber-1)*this.pageSize;
    }

    public void setPassOver(int passOver) {
        this.passOver = passOver;
    }

}
