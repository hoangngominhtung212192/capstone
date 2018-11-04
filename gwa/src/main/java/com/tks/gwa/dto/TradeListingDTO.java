package com.tks.gwa.dto;

import com.tks.gwa.entity.Tradepost;

public class TradeListingDTO {
    private Tradepost tradepost;
    private String thumbnail;
    private int totalPage;

    public TradeListingDTO() {
    }

    public TradeListingDTO(Tradepost tradepost, String thumbnail, int totalPage) {
        this.tradepost = tradepost;
        this.thumbnail = thumbnail;
        this.totalPage = totalPage;
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

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
