package com.tks.gwa.dto;

public class Pagination {

    int total;
    int currentPage;
    int lastPage;
    int beginPage;
    String type;

    public Pagination(){}

    public Pagination(int total, int currentPage, int lastPage, int beginPage) {
        this.total = total;
        this.currentPage = currentPage;
        this.lastPage = lastPage;
        this.beginPage = beginPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getBeginPage() {
        return beginPage;
    }

    public void setBeginPage(int beginPage) {
        this.beginPage = beginPage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
