package com.tks.gwa.dto;

public class NewOrderDTO {
    private int traderId;
    private String traderEmail;
    private String traderPhone;
    private String address;
    private int tradepostId;
    private int quantity;

    public NewOrderDTO() {
    }

    public NewOrderDTO(int traderId, String traderEmail, String traderPhone, String address, int tradepostId, int quantity) {
        this.traderId = traderId;
        this.traderEmail = traderEmail;
        this.traderPhone = traderPhone;
        this.address = address;
        this.tradepostId = tradepostId;
        this.quantity = quantity;
    }

    public int getTraderId() {
        return traderId;
    }

    public void setTraderId(int traderId) {
        this.traderId = traderId;
    }

    public String getTraderEmail() {
        return traderEmail;
    }

    public void setTraderEmail(String traderEmail) {
        this.traderEmail = traderEmail;
    }

    public String getTraderPhone() {
        return traderPhone;
    }

    public void setTraderPhone(String traderPhone) {
        this.traderPhone = traderPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTradepostId() {
        return tradepostId;
    }

    public void setTradepostId(int tradepostId) {
        this.tradepostId = tradepostId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
