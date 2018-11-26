package com.tks.gwa.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tradepost")
public class Tradepost implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "approvalstatus")
    private String approvalStatus;

    @Column(name = "brand")
    private String brand;

    @Column(name = "`condition`")
    private int condition;

    @Lob
    @Column(name = "`description`")
    private String description;

    @Column(name = "lastmodified")
    private String lastModified;

    @Column(name = "location")
    private String location;

    @Column(name = "latlng")
    private String latlng;

    @Column(name = "model")
    private String model;

    @Column(name = "posteddate")
    private String postedDate;

    @Column(name = "price")
    private double price;

    @Column(name = "pricenegotiable")
    private int priceNegotiable;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "rejectreason")
    private String rejectReason;

    @Column(name = "title")
    private String title;

    @Column(name = "tradetype")
    private int tradeType;

    @Column(name = "numberofrater")
    private int numberOfRater;

    @Column(name = "numberofstar")
    private int numberOfStar;

    //bi-directional many-to-one association to Account
    @ManyToOne
    @JoinColumn(name="ownerid")
    private Account account;

    public Tradepost(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPriceNegotiable() {
        return priceNegotiable;
    }

    public void setPriceNegotiable(int priceNegotiable) {
        this.priceNegotiable = priceNegotiable;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getNumberOfRater() {
        return numberOfRater;
    }

    public void setNumberOfRater(int numberOfRater) {
        this.numberOfRater = numberOfRater;
    }

    public int getNumberOfStar() {
        return numberOfStar;
    }

    public void setNumberOfStar(int numberOfStar) {
        this.numberOfStar = numberOfStar;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }
}
