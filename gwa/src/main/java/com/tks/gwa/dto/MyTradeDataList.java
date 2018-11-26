package com.tks.gwa.dto;

import java.util.List;

public class MyTradeDataList {
    private int totalPage;
    private List<MyTradeDTO> data;

    public MyTradeDataList() {
    }

    public MyTradeDataList(int totalPage, List<MyTradeDTO> data) {
        this.totalPage = totalPage;
        this.data = data;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<MyTradeDTO> getData() {
        return data;
    }

    public void setData(List<MyTradeDTO> data) {
        this.data = data;
    }
}
