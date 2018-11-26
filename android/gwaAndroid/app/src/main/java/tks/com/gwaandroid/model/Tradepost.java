package tks.com.gwaandroid.model;

import com.google.gson.annotations.SerializedName;

public class Tradepost
{
    @SerializedName("id")
    public int id ;

    @SerializedName("approvalStatus")
    public String approvalStatus ;

    @SerializedName("brand")
    public String brand ;

    @SerializedName("condition")
    public int condition ;

    @SerializedName("description")
    public String description ;

    @SerializedName("lastModified")
    public String lastModified ;

    @SerializedName("location")
    public String location ;

    @SerializedName("latlng")
    private String latlng;

    @SerializedName("model")
    public String model ;

    @SerializedName("postedDate")
    public String postedDate ;

    @SerializedName("price")
    public double price ;

    @SerializedName("priceNegotiable")
    public int priceNegotiable ;

    @SerializedName("quantity")
    public int quantity ;

    @SerializedName("rejectReason")
    public String rejectReason ;

    @SerializedName("title")
    public String title ;

    @SerializedName("tradeType")
    public int tradeType ;

    @SerializedName("numberOfRater")
    public int numberOfRater ;

    @SerializedName("numberOfStar")
    public int numberOfStar ;

    @SerializedName("account")
    public Account account ;

    public Tradepost() {
    }

    public Tradepost(int id, String approvalStatus, String brand, int condition, String description, String lastModified, String location, String latlng, String model, String postedDate, double price, int priceNegotiable, int quantity, String rejectReason, String title, int tradeType, int numberOfRater, int numberOfStar, Account account) {
        this.id = id;
        this.approvalStatus = approvalStatus;
        this.brand = brand;
        this.condition = condition;
        this.description = description;
        this.lastModified = lastModified;
        this.location = location;
        this.latlng = latlng;
        this.model = model;
        this.postedDate = postedDate;
        this.price = price;
        this.priceNegotiable = priceNegotiable;
        this.quantity = quantity;
        this.rejectReason = rejectReason;
        this.title = title;
        this.tradeType = tradeType;
        this.numberOfRater = numberOfRater;
        this.numberOfStar = numberOfStar;
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPriceNegotiable() {
        return priceNegotiable;
    }

    public void setPriceNegotiable(int priceNegotiable) {
        this.priceNegotiable = priceNegotiable;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public int getNumberOfRater() {
        return numberOfRater;
    }

    public void setNumberOfRater(int numberOfRater) {
        this.numberOfRater = numberOfRater;
    }

    public int getNumberOfStar() {
        return numberOfStar;
    }

    public void setNumberOfStar(int numberOfStar) {
        this.numberOfStar = numberOfStar;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
