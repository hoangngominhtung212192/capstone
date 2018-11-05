package com.tks.gwa.service.serviceImpl;

import com.tks.gwa.entity.Eventattendee;
import com.tks.gwa.repository.EventAttendeeRepository;
import com.tks.gwa.service.EventAttendeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class EventattendeeServiceImpl implements EventAttendeeService {

    @Autowired
    private EventAttendeeRepository attendeeRepository;

    @Override
    public List<Eventattendee> getAllAttendee() {
        return attendeeRepository.getAllAttendee();
    }

    @Override
    public Eventattendee addNewAttendee(Eventattendee attendee) {
        return attendeeRepository.addNewAttendee(attendee);
    }

    @Override
    public Eventattendee updateAttendee(Eventattendee attendee) {
        return attendeeRepository.updateAttendee(attendee);
    }

    @Override
    public void deleteAttendee(Eventattendee attendee) {
        attendeeRepository.deleteAttendee(attendee);
    }

    @Override
    public List<Eventattendee> searchAttendeeByEvent(Integer eventid) {
        return attendeeRepository.searchAttendeeByEvent(eventid);
    }

    @Override
    public Eventattendee getAttendeeInEvent(Integer userid, Integer eventid) {
        return attendeeRepository.getAttendeeInEvent(userid, eventid);
    }

    @Override
    public Eventattendee getAttendeeByID(int id) {
        return attendeeRepository.getAttendee(id);
    }
}
