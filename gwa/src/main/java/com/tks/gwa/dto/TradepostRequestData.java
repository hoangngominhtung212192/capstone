package com.tks.gwa.dto;


import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.entity.Tradepost;

import java.util.Date;

public class TradepostRequestData {
    private  int traderId;
    private int tradeId;
    private String tradeType;
    private String tradeTitle;
    private String[] imageUploadedList;
    private String tradeCondition;
    private Double tradePrice;
    private String tradeNegotiable;
    private int tradeQuantity;
    private String tradeBrand;
    private String tradeModel;
    private String tradeDesc;
    private String traderName;
    private String traderEmail;
    private String traderPhone;
    private String traderAddress;
    private String traderLatlng;


    public TradepostRequestData() {
    }

    public TradepostRequestData(int traderId, int tradeId, String tradeType, String tradeTitle, String[] imageUploadedList, String tradeCondition, Double tradePrice, String tradeNegotiable, int tradeQuantity, String tradeBrand, String tradeModel, String tradeDesc, String traderName, String traderEmail, String traderPhone, String traderAddress, String traderLatlng) {
        this.traderId = traderId;
        this.tradeId = tradeId;
        this.tradeType = tradeType;
        this.tradeTitle = tradeTitle;
        this.imageUploadedList = imageUploadedList;
        this.tradeCondition = tradeCondition;
        this.tradePrice = tradePrice;
        this.tradeNegotiable = tradeNegotiable;
        this.tradeQuantity = tradeQuantity;
        this.tradeBrand = tradeBrand;
        this.tradeModel = tradeModel;
        this.tradeDesc = tradeDesc;
        this.traderName = traderName;
        this.traderEmail = traderEmail;
        this.traderPhone = traderPhone;
        this.traderAddress = traderAddress;
        this.traderLatlng = traderLatlng;
    }

    public int getTraderId() {
        return traderId;
    }

    public void setTraderId(int traderId) {
        this.traderId = traderId;
    }

    public int getTradeId() {
        return tradeId;
    }

    public void setTradeId(int tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeTitle() {
        return tradeTitle;
    }

    public void setTradeTitle(String tradeTitle) {
        this.tradeTitle = tradeTitle;
    }

    public String[] getImageUploadedList() {
        return imageUploadedList;
    }

    public void setImageUploadedList(String[] imageUploadedList) {
        this.imageUploadedList = imageUploadedList;
    }

    public String getTradeCondition() {
        return tradeCondition;
    }

    public void setTradeCondition(String tradeCondition) {
        this.tradeCondition = tradeCondition;
    }

    public Double getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(Double tradePrice) {
        this.tradePrice = tradePrice;
    }

    public String getTradeNegotiable() {
        return tradeNegotiable;
    }

    public void setTradeNegotiable(String tradeNegotiable) {
        this.tradeNegotiable = tradeNegotiable;
    }

    public int getTradeQuantity() {
        return tradeQuantity;
    }

    public void setTradeQuantity(int tradeQuantity) {
        this.tradeQuantity = tradeQuantity;
    }

    public String getTradeBrand() {
        return tradeBrand;
    }

    public void setTradeBrand(String tradeBrand) {
        this.tradeBrand = tradeBrand;
    }

    public String getTradeModel() {
        return tradeModel;
    }

    public void setTradeModel(String tradeModel) {
        this.tradeModel = tradeModel;
    }

    public String getTradeDesc() {
        return tradeDesc;
    }

    public void setTradeDesc(String tradeDesc) {
        this.tradeDesc = tradeDesc;
    }

    public String getTraderName() {
        return traderName;
    }

    public void setTraderName(String traderName) {
        this.traderName = traderName;
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

    public String getTraderAddress() {
        return traderAddress;
    }

    public void setTraderAddress(String traderAddress) {
        this.traderAddress = traderAddress;
    }

    public String getTraderLatlng() {
        return traderLatlng;
    }

    public void setTraderLatlng(String traderLatlng) {
        this.traderLatlng = traderLatlng;
    }

    @Override
    public String toString() {
        return super.toString();
    }
    public void printContent(){
        System.out.println("***************TRADE POST INFORMATION****************");
        System.out.println("ID:" + this.tradeId);
        System.out.println("Type:" + this.tradeType);
        System.out.println("Title: " + this.tradeTitle);
        System.out.println("Condition: " + this.tradeCondition);
        System.out.println("Price: " + this.tradePrice);
        System.out.println("Price Negotiable: " + this.tradeNegotiable);
        System.out.println("Quantity: " + this.tradeQuantity);
        System.out.println("Brand: " + this.tradeBrand);
        System.out.println("Model: " + this.tradeModel);
        System.out.println("Description: " + this.tradeDesc);
        System.out.println("******************TRADER INFORMATION******************");
        System.out.println("Fullname: " + this.traderName);
        System.out.println("Phone: " + this.traderPhone);
        System.out.println("Email: " + this.traderEmail);
        System.out.println("Address: " + this.traderAddress);
    }

}
