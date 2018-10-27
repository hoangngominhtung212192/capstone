package com.tks.gwa.dto;

import com.tks.gwa.entity.Orderrequest;
import com.tks.gwa.entity.Tradepost;

import java.util.List;

public class MyTradeDTO {
    private Tradepost myTradePost;
    private List<Orderrequest> myRequest;
    private String thumbnail;

    public MyTradeDTO() {
    }

    public MyTradeDTO(Tradepost myTradePost, List<Orderrequest> myRequest, String thumbnail) {
        this.myTradePost = myTradePost;
        this.myRequest = myRequest;
        this.thumbnail = thumbnail;
    }

    public Tradepost getMyTradePost() {
        return myTradePost;
    }

    public void setMyTradePost(Tradepost myTradePost) {
        this.myTradePost = myTradePost;
    }

    public List<Orderrequest> getMyRequest() {
        return myRequest;
    }

    public void setMyRequest(List<Orderrequest> myRequest) {
        this.myRequest = myRequest;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
