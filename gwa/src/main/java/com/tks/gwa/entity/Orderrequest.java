package com.tks.gwa.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "orderrequest")
public class Orderrequest implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "billingaddress")
    private String billingAddress;

    @Column(name = "cancelreason")
    private String cancelReason;

    @Column(name = "orderdate")
    private String orderDate;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "rejectreason")
    private String rejectReason;

    @Column(name = "statesetdate")
    private String stateSetDate;

    @Column(name = "status")
    private String status;

    //bi-directional many-to-one association to Account
    @ManyToOne
    @JoinColumn(name="traderid")
    private Account account;

    //bi-directional many-to-one association to Tradepost
    @ManyToOne
    @JoinColumn(name="tradepostid")
    private Tradepost tradepost;

    public Orderrequest(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
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

    public String getStateSetDate() {
        return stateSetDate;
    }

    public void setStateSetDate(String stateSetDate) {
        this.stateSetDate = stateSetDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Tradepost getTradepost() {
        return tradepost;
    }

    public void setTradepost(Tradepost tradepost) {
        this.tradepost = tradepost;
    }
}
