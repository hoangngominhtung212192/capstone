package com.tks.gwa.dto;

import com.tks.gwa.entity.Orderrequest;

import java.util.List;

public class OrderRequestDataList {
    private int totalPage;
    private List<Orderrequest> data;

    public OrderRequestDataList() {
    }

    public OrderRequestDataList(int totalPage, List<Orderrequest> data) {
        this.totalPage = totalPage;
        this.data = data;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<Orderrequest> getData() {
        return data;
    }

    public void setData(List<Orderrequest> data) {
        this.data = data;
    }
}
