package com.example.spring_boot_mode.entity.mode.Vo;

import com.example.spring_boot_mode.entity.mode.Animation;

public class AnimationVo extends Animation {
    private int pageNumber;
    private int pageSize;
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
        return passOver;
    }

    public void setPassOver(int passOver) {
        this.passOver = passOver;
    }
    public  void getpassOver(){
        this.passOver = (this.pageNumber-1)*this.pageSize;
    }
}
