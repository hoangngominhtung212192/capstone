package com.tks.gwa.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "event")
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "content")
    private String content;

    @Column(name = "enddate")
    private String endDate;

    @Column(name = "location")
    private String location;

    @Column(name = "maxattendee")
    private int maxAttendee;

    @Column(name = "minattendee")
    private int minAttendee;

    @Column(name = "numberofattendee")
    private int numberOfAttendee;

    @Column(name = "numberofrating")
    private int numberOfRating;

    @Column(name = "numberofstars")
    private int numberOfStars;

    @Column(name = "startdate")
    private String startDate;

    @Column(name = "status")
    private String status;

    @Column(name = "ticketprice")
    private String ticketPrice;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "thumbimage")
    private String thumbImage;

    @Column(name = "regdatestart")
    private String regDateStart;

    @Column(name = "regdateend")
    private String regDateEnd;

    public Event(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMaxAttendee() {
        return maxAttendee;
    }

    public void setMaxAttendee(int maxAttendee) {
        this.maxAttendee = maxAttendee;
    }

    public int getMinAttendee() {
        return minAttendee;
    }

    public void setMinAttendee(int minAttendee) {
        this.minAttendee = minAttendee;
    }

    public int getNumberOfAttendee() {
        return numberOfAttendee;
    }

    public void setNumberOfAttendee(int numberOfAttendee) {
        this.numberOfAttendee = numberOfAttendee;
    }

    public int getNumberOfRating() {
        return numberOfRating;
    }

    public void setNumberOfRating(int numberOfRating) {
        this.numberOfRating = numberOfRating;
    }

    public int getNumberOfStars() {
        return numberOfStars;
    }

    public void setNumberOfStars(int numberOfStars) {
        this.numberOfStars = numberOfStars;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

    public String getRegDateStart() {
        return regDateStart;
    }

    public void setRegDateStart(String regDateStart) {
        this.regDateStart = regDateStart;
    }

    public String getRegDateEnd() {
        return regDateEnd;
    }

    public void setRegDateEnd(String regDateEnd) {
        this.regDateEnd = regDateEnd;
    }
}
