package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Event;
import com.tks.gwa.repository.EventRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
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
        String sql = "FROM " + Event.class.getName()+ " WHERE status = :status OR status = :statusnd";

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
}
