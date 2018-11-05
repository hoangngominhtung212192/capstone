package com.tks.gwa.dto;

import com.tks.gwa.entity.Tradepost;

public class TradeListingDTO {
    private Tradepost tradepost;
    private String thumbnail;

    public TradeListingDTO() {
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

    public TradeListingDTO(Tradepost tradepost, String thumbnail) {
        this.tradepost = tradepost;
        this.thumbnail = thumbnail;
    }
}



