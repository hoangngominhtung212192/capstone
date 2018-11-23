package tks.com.gwaandroid.model;

import java.util.List;

public class TradingDataResponse {
    private int totalPage;
    private List<TradingModel> data;

    public TradingDataResponse(int totalPage, List<TradingModel> data) {
        this.totalPage = totalPage;
        this.data = data;
    }

    public TradingDataResponse() {
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<TradingModel> getData() {
        return data;
    }

    public void setData(List<TradingModel> data) {
        this.data = data;
    }
}
