package com.tks.gwa.dto;

import com.tks.gwa.entity.Tradepost;

public class ViewTradepostDTO {
    private Tradepost tradepost;
    private int totalOrder;
    private String[] images;

    public ViewTradepostDTO() {
    }

    public ViewTradepostDTO(Tradepost tradepost, int totalOrder, String[] images) {
        this.tradepost = tradepost;
        this.totalOrder = totalOrder;
        this.images = images;
    }

    public Tradepost getTradepost() {
        return tradepost;
    }

    public void setTradepost(Tradepost tradepost) {
        this.tradepost = tradepost;
    }

    public int getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(int totalOrder) {
        this.totalOrder = totalOrder;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }
}
