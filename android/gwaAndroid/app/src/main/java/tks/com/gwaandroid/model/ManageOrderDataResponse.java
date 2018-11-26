package tks.com.gwaandroid.model;

import java.util.List;

public class ManageOrderDataResponse {
    private int totalPage;
    private List<Orderrequest> data;

    public ManageOrderDataResponse() {
    }

    public ManageOrderDataResponse(int totalPage, List<Orderrequest> data) {
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
