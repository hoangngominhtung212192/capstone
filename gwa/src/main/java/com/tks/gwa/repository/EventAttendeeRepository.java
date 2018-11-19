package com.tks.gwa.repository;

import com.tks.gwa.entity.Event;
import com.tks.gwa.entity.Eventattendee;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public interface EventAttendeeRepository extends GenericRepository<Eventattendee, Integer> {
    List<Eventattendee> getAllAttendee();
    Eventattendee addNewAttendee(Eventattendee attendee);
    Eventattendee updateAttendee(Eventattendee attendee);
    void deleteAttendee(Eventattendee attendee);
    List<Eventattendee> searchAttendeeByEvent(Integer eventid);
    Eventattendee getAttendeeInEvent(Integer userid, Integer eventid) throws NoResultException;
    Eventattendee getAttendee(int id);
    int countAttendeeByAccoutnID(int accountID);
    List<Eventattendee> getAttendeeByAccountID(int accountID, String sorttype, int pageNum);
}
