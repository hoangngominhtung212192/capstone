package com.tks.gwa.dto;

import com.tks.gwa.entity.Orderrequest;
import com.tks.gwa.entity.Tradepost;

import java.util.List;

public class MyTradeDTO {
    private Tradepost myTradePost;
    private int numOfSucceedRequest;
    private int numOfPendingRequest;
    private int numOfOnPaymentRequest;
    private String thumbnail;

    public MyTradeDTO() {
    }

    public MyTradeDTO(Tradepost myTradePost, int numOfSucceedRequest, int numOfPendingRequest, int numOfOnPaymentRequest, String thumbnail) {
        this.myTradePost = myTradePost;
        this.numOfSucceedRequest = numOfSucceedRequest;
        this.numOfPendingRequest = numOfPendingRequest;
        this.numOfOnPaymentRequest = numOfOnPaymentRequest;
        this.thumbnail = thumbnail;
    }

    public Tradepost getMyTradePost() {
        return myTradePost;
    }

    public void setMyTradePost(Tradepost myTradePost) {
        this.myTradePost = myTradePost;
    }

    public int getNumOfSucceedRequest() {
        return numOfSucceedRequest;
    }

    public void setNumOfSucceedRequest(int numOfSucceedRequest) {
        this.numOfSucceedRequest = numOfSucceedRequest;
    }

    public int getNumOfPendingRequest() {
        return numOfPendingRequest;
    }

    public void setNumOfPendingRequest(int numOfPendingRequest) {
        this.numOfPendingRequest = numOfPendingRequest;
    }

    public int getNumOfOnPaymentRequest() {
        return numOfOnPaymentRequest;
    }

    public void setNumOfOnPaymentRequest(int numOfOnPaymentRequest) {
        this.numOfOnPaymentRequest = numOfOnPaymentRequest;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
