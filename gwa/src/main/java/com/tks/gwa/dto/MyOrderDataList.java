package com.tks.gwa.dto;

import java.util.List;

public class MyOrderDataList {
    private int totalPage;
    private List<MyOrderDTO> data;

    public MyOrderDataList(int totalPage, List<MyOrderDTO> data) {
        this.totalPage = totalPage;
        this.data = data;
    }

    public MyOrderDataList() {
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<MyOrderDTO> getData() {
        return data;
    }

    public void setData(List<MyOrderDTO> data) {
        this.data = data;
    }
}
