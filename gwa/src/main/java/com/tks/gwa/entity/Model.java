package com.tks.gwa.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "model")
public class Model implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "code")
    private String code;

    @Column(name = "createddate")
    private String createdDate;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private String price;

    @Column(name = "releaseddate")
    private String releasedDate;

    @Column(name = "status")
    private String status;

    @Column(name = "md5hash")
    private String md5Hash;

    @Column(name = "numberofrating")
    private int numberOfRating;

    @Column(name = "numberofrater")
    private int numberOfRater;

    @Transient
    private String message;

    @Transient
    private String thumbImage;

    //bi-directional many-to-one association to Manufacturer
    @ManyToOne
    @JoinColumn(name="manufacturerid")
    private Manufacturer manufacturer;

    //bi-directional many-to-one association to Productsery
    @ManyToOne
    @JoinColumn(name="productseriesid")
    private Productseries productseries;

    //bi-directional many-to-one association to Seriestitle
    @ManyToOne
    @JoinColumn(name="seriestitleid")
    private Seriestitle seriestitle;

    @Transient
    private List<Modelimage> modelimages;

    public Model(){}

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

    public List<Modelimage> getModelimages() {
        return modelimages;
    }

    public void setModelimages(List<Modelimage> modelimages) {
        this.modelimages = modelimages;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMd5Hash() {
        return md5Hash;
    }

    public void setMd5Hash(String md5Hash) {
        this.md5Hash = md5Hash;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
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
}
