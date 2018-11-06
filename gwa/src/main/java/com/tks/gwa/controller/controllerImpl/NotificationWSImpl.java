package com.tks.gwa.controller.controllerImpl;

import com.tks.gwa.controller.NotificationWS;
import com.tks.gwa.entity.Notification;
import com.tks.gwa.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NotificationWSImpl implements NotificationWS {

    @Autowired
    private NotificationService notificationService;

    @Override
    public ResponseEntity<List<Object>> getListNotificationByAccountID(int pageNumber, int accountID) {

        System.out.println("[NotificationWS] Begin getListNotificationByAccountID with data:");
        System.out.println("Page number: " + pageNumber);
        System.out.println("AccountID: " + accountID);

        List<Object> result = notificationService.getNotificationByAccountID(pageNumber, accountID);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Notification> addNewNotification(@RequestBody Notification notification) {

        System.out.println("[NotificationWS] Begin addNewNotification with data:");
        System.out.println("AccountID: " + notification.getAccount().getId());
        System.out.println("ObjectID: " + notification.getObjectID());
        System.out.println("NotificationType: " + notification.getNotificationtype().getId());
        System.out.println("Description: " + notification.getDescription());

        Notification result = notificationService.addNewNotification(notification);

        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        return new ResponseEntity<>(result, HttpStatus.valueOf(400));
    }

    @Override
    public ResponseEntity<String> updateNotificationStatus(int notificationID) {

        System.out.println("[NotificationWS] Begin updateNotificationStatus with notificationID: " + notificationID);

        notificationService.updateNotificationSeen(notificationID);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
