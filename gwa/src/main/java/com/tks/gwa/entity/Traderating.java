package com.tks.gwa.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "traderating")
public class Traderating implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "comment")
    private String comment;

    @Column(name = "feedbacktype")
    private int feedbackType;

    @Column(name = "rating")
    private int rating;

    //bi-directional many-to-one association to Account
    @ManyToOne
    @JoinColumn(name="fromuser")
    private Account fromUser;

    //bi-directional many-to-one association to Account
    @ManyToOne
    @JoinColumn(name="touser")
    private Account toUser;

    //bi-directional many-to-one association to Tradepost
    @ManyToOne
    @JoinColumn(name="tradepostid")
    private Tradepost tradepost;

    public Traderating(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(int feedbackType) {
        this.feedbackType = feedbackType;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Account getFromUser() {
        return fromUser;
    }

    public void setFromUser(Account fromUser) {
        this.fromUser = fromUser;
    }

    public Account getToUser() {
        return toUser;
    }

    public void setToUser(Account toUser) {
        this.toUser = toUser;
    }

    public Tradepost getTradepost() {
        return tradepost;
    }

    public void setTradepost(Tradepost tradepost) {
        this.tradepost = tradepost;
    }
}
