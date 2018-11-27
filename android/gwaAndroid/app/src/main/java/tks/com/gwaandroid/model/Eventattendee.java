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

    @SerializedName("ratingdate")
    private String ratingDate;

    //bi-directional many-to-one association to Account
//    @ManyToOne
    @SerializedName("accountid")
    private Account account;

    //bi-directional many-to-one association to Event
//    @ManyToOne
    @SerializedName("eventid")
    private Event event;
}
