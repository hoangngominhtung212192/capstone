package tks.com.gwaandroid.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Tung Hoang Ngo Minh on 12/3/2018.
 */

public class Notification implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("description")
    private String description;

    @SerializedName("objectID")
    private int objectID;

    @SerializedName("seen")
    private int seen;

    @SerializedName("date")
    private String date;

    @SerializedName("account")
    private Account account;

    @SerializedName("notificationtype")
    private NotificationType notificationType;

    public Notification(int id, String description, int objectID, int seen, String date,
                        Account account, NotificationType notificationType) {
        this.id = id;
        this.description = description;
        this.objectID = objectID;
        this.seen = seen;
        this.date = date;
        this.account = account;
        this.notificationType = notificationType;
    }

    public Notification() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getObjectID() {
        return objectID;
    }

    public void setObjectID(int objectID) {
        this.objectID = objectID;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }
}
