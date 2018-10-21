package com.tks.gwa.repository;

import com.tks.gwa.entity.Event;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends GenericRepository<Event, Integer> {
    List<Event> getListEvent();
    Event addNewEvent(Event event);
    Event updateEvent(Event event);
    boolean deleteEvent(Event event);
    Event getEventByID(Integer id);
}
