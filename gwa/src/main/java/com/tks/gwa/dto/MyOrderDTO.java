package com.tks.gwa.dto;

public class MyOrderDTO {
    private int tradepostId;
    private String tradepostTitle;
    private String tradepostThumbnail;
    private int ownerId;
    private String ownerName;
    private String ownerPhone;
    private String ownerEmail;
    private String ownerAddress;
    private int orderQuantity;
    private double orderPay;
    private String orderedDate;
    private String orderSetDate;
    private String orderReason;
    private String orderStatus;
    private int orderId;
    private  boolean isRated;


    public MyOrderDTO() {
    }

    public MyOrderDTO(int tradepostId, String tradepostTitle, String tradepostThumbnail, int ownerId, String ownerName, String ownerPhone, String ownerEmail, String ownerAddress, int orderQuantity, double orderPay, String orderedDate, String orderSetDate, String orderReason, String orderStatus, int orderId, boolean isRated) {
        this.tradepostId = tradepostId;
        this.tradepostTitle = tradepostTitle;
        this.tradepostThumbnail = tradepostThumbnail;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.ownerPhone = ownerPhone;
        this.ownerEmail = ownerEmail;
        this.ownerAddress = ownerAddress;
        this.orderQuantity = orderQuantity;
        this.orderPay = orderPay;
        this.orderedDate = orderedDate;
        this.orderSetDate = orderSetDate;
        this.orderReason = orderReason;
        this.orderStatus = orderStatus;
        this.orderId = orderId;
        this.isRated = isRated;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isRated() {
        return isRated;
    }

    public void setRated(boolean rated) {
        isRated = rated;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getTradepostId() {
        return tradepostId;
    }

    public void setTradepostId(int tradepostId) {
        this.tradepostId = tradepostId;
    }

    public String getTradepostTitle() {
        return tradepostTitle;
    }

    public void setTradepostTitle(String tradepostTitle) {
        this.tradepostTitle = tradepostTitle;
    }

    public String getTradepostThumbnail() {
        return tradepostThumbnail;
    }

    public void setTradepostThumbnail(String tradepostThumbnail) {
        this.tradepostThumbnail = tradepostThumbnail;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public double getOrderPay() {
        return orderPay;
    }

    public void setOrderPay(double orderPay) {
        this.orderPay = orderPay;
    }

    public String getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(String orderedDate) {
        this.orderedDate = orderedDate;
    }

    public String getOrderSetDate() {
        return orderSetDate;
    }

    public void setOrderSetDate(String orderSetDate) {
        this.orderSetDate = orderSetDate;
    }

    public String getOrderReason() {
        return orderReason;
    }

    public void setOrderReason(String orderReason) {
        this.orderReason = orderReason;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
