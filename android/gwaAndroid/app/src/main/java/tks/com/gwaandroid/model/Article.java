package tks.com.gwaandroid.model;

import com.google.gson.annotations.SerializedName;

public class Article {
    @SerializedName("id")
    private int id;

    @SerializedName("approvalStatus")
    private String approvalStatus;

    @SerializedName("category")
    private String category;

    @SerializedName("content")
    private String content;

    @SerializedName("date")
    private String date;

    @SerializedName("lastEditor")
    private String lastEditor;

    @SerializedName("modifiedDate")
    private String modifiedDate;

    @SerializedName("title")
    private String title;

    @SerializedName("thumbImage")
    private String thumbImage;

    @SerializedName("description")
    private String description;

    //bi-directional many-to-one association to Account

    @SerializedName("account")
    private Account account;

    public Article(int id, String approvalStatus, String category, String content, String date,
                   String lastEditor, String modifiedDate, String title, String thumbImage,
                   String description, Account account) {
        this.id = id;
        this.approvalStatus = approvalStatus;
        this.category = category;
        this.content = content;
        this.date = date;
        this.lastEditor = lastEditor;
        this.modifiedDate = modifiedDate;
        this.title = title;
        this.thumbImage = thumbImage;
        this.description = description;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLastEditor() {
        return lastEditor;
    }

    public void setLastEditor(String lastEditor) {
        this.lastEditor = lastEditor;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
