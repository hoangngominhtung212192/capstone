package com.tks.gwa.dto;

import com.tks.gwa.entity.Traderating;

import java.util.List;

public class UserRatingDTO {

    int lastPage;
    List<Traderating> traderatingList;

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
