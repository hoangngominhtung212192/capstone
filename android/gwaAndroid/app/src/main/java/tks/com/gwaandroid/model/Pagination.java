package tks.com.gwaandroid.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tung Hoang Ngo Minh on 11/12/2018.
 */

public class Pagination {

    @SerializedName("total")
    int total;

    @SerializedName("currentPage")
    int currentPage;

    @SerializedName("lastPage")
    int lastPage;

    @SerializedName("beginPage")
    int beginPage;

    @SerializedName("type")
    String type;

    public Pagination(int total, int currentPage, int lastPage, int beginPage, String type) {
        this.total = total;
        this.currentPage = currentPage;
        this.lastPage = lastPage;
        this.beginPage = beginPage;
        this.type = type;
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
