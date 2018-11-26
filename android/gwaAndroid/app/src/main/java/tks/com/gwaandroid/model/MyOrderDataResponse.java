package tks.com.gwaandroid.model;

import java.util.List;

public class MyOrderDataResponse {
    private int totalPage;
    private List<MyOrderModel> data;

    public MyOrderDataResponse(int totalPage, List<MyOrderModel> data) {
        this.totalPage = totalPage;
        this.data = data;
    }

    public MyOrderDataResponse() {
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<MyOrderModel> getData() {
        return data;
    }

    public void setData(List<MyOrderModel> data) {
        this.data = data;
    }
}
