package com.tks.gwa.repository;

import com.tks.gwa.entity.Event;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface EventRepository extends GenericRepository<Event, Integer> {
    List<Event> getListEvent();
    List<Event> getListEventPageable(Pageable pageable);
    Event addNewEvent(Event event);
    Event updateEvent(Event event);
    boolean deleteEvent(Event event);
    Event getEventByID(Integer id);
    List<Event> getEventByStatus(String status);
    List<Event> getAvailableEvent();
    List<Event> getAvailableEventExceptID(int id);
    List<Event> searchEventByStatusAsc(String status, int limit, int offset);
    List<Event> searchEventByStatusDesc(String status, int limit, int offset);
    int countEventByStatus(String status);
    int countEventBySearchStatus(String title, String status);
    List<Event> getEventByStatusAndSort(String status, String sorttype, int pageNum);
    List<Event> searchEventByStatusAndSort(String title, String status, String sorttype, int pageNum);
    int updateEventStatus(int id);
}
