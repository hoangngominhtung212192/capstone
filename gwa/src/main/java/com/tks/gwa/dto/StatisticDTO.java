package com.tks.gwa.dto;

public class StatisticDTO {

    int sell;
    int buy;
    int proposal;

    public StatisticDTO() {}

    public StatisticDTO(int sell, int buy, int proposal) {
        this.sell = sell;
        this.buy = buy;
        this.proposal = proposal;
    }

    public int getSell() {
        return sell;
    }

    public void setSell(int sell) {
        this.sell = sell;
    }

    public int getBuy() {
        return buy;
    }

    public void setBuy(int buy) {
        this.buy = buy;
    }

    public int getProposal() {
        return proposal;
    }

    public void setProposal(int proposal) {
        this.proposal = proposal;
    }
}
