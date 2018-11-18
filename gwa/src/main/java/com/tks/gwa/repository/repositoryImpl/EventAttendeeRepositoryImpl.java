package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.entity.Event;
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
        System.out.println("earching by id "+eventid);
        String sql = "FROM " + Eventattendee.class.getName()+ " WHERE eventid = :eventid AND rating > 0";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("eventid", eventid );
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

    @Override
    public List<Eventattendee> getAttendeeByAccountID(int accountID, String sorttype, int pageNum) {
        String sortSql = "";
        if (sorttype.equalsIgnoreCase("asc")){
            sortSql = " ORDER BY date ASC";
        } else {
            sortSql = " ORDER BY date DESC";
        }
        String sql = "FROM " + Eventattendee.class.getName()+ " WHERE accountID = :accountID" + sortSql;
        Query query = this.entityManager.createQuery(sql);
        query.setParameter("accountID", accountID);
        query.setFirstResult((pageNum-1) * AppConstant.EVENT_MAX_RECORD_PER_PAGE);
        query.setMaxResults(AppConstant.EVENT_MAX_RECORD_PER_PAGE);
        List<Eventattendee> listres = null;

        try {
            listres = query.getResultList();
            System.out.println("user attended "+listres.size() +" results");
        } catch (NoResultException e) {
            System.out.println("no eventattendee found");
            return listres;
        }
        return listres;
    }

}

