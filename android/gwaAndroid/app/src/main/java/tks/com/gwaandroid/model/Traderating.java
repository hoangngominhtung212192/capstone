package tks.com.gwaandroid.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Tung Hoang Ngo Minh on 12/3/2018.
 */

public class Traderating implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("comment")
    private String comment;

    @SerializedName("feedbackType")
    private int feedbackType;

    @SerializedName("rating")
    private int rating;

    @SerializedName("ratingDate")
    private String ratingDate;

    @SerializedName("fromUser")
    private Account fromUser;

    @SerializedName("toUser")
    private Account toUser;

    @SerializedName("orderrequest")
    private Orderrequest orderrequest;

    public Traderating(int id, String comment, int feedbackType, int rating, String ratingDate,
                       Account fromUser, Account toUser, Orderrequest orderrequest) {
        this.id = id;
        this.comment = comment;
        this.feedbackType = feedbackType;
        this.rating = rating;
        this.ratingDate = ratingDate;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.orderrequest = orderrequest;
    }

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

    public String getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(String ratingDate) {
        this.ratingDate = ratingDate;
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

    public Orderrequest getOrderrequest() {
        return orderrequest;
    }

    public void setOrderrequest(Orderrequest orderrequest) {
        this.orderrequest = orderrequest;
    }
}
