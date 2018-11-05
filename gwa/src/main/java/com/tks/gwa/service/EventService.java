package com.tks.gwa.service;

import com.tks.gwa.entity.Event;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public interface EventService {
    List<Event> getAllEvent();
    Event createEvent(Event event);
    Event updateEvent(Event event);
    Event getEvent(Integer id);
    boolean deleteEvent(Integer id);
    List<Event> checkForMatchingLocation(String location);
    List<Event> checkForMatchingLocationNTime(String location, String staDate, String endDate ) throws ParseException;
}
