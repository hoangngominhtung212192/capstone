package com.tks.gwa.dto;

import com.tks.gwa.entity.Tradepost;

public class ViewTradepostDTO {
    private Tradepost tradepost;
    private String[] images;

    public ViewTradepostDTO() {
    }

    public ViewTradepostDTO(Tradepost tradepost, String[] images) {
        this.tradepost = tradepost;
        this.images = images;
    }

    public Tradepost getTradepost() {
        return tradepost;
    }

    public void setTradepost(Tradepost tradepost) {
        this.tradepost = tradepost;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }
}
