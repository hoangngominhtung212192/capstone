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
    public int countSearchAttendeeByEvent(int eventid, String username) {
        String sql = "SELECT COUNT(e.id) FROM " + Eventattendee.class.getName()+" AS e WHERE e.event.id = :eventid AND e.account.username LIKE :username";
        Query query = this.entityManager.createQuery(sql);
//        String evidS = String.valueOf(eventid);
        query.setParameter("eventid", eventid);
        query.setParameter("username", "%"+username+"%");
        long result = 0;
        try{
            result = (long) query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("no attendee foudn");
            return 0;
        }
        System.out.println("count attendee"+result);
        return (int) result;
    }

    @Override
    public int countAttendeeByEvent(int eventid) {
        String sql = "SELECT COUNT(e.id) FROM " + Eventattendee.class.getName()+" AS e WHERE e.event.id = :eventid AND e.rating > 0";
        Query query = this.entityManager.createQuery(sql);
        String evidS = String.valueOf(eventid);
        query.setParameter("eventid", eventid);
        long result = 0;
        try{
            result = (long) query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("no attendee foudn");
            return 0;
        }
        System.out.println("count attendee"+result);
        return (int) result;
    }

    @Override
    public List<Eventattendee> searchRatedAttendeeByEvent(int eventid, int pageNum) {
        System.out.println("searching rated attendee in event by id "+eventid);
        String sql = "FROM " + Eventattendee.class.getName()+ " WHERE eventid = :eventid AND rating > 0";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("eventid", eventid );
        query.setFirstResult((pageNum-1) * 5);
        query.setMaxResults(5);

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
    public List<Eventattendee> searchAttendeeByEvent(Integer eventid) {
        System.out.println("searching attendee in event by id "+eventid);
        String sql = "FROM " + Eventattendee.class.getName()+ " WHERE eventid = :eventid";

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
    public List<Eventattendee> searchAttendeeByEventWithPage(Integer eventid, String username, int pageNum) {
        String sorttype = "desc";
        String sortSql = "";
        if (sorttype.equalsIgnoreCase("asc")){
            sortSql = " ORDER BY date ASC";
        } else {
            sortSql = " ORDER BY date DESC";
        }
        String sql = "FROM " + Eventattendee.class.getName()+ " AS e WHERE e.event.id = :eventid AND e.account.username LIKE :username" + sortSql;
        Query query = this.entityManager.createQuery(sql);
        query.setParameter("eventid", eventid);
        query.setParameter("username", "%"+username+"%");
        query.setFirstResult((pageNum-1) * AppConstant.EVENT_MAX_RECORD_PER_PAGE);
        query.setMaxResults(AppConstant.EVENT_MAX_RECORD_PER_PAGE);
        List<Eventattendee> listres = null;

        try {
            listres = query.getResultList();
        } catch (NoResultException e) {
            System.out.println("no eventattendee found");
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
    public int countAttendeeByAccoutnID(int accountID) {
        String sql = "SELECT COUNT(e) FROM " + Eventattendee.class.getName()+" AS e WHERE 'accountID' = :accountID";
        Query query = this.entityManager.createQuery(sql);
        String accSt = String.valueOf(accountID);
        query.setParameter("accountID", accSt);
        long result = 0;
        try{
            result = (long) query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("no event foudn");
            return 0;
        }

        return (int) result;
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

