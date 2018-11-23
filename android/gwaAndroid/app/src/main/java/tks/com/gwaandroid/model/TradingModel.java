package tks.com.gwaandroid.model;

import com.google.gson.annotations.SerializedName;

public class TradingModel {
    @SerializedName("tradepost")
    public Tradepost tradepost ;
    @SerializedName("thumbnail")
    public String thumbnail ;

    public TradingModel() {
    }

    public TradingModel(Tradepost tradepost, String thumbnail) {
        this.tradepost = tradepost;
        this.thumbnail = thumbnail;
    }

    public Tradepost getTradepost() {
        return tradepost;
    }

    public void setTradepost(Tradepost tradepost) {
        this.tradepost = tradepost;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
