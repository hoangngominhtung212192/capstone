package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Event;
import com.tks.gwa.repository.EventRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventRepositoryImpl extends GenericRepositoryImpl<Event, Integer> implements EventRepository {
    @Override
    public List<Event> getListEvent() {
        return null;
    }

    @Override
    public Event addNewEvent(Event event) {
        return null;
    }

    @Override
    public Event updateEvent(Event event) {
        return null;
    }

    @Override
    public boolean deleteEvent(Event event) {
        return false;
    }

    @Override
    public Event getEventByID(Integer id) {
        return null;
    }
}
