package com.tks.gwa.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "proposal")
public class Proposal implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "description")
    private String description;

    @Column(name = "eventtitle")
    private String eventTitle;

    @Column(name = "location")
    private String location;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "status")
    private String status;

    //bi-directional many-to-one association to Account
    @ManyToOne
    @JoinColumn(name="accountid")
    private Account account;

    public Proposal(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
