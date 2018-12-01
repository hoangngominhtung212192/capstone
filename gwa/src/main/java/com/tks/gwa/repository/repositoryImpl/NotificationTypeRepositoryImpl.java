package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Notificationtype;
import com.tks.gwa.repository.NotificationTypeRepository;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationTypeRepositoryImpl extends GenericRepositoryImpl<Notificationtype, Integer> implements NotificationTypeRepository {

    public NotificationTypeRepositoryImpl() {
        super(Notificationtype.class);
    }
}
