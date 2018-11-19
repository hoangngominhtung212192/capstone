package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.entity.Event;
import com.tks.gwa.repository.EventRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.awt.print.Pageable;
import java.util.List;

@Repository
public class EventRepositoryImpl extends GenericRepositoryImpl<Event, Integer> implements EventRepository {
    public EventRepositoryImpl() {
        super(Event.class);
    }

    @Override
    public List<Event> getListEvent() {
        return this.getAll();
    }

    @Override
    public List<Event> getListEventPageable(Pageable pageable) {
        return this.getAll();
    }

    @Override
    public Event addNewEvent(Event event) {
        System.out.println("repo content is: " + event.getContent());
        System.out.println("repo title is: " + event.getTitle());
        Event newevent = this.create(event);
        return newevent;
    }

    @Override
    public Event updateEvent(Event event) {
        Event updatedEvent = this.update(event);
        return updatedEvent;
    }

    @Override
    public boolean deleteEvent(Event event) {
        this.delete(event);
        return true;
    }

    @Override
    public Event getEventByID(Integer id) {
        return this.read(id);
    }

    @Override
    public List<Event> getEventByStatus(String status) {
        String sql = "FROM " + Event.class.getName()+ " WHERE status = :status";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("status", "%" + status + "%");
        List<Event> listres = null;

        try {
            listres = query.getResultList();
        } catch (NoResultException e) {
            System.out.println("no event with such status");
            return listres;
        }


        return listres;
    }

    @Override
    public List<Event> getAvailableEvent() {
        System.out.println("repo: getting available event");
        String sql = "FROM " + Event.class.getName()+ " WHERE status = :status OR status = :statusnd ";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("status", "Active");
        query.setParameter("statusnd", "Inactive");
        List<Event> listres = null;

        try {
            listres = query.getResultList();
            System.out.println("got "+listres.size() +" results");
        } catch (NoResultException e) {
            System.out.println("no event with such status");
            return listres;
        }

        return listres;
    }

    @Override
    public List<Event> getAvailableEventExceptID(int id) {
        System.out.println("repo: getting available event except id "+id);
        String sql = "FROM " + Event.class.getName()+ " WHERE NOT id = :id AND status = :status OR status = :statusnd ";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("status", "Active");
        query.setParameter("statusnd", "Inactive");
        query.setParameter("id", id);
        List<Event> listres = null;

        try {
            listres = query.getResultList();
            System.out.println("got "+listres.size() +" results");
        } catch (NoResultException e) {
            System.out.println("no event with such status");
            return listres;
        }

        return listres;
    }

    @Override
    public List<Event> searchEventByStatusAsc(String status, int limit, int offset) {
        String sql = "FROM " + Event.class.getName()+ " WHERE status = :status ORDER BY startDate";
        Query query = this.entityManager.createQuery(sql);
        query.setParameter("status", status);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        List<Event> listres = null;

        try {
            listres = query.getResultList();
            System.out.println("got "+listres.size() +" results");
        } catch (NoResultException e) {
            System.out.println("no event found");
            return listres;
        }
        return listres;
    }

    @Override
    public List<Event> searchEventByStatusDesc(String status, int limit, int offset) {
        String sql = "FROM " + Event.class.getName()+ " WHERE status = :status ORDER BY startDate";
        Query query = this.entityManager.createQuery(sql);
        query.setParameter("status", status);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        List<Event> listres = null;

        try {
            listres = query.getResultList();
            System.out.println("got "+listres.size() +" results");
        } catch (NoResultException e) {
            System.out.println("no event found");
            return listres;
        }
        return listres;
    }

    @Override
    public int countEventByStatus(String status) {
        String sql = "SELECT COUNT(e) FROM " + Event.class.getName()+" AS e WHERE e.status =:status";
        Query query = this.entityManager.createQuery(sql);
        query.setParameter("status", status);
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
    public int countEventBySearchStatus(String title, String status) {
        String sql = "SELECT COUNT(e) FROM " + Event.class.getName()+" AS e WHERE e.status =:status AND e.title LIKE :title";
        Query query = this.entityManager.createQuery(sql);
        query.setParameter("status", status);
        query.setParameter("title", "%"+title+"%");
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
    public List<Event> getEventByStatusAndSort(String status, String sorttype, int pageNum) {
        String sortSql = "";
        if (sorttype.equalsIgnoreCase("asc")){
            sortSql = " ORDER BY startDate ASC";
        } else {
            sortSql = " ORDER BY startDate DESC";
        }
        String sql = "FROM " + Event.class.getName()+ " WHERE status = :status" + sortSql;
        Query query = this.entityManager.createQuery(sql);
        query.setParameter("status", status);
        query.setFirstResult((pageNum-1) * AppConstant.EVENT_MAX_RECORD_PER_PAGE);
        query.setMaxResults(AppConstant.EVENT_MAX_RECORD_PER_PAGE);
        List<Event> listres = null;

        try {
            listres = query.getResultList();
            System.out.println("got "+listres.size() +" results");
        } catch (NoResultException e) {
            System.out.println("no event found");
            return listres;
        }
        return listres;
    }

    @Override
    public List<Event> searchEventByStatusAndSort(String title, String status, String sorttype, int pageNum) {
        String sortSql = "";
        if (sorttype.equalsIgnoreCase("asc")){
            sortSql = " ORDER BY startDate ASC";
        } else {
            sortSql = " ORDER BY startDate DESC";
        }
        String sql = "FROM " + Event.class.getName()+ " WHERE title LIKE :title AND status = :status" + sortSql;
        Query query = this.entityManager.createQuery(sql);
        query.setParameter("title", "%"+title+"%");
        query.setParameter("status", status);
        query.setFirstResult((pageNum-1) * AppConstant.EVENT_MAX_RECORD_PER_PAGE);
        query.setMaxResults(AppConstant.EVENT_MAX_RECORD_PER_PAGE);
        List<Event> listres = null;

        try {
            listres = query.getResultList();
            System.out.println("got "+listres.size() +" results");
        } catch (NoResultException e) {
            System.out.println("no event found");
            return listres;
        }
        return listres;
    }

    @Override
    public int updateEventStatus(int id) {
        String sql = "UPDATE " + Event.class.getName()+ " e SET e.status = 'Inactive' WHERE e.id = :id";
        Query query = this.entityManager.createQuery(sql);
        query.setParameter("id", id);
        int result = query.executeUpdate();
        return result;
    }
}
