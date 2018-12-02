package tks.com.gwaandroid.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Eventattendee implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("amount")
    private int amount;

    @SerializedName("date")
    private String date;

    @SerializedName("feedback")
    private String feedback;

    @SerializedName("rating")
    private int rating;

    @SerializedName("ratingDate")
    private String ratingDate;

    //bi-directional many-to-one association to Account
//    @ManyToOne
    @SerializedName("account")
    private Account account;

    //bi-directional many-to-one association to Event
//    @ManyToOne
    @SerializedName("event")
    private Event event;

    public Eventattendee(int id, int amount, String date, String feedback, int rating, String ratingDate, Account account, Event event) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.feedback = feedback;
        this.rating = rating;
        this.ratingDate = ratingDate;
        this.account = account;
        this.event = event;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
