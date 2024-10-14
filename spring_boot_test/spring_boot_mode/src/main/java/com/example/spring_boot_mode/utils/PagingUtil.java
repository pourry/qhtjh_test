package com.example.spring_boot_mode.utils;

import liquibase.pro.packaged.T;

import java.util.List;

public  class PagingUtil<T> {
     //获取 limit 1 2
    private List<T> list;
    private int pageNumber = 1;
    private int pageSize = 10;
    private int total;
    private int passOver;
    private int currentPage;

    public PagingUtil(int pageNumber,int pageSize,List<T> object, int total) {
        this.list = object;
        this.total = total;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.currentPage = (int) Math.ceil(total/pageSize);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

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
}
