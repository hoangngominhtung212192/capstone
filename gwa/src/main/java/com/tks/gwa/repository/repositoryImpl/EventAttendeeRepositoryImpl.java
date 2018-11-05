package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Eventattendee;
import com.tks.gwa.repository.EventAttendeeRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
public class EventAttendeeRepositoryImpl extends GenericRepositoryImpl<Eventattendee, Integer> implements EventAttendeeRepository {

    public EventAttendeeRepositoryImpl() {
        super(Eventattendee.class);
    }

    @Override
    public List<Eventattendee> getAllAttendee() {
        return this.getAll();
    }

    @Override
    public Eventattendee addNewAttendee(Eventattendee attendee) {
        return this.create(attendee);
    }

    @Override
    public Eventattendee updateAttendee(Eventattendee attendee) {
        return this.update(attendee);
    }

    @Override
    public void deleteAttendee(Eventattendee attendee) {
        this.delete(attendee);
    }

    @Override
    public List<Eventattendee> searchAttendeeByEvent(Integer eventid) {
        String sql = "FROM " + Eventattendee.class.getName()+ " WHERE eventid = :eventid";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("eventid", "%" + eventid + "%");
        List<Eventattendee> listres = null;

        try {
            listres = query.getResultList();
        } catch (NoResultException e) {
            System.out.println("search FAILED!!");
            return listres;
        }

        return listres;
    }

    @Override
    public Eventattendee getAttendeeInEvent(Integer userid, Integer eventid) throws NoResultException{
        System.out.println("Repo userid: "+userid);
        System.out.println("Repo eventid: "+eventid);

        String sql = "FROM " + Eventattendee.class.getName()+ " WHERE accountID = :userid AND eventID = :eventid";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("userid",  userid);
        query.setParameter("eventid", eventid);
        Eventattendee attendee = null;
        try {
            attendee = (Eventattendee) query.getSingleResult();
        } catch (NoResultException e) {
            return attendee;
        }
        return attendee;
    }

    @Override
    public Eventattendee getAttendee(int id) {
        System.out.println("geting attende id: "+id);
        return this.read(id);
    }
}

