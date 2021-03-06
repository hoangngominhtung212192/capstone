package com.tks.gwa.service.serviceImpl;

import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.entity.Event;
import com.tks.gwa.entity.Eventattendee;
import com.tks.gwa.repository.EventAttendeeRepository;
import com.tks.gwa.service.EventAttendeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public List<Object> searchRatedAttendeeByEvent(int eventid, int pageNum) {
        int totalRecord = attendeeRepository.countAttendeeByEvent(eventid);
        int totalPage = totalRecord / 5;
        if (totalRecord % 5 > 0){
            totalPage +=1;
        }
        List<Object> result = new ArrayList<>();
        result.add(0, totalPage);
        System.out.println("totalpage "+totalPage);
        List<Eventattendee> eventattendeeList = attendeeRepository.searchRatedAttendeeByEvent(eventid, pageNum);
        result.add(1, eventattendeeList);
        return result;
    }

    @Override
    public List<Object> searchAttendeeByEvent(Integer eventid, String username, int pageNum) {
        int totalRecord = attendeeRepository.countSearchAttendeeByEvent(eventid, username);
        int totalPage = totalRecord / 10;
        if (totalRecord % 10 > 0){
            totalPage +=1;
        }
        List<Object> result = new ArrayList<>();
        result.add(0, totalPage);
        System.out.println("totalpage "+totalPage);
        List<Eventattendee> eventattendeeList = attendeeRepository.searchAttendeeByEventWithPage(eventid, username, pageNum);
        result.add(1, eventattendeeList);
        return result;
    }

    @Override
    public Eventattendee getAttendeeInEvent(Integer userid, Integer eventid) {
        return attendeeRepository.getAttendeeInEvent(userid, eventid);
    }

    @Override
    public Eventattendee getAttendeeByID(int id) {
        return attendeeRepository.getAttendee(id);
    }


    @Override
    public List<Object> getAttendeeByAccountID(int accountid, String sorttype, int pageNum) {
        List<Eventattendee> attList = attendeeRepository.getAttendeeByAccountID(accountid, sorttype, pageNum);

        int totalRecord = attendeeRepository.countAttendeeByAccoutnID(accountid);
        int totalPage = totalRecord / AppConstant.EVENT_MAX_RECORD_PER_PAGE;
        if (totalRecord % AppConstant.EVENT_MAX_RECORD_PER_PAGE > 0){
            totalPage +=1;
        }

        System.out.println("logged user's events: "+ totalPage);

        List<Object> result = new ArrayList<>();
        result.add(totalPage);
        result.add(attList);

        return result;
    }
}
