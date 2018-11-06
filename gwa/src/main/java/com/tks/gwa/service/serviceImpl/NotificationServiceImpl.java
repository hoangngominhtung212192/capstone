package com.tks.gwa.service.serviceImpl;

import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.entity.Notification;
import com.tks.gwa.repository.NotificationRepository;
import com.tks.gwa.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public List<Object> getNotificationByAccountID(int pageNumber, int accountID) {

        List<Object> result = new ArrayList<>();

        int total = notificationRepository.getCountNotificationByAccountID(accountID);

        if (total > 0) {
            int lastPage = 0;

            if (total % 10 == 0) {
                lastPage = total / 10;
            } else {
                lastPage = ((total / 10) + 1);
            }

            result.add(lastPage);

            List<Notification> notificationList = notificationRepository.getListNotificationByAccountID(pageNumber, accountID);
            result.add(notificationList);

            return result;
        }

        return null;
    }

    @Override
    public Notification addNewNotification(Notification notification) {

        String date = getCurrentTimeStamp();
        notification.setDate(date);

        notification.setSeen(AppConstant.NOTIFICATION_NOT_SEEN);

        Notification newNotification = notificationRepository.create(notification);

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
}
