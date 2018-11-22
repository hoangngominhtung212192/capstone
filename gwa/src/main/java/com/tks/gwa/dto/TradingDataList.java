package com.tks.gwa.dto;

import java.util.List;

public class TradingDataList {
    private int totalPage;
    private List<TradeListingDTO> data;

    public TradingDataList(int totalPage, List<TradeListingDTO> data) {
        this.totalPage = totalPage;
        this.data = data;
    }

    public TradingDataList() {
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<TradeListingDTO> getData() {
        return data;
    }

    public void setData(List<TradeListingDTO> data) {
        this.data = data;
    }
}
