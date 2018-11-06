package com.tks.gwa.repository;

import com.tks.gwa.entity.Notification;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends GenericRepository<Notification, Integer> {

    /**
     *
     * @param pageNumber
     * @param accountID
     * @return
     */
    public List<Notification> getListNotificationByAccountID(int pageNumber, int accountID);

    /**
     *
     * @param accountID
     * @return
     */
    public int getCountNotificationByAccountID(int accountID);
}
