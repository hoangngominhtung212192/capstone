package tks.com.gwaandroid.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Tung Hoang Ngo Minh on 11/12/2018.
 */

public class Model implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("code")
    private String code;

    @SerializedName("createdDate")
    private String createdDate;

    @SerializedName("description")
    private String description;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private String price;

    @SerializedName("releasedDate")
    private String releasedDate;

    @SerializedName("status")
    private String status;

    @SerializedName("numberOfRating")
    private int numberOfRating;

    @SerializedName("numberOfRater")
    private int numberOfRater;

    @SerializedName("message")
    private String message;

    @SerializedName("thumbImage")
    private String thumbImage;

    @SerializedName("manufacturer")
    private Manufacturer manufacturer;

    @SerializedName("productseries")
    private Productseries productseries;

    @SerializedName("seriestitle")
    private Seriestitle seriestitle;

    public Model(int id, String code, String createdDate, String description, String name, String price,
                 String releasedDate, String status, int numberOfRating, int numberOfRater,
                 String message, String thumbImage, Manufacturer manufacturer, Productseries productseries, Seriestitle seriestitle) {
        this.id = id;
        this.code = code;
        this.createdDate = createdDate;
        this.description = description;
        this.name = name;
        this.price = price;
        this.releasedDate = releasedDate;
        this.status = status;
        this.numberOfRating = numberOfRating;
        this.numberOfRater = numberOfRater;
        this.message = message;
        this.thumbImage = thumbImage;
        this.manufacturer = manufacturer;
        this.productseries = productseries;
        this.seriestitle = seriestitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(String releasedDate) {
        this.releasedDate = releasedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNumberOfRating() {
        return numberOfRating;
    }

    public void setNumberOfRating(int numberOfRating) {
        this.numberOfRating = numberOfRating;
    }

    public int getNumberOfRater() {
        return numberOfRater;
    }

    public void setNumberOfRater(int numberOfRater) {
        this.numberOfRater = numberOfRater;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Productseries getProductseries() {
        return productseries;
    }

    public void setProductseries(Productseries productseries) {
        this.productseries = productseries;
    }

    public Seriestitle getSeriestitle() {
        return seriestitle;
    }

    public void setSeriestitle(Seriestitle seriestitle) {
        this.seriestitle = seriestitle;
    }
}
