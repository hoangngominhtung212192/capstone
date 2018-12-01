package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Notification;
import com.tks.gwa.repository.NotificationRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
public class NotificationRepositoryImpl extends GenericRepositoryImpl<Notification, Integer> implements NotificationRepository {

    public NotificationRepositoryImpl() {
        super(Notification.class);
    }

    @Override
    public List<Notification> getListNotificationByAccountID(int pageNumber, int accountID) {

        String sql = "SELECT n FROM " + Notification.class.getName() + " AS n WHERE n.account.id =:accountID ORDER BY " +
                "n.seen ASC, n.date DESC";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("accountID", accountID);
        query.setFirstResult((pageNumber-1)*10);
        query.setMaxResults(10);

        List<Notification> notificationList = query.getResultList();

        return notificationList;
    }

    @Override
    public int getCountNotificationByAccountID(int accountID) {

        String sql = "SELECT count(n.id) FROM " + Notification.class.getName() + " AS n WHERE n.account.id =:accountID";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("accountID", accountID);

        return (int) (long) query.getSingleResult();
    }

    @Override
    public int getCountNotSeenByAccountID(int accountID) {

        String sql = "SELECT count(n.id) FROM " + Notification.class.getName() + " AS n WHERE n.account.id =:accountID " +
                "AND n.seen=0";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("accountID", accountID);

        return (int) (long) query.getSingleResult();
    }

    @Override
    public Notification addNewNotification(Notification notification) {

        Notification result = this.create(notification);

        return result;
    }

    @Override
    public Notification findByID(int id) {
        String sql = "SELECT n FROM " + Notification.class.getName() + " AS n WHERE n.id =:id";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("id", id);

        Notification result = null;

        try {
            result = (Notification) query.getSingleResult();
        } catch (NoResultException e) {

        }

        return result;
    }
}
