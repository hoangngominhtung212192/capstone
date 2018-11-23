package tks.com.gwaandroid.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Event implements Serializable {
    @SerializedName("id")
    private int id;

//    @Lob
    @SerializedName("content")
    private String content;

    @SerializedName("endDate")
    private String endDate;

    @SerializedName("location")
    private String location;

    @SerializedName("maxAttendee")
    private int maxAttendee;

    @SerializedName("minAttendee")
    private int minAttendee;

    @SerializedName("numberOfAttendee")
    private int numberOfAttendee;

    @SerializedName("numberOfRating")
    private int numberOfRating;

    @SerializedName("numberOfStars")
    private int numberOfStars;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("status")
    private String status;

    @SerializedName("ticketPrice")
    private String ticketPrice;

    @SerializedName("title")
    private String title;

//    @Lob
    @SerializedName("description")
    private String description;

    @SerializedName("thumbImage")
    private String thumbImage;

    @SerializedName("regDateStart")
    private String regDateStart;

    @SerializedName("regDateEnd")
    private String regDateEnd;

    public Event(int id, String content, String endDate, String location, int maxAttendee, int minAttendee, int numberOfAttendee, int numberOfRating, int numberOfStars, String startDate, String status, String ticketPrice, String title, String description, String thumbImage, String regDateStart, String regDateEnd) {
        this.id = id;
        this.content = content;
        this.endDate = endDate;
        this.location = location;
        this.maxAttendee = maxAttendee;
        this.minAttendee = minAttendee;
        this.numberOfAttendee = numberOfAttendee;
        this.numberOfRating = numberOfRating;
        this.numberOfStars = numberOfStars;
        this.startDate = startDate;
        this.status = status;
        this.ticketPrice = ticketPrice;
        this.title = title;
        this.description = description;
        this.thumbImage = thumbImage;
        this.regDateStart = regDateStart;
        this.regDateEnd = regDateEnd;
    }

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
