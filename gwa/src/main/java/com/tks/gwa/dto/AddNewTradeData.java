package com.tks.gwa.dto;


import com.tks.gwa.entity.Account;
import com.tks.gwa.entity.Tradepost;

import java.util.Date;

public class AddNewTradeData {
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

    public AddNewTradeData() {
    }

    public AddNewTradeData(String tradeType, String tradeTitle, String[] imageUploadedList, String tradeCondition,
                           Double tradePrice, String tradeNegotiable, int tradeQuantity, String tradeBrand,
                           String tradeModel, String tradeDesc, String traderName, String traderEmail,
                           String traderPhone, String traderAddress) {
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

    @Override
    public String toString() {
        return super.toString();
    }
    public void printContent(){
        System.out.println("***************TRADE POST INFORMATION****************");
        System.out.println("Type:" + this.tradeType);
        System.out.println("Title: " + this.tradeTitle);
        System.out.println("List Images:");
        for (int i = 0; i < this.imageUploadedList.length; i++) {
            System.out.println("- " + this.imageUploadedList[i]);
        }
        System.out.println("Condition: " + this.tradeCondition);
        System.out.println("Price: " + this.tradePrice);
        System.out.println("Price Negotiable: " + this.tradeNegotiable);
        System.out.println("Quantity: " + this.tradeQuantity);
        System.out.println("Brand: " + this.tradeBrand);
        System.out.println("Model: " + this.tradeModel);
        System.out.println("Description: " + this.tradeDesc);
        System.out.println("***************BUYER/SELLER INFORMATION****************");
        System.out.println("Fullname: " + this.traderName);
        System.out.println("Phone: " + this.traderPhone);
        System.out.println("Email: " + this.traderEmail);
        System.out.println("Address: " + this.traderAddress);
    }
    public Tradepost getTradepostEntity(){
        Tradepost result = new Tradepost();
        result.setApprovalStatus("");
        result.setBrand(this.tradeBrand);
        result.setCondition(this.tradeCondition.equals("new") ? 1: 2);
        result.setDescription(this.tradeDesc);
        result.setLastModified((new Date()).toString());
        result.setLocation(this.traderAddress);
        result.setModel(this.tradeModel);
        result.setPostedDate((new Date()).toString());
        result.setPrice(this.tradePrice);
        result.setPriceNegotiable(this.tradeNegotiable.equalsIgnoreCase("on") ? 1 : 0);
        result.setQuantity(this.tradeQuantity);
        result.setTitle(this.tradeTitle);
        result.setTradeType(this.tradeType.equals("sell")? 1 : 2);
        return  result;
    }
    public Account getUserUpdateData(){
        Account result = new Account();
        return result;
    }

}
