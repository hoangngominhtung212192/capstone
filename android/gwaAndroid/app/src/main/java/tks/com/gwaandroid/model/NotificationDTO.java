package tks.com.gwaandroid.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tung Hoang Ngo Minh on 12/3/2018.
 */

public class NotificationDTO implements Serializable {

    @SerializedName("lastPage")
    int lastPage;

    @SerializedName("notificationList")
    List<Notification> notificationList;

    @SerializedName("notSeen")
    int notSeen;

    public NotificationDTO(int lastPage, List<Notification> notificationList, int notSeen) {
        this.lastPage = lastPage;
        this.notificationList = notificationList;
        this.notSeen = notSeen;
    }

    public NotificationDTO() {
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public int getNotSeen() {
        return notSeen;
    }

    public void setNotSeen(int notSeen) {
        this.notSeen = notSeen;
    }
}
