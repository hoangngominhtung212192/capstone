package tks.com.gwaandroid.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tung Hoang Ngo Minh on 12/3/2018.
 */

public class UserRatingDTO implements Serializable {

    @SerializedName("lastPage")
    int lastPage;

    @SerializedName("traderatingList")
    List<Traderating> traderatingList;

    public UserRatingDTO() {
    }

    public UserRatingDTO(int lastPage, List<Traderating> traderatingList) {
        this.lastPage = lastPage;
        this.traderatingList = traderatingList;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public List<Traderating> getTraderatingList() {
        return traderatingList;
    }

    public void setTraderatingList(List<Traderating> traderatingList) {
        this.traderatingList = traderatingList;
    }
}
