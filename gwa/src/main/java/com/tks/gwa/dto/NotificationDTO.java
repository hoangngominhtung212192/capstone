package com.tks.gwa.dto;

import com.tks.gwa.entity.Notification;

import java.util.List;

public class NotificationDTO {

    int lastPage;
    List<Notification> notificationList;
    int notSeen;

    public NotificationDTO(int lastPage, List<Notification> notificationList, int notSeen) {
        this.lastPage = lastPage;
        this.notificationList = notificationList;
        this.notSeen = notSeen;
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
