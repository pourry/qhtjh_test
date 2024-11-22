package com.example.spring_boot_mode.model.entity.Vo;

import com.example.spring_boot_mode.model.entity.Animation;

public class ComicVo extends Animation {
    private int pageNumber = 1;
    private int pageSize = 10;
    private int total;
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
