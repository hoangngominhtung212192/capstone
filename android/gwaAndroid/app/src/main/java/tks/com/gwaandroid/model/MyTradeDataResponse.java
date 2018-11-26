package tks.com.gwaandroid.model;

import java.util.List;

public class MyTradeDataResponse {
    private int totalPage;
    private List<MyTradeModel> data;

    public MyTradeDataResponse() {
    }

    public MyTradeDataResponse(int totalPage, List<MyTradeModel> data) {
        this.totalPage = totalPage;
        this.data = data;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<MyTradeModel> getData() {
        return data;
    }

    public void setData(List<MyTradeModel> data) {
        this.data = data;
    }
}
