package com.tks.gwa.service.serviceImpl;

import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.dto.NotificationDTO;
import com.tks.gwa.entity.Notification;
import com.tks.gwa.entity.Token;
import com.tks.gwa.firebase.PushNotification;
import com.tks.gwa.repository.NotificationRepository;
import com.tks.gwa.repository.NotificationTypeRepository;
import com.tks.gwa.repository.TokenRepository;
import com.tks.gwa.service.NotificationService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationTypeRepository notificationTypeRepository;

    @Autowired
    private PushNotification pushNotification;

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public NotificationDTO getNotificationByAccountID(int pageNumber, int accountID) {

        int total = notificationRepository.getCountNotificationByAccountID(accountID);

        if (total > 0) {
            int lastPage = 0;

            if (total % 10 == 0) {
                lastPage = total / 10;
            } else {
                lastPage = ((total / 10) + 1);
            }

            List<Notification> notificationList = notificationRepository.getListNotificationByAccountID(pageNumber, accountID);
            int notSeen = notificationRepository.getCountNotSeenByAccountID(accountID);

            NotificationDTO dto = new NotificationDTO(lastPage, notificationList, notSeen);

            return dto;
        }

        return new NotificationDTO(0, new ArrayList<Notification>(), 0);
    }

    @Override
    public Notification addNewNotification(Notification notification) {

        String date = getCurrentTimeStamp();
        notification.setDate(date);

        notification.setSeen(AppConstant.NOTIFICATION_NOT_SEEN);

        Notification newNotification = notificationRepository.addNewNotification(notification);

        // firebase send notification
        if (newNotification != null) {
            List<Token> tokens = tokenRepository.findTokenByAccountID(newNotification.getAccount().getId());

            System.out.println("User " + newNotification.getAccount().getId() + " have " + tokens.size() + " tokens!!!");

            String title = notificationTypeRepository.read(newNotification.getNotificationtype().getId()).getName();

            for (Token token : tokens) {
                send(token.getToken(), title, newNotification.getDescription(), newNotification.getAccount().getId());
            }
        }

        return newNotification;
    }

    @Override
    public void updateNotificationSeen(int notificationID) {

        Notification notification = notificationRepository.read(notificationID);

        if (notification != null) {
            notification.setSeen(AppConstant.NOTIFICATION_SEEN);

            notificationRepository.update(notification);
        }
    }

    public String getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String strDate = sdf.format(now);
        return strDate;
    }

    public void send(String token, String notificationType, String content, int accountID) throws JSONException {

        JSONObject body = new JSONObject();
        body.put("to", token.trim());

        JSONObject notification = new JSONObject();
        notification.put("title", notificationType);
        notification.put("body", content);
        notification.put("accountID", accountID);
        body.put("data", notification);

        // print
        System.out.println(body.toString());

        HttpEntity<String> request = new HttpEntity<>(body.toString());

        CompletableFuture<String> pushNotifications = pushNotification.send(request);
        CompletableFuture.allOf(pushNotifications).join();

        try {
            String firebaseResponse = pushNotifications.get();

            System.out.println(firebaseResponse);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
