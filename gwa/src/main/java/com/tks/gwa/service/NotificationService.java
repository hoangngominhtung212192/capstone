package com.tks.gwa.service;

import com.tks.gwa.dto.NotificationDTO;
import com.tks.gwa.entity.Notification;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface NotificationService {

    /**
     *
     * @param pageNumber
     * @param accountID
     * @return NotificationDTO
     */
    public NotificationDTO getNotificationByAccountID(int pageNumber, int accountID);

    /**
     *
     * @param notification
     * @return
     */
    public Notification addNewNotification(Notification notification);

    /**
     *
     * @param notificationID
     */
    public void updateNotificationSeen(int notificationID);
}
