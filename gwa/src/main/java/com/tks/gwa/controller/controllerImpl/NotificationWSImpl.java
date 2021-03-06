package com.tks.gwa.controller.controllerImpl;

import com.tks.gwa.controller.NotificationWS;
import com.tks.gwa.dto.NotificationDTO;
import com.tks.gwa.entity.Notification;
import com.tks.gwa.firebase.PushNotification;
import com.tks.gwa.service.NotificationService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class NotificationWSImpl implements NotificationWS {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PushNotification pushNotification;

    @Override
    public ResponseEntity<NotificationDTO> getListNotificationByAccountID(int pageNumber, int accountID) {

        System.out.println("[NotificationWS] Begin getListNotificationByAccountID with data:");
        System.out.println("Page number: " + pageNumber);
        System.out.println("AccountID: " + accountID);

        NotificationDTO result = notificationService.getNotificationByAccountID(pageNumber, accountID);

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

    @Override
    public ResponseEntity<String> send() throws JSONException {

        String deviceToken = "eKLkzOU8AkE:APA91bFx6VoEWCxdc3xT3e5z3U9Csb9hIt-GM-jO6-313BOONh0NDta1_92rE0lm60HFhUZqku5MwmFFu3TbKecmdBD-watp2vsWDF0qoumOB6K-GkflueuSYrZ9hIwvhS_exRblBwsT";

        JSONObject body = new JSONObject();
        body.put("to", deviceToken.trim());

        JSONObject notification = new JSONObject();
        notification.put("title", "Notification Type");
        notification.put("body", "Here is body");
        body.put("data", notification);

        System.out.println(body.toString());

        HttpEntity<String> request = new HttpEntity<>(body.toString());

        CompletableFuture<String> pushNotifications = pushNotification.send(request);
        CompletableFuture.allOf(pushNotifications).join();

        try {
            String firebaseResponse = pushNotifications.get();

            return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("Push notification ERROR!", HttpStatus.BAD_REQUEST);
    }
}
