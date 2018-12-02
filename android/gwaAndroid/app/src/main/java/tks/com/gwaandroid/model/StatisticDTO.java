package tks.com.gwaandroid.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tung Hoang Ngo Minh on 12/2/2018.
 */

public class StatisticDTO {

    @SerializedName("sell")
    int sell;

    @SerializedName("buy")
    int buy;

    @SerializedName("proposal")
    int proposal;

    public StatisticDTO(int buy, int sell, int proposal) {
        this.buy = buy;
        this.sell = sell;
        this.proposal = proposal;
    }

    public int getBuy() {
        return buy;
    }

    public void setBuy(int buy) {
        this.buy = buy;
    }

    public int getSell() {
        return sell;
    }

    public void setSell(int sell) {
        this.sell = sell;
    }

    public int getProposal() {
        return proposal;
    }

    public void setProposal(int proposal) {
        this.proposal = proposal;
    }
}
