package com.tks.gwa.service;

import com.tks.gwa.entity.Eventattendee;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventAttendeeService {
    List<Eventattendee> getAllAttendee();
    Eventattendee addNewAttendee(Eventattendee attendee);
    Eventattendee updateAttendee(Eventattendee attendee);
    void deleteAttendee(Eventattendee attendee);
    List<Eventattendee> searchAttendeeByEvent(Integer eventid);
    Eventattendee getAttendeeInEvent(Integer userid, Integer eventid);
    Eventattendee getAttendeeByID(int id);
}
