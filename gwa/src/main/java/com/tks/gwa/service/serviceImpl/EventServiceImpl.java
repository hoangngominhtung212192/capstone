package com.tks.gwa.service.serviceImpl;

import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.entity.Event;
import com.tks.gwa.entity.Eventattendee;
import com.tks.gwa.repository.EventAttendeeRepository;
import com.tks.gwa.repository.EventRepository;
import com.tks.gwa.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;


    @Override
    public List<Event> getAllEvent() {
        return eventRepository.getListEvent();
    }

    @Override
    public Event createEvent(Event event) {
        return eventRepository.addNewEvent(event);
    }

    @Override
    public Event updateEvent(Event event) {
        return eventRepository.updateEvent(event);
    }

    @Override
    public Event getEvent(Integer id) {
        return eventRepository.getEventByID(id);
    }

    @Override
    public boolean deleteEvent(Integer id) {
        Event dEvent = getEvent(id);
        return eventRepository.deleteEvent(dEvent);
    }

    @Override
    public List<Event> checkForMatchingLocation(String location) {
        System.out.println("comparin from: "+location);
        List<Event> availEventlist = eventRepository.getAvailableEvent();
        System.out.println("gettin available event");
        if (availEventlist.isEmpty()){
            System.out.println("cannot get available event");
        }
        ArrayList<Event> matchingEventlist = new ArrayList<Event>();
        for (int i = 0; i < availEventlist.size(); i++) {
            System.out.println("ii:" + i);
            if (location.equalsIgnoreCase(availEventlist.get(i).getLocation() ) ) {
                System.out.println("adding "+availEventlist.get(i).getLocation());
                matchingEventlist.add(availEventlist.get(i));
            }
        }
        return matchingEventlist;
    }

    @Override
    public List<Event> checkForMatchingLocationExceptID(int id, String location) {
        System.out.println("comparin from: "+location);
        List<Event> availEventlist = eventRepository.getAvailableEventExceptID(id);
        System.out.println("gettin available event");
        if (availEventlist.isEmpty()){
            System.out.println("cannot get available event");
        }
        ArrayList<Event> matchingEventlist = new ArrayList<Event>();
        for (int i = 0; i < availEventlist.size(); i++) {
            System.out.println("ii:" + i);
            if (location.equalsIgnoreCase(availEventlist.get(i).getLocation() ) ) {
                System.out.println("adding "+availEventlist.get(i).getLocation());
                matchingEventlist.add(availEventlist.get(i));
            }
        }
        return matchingEventlist;
    }

    @Override
    public List<Event> checkForMatchingLocationNTime(String location, String staDate, String endDate) throws ParseException {
        Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(staDate);
        Date date2=new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

        List<Event> matchingLocationEv = checkForMatchingLocation(location);
        System.out.println("FOUND :" + matchingLocationEv.size() + "evts with matching location");
        ArrayList<Event> matchingTimeEvList = new ArrayList<Event>();
        if (matchingLocationEv.isEmpty()){
            return null;
        } else{

            for (int i = 0; i < matchingLocationEv.size(); i++) {
                Date Mdate1=new SimpleDateFormat("yyyy-MM-dd").parse(matchingLocationEv.get(i).getStartDate());
                Date Mdate2=new SimpleDateFormat("yyyy-MM-dd").parse(matchingLocationEv.get(i).getEndDate());

                if ((date1.compareTo(Mdate1)>=0 && date1.compareTo(Mdate2)<=0) || (date2.compareTo(Mdate1)>=0 && date2.compareTo(Mdate2)<=0) ){
                    System.out.println("date1: "+date1+"and mdate1: "+Mdate1+"and mdate2:"+Mdate2);
                    System.out.println("date2: "+date1+"and mdate1: "+Mdate1+"and mdate2:"+Mdate2);
                    matchingTimeEvList.add(matchingLocationEv.get(i));
                }
            }
        }
        return matchingTimeEvList;
    }

    @Override
    public List<Event> checkForMatchingLocationNTimeExcept(int id, String location, String staDate, String endDate) throws ParseException {
        Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(staDate);
        Date date2=new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

        List<Event> matchingLocationEv = checkForMatchingLocationExceptID(id, location);
        System.out.println("FOUND :" + matchingLocationEv.size() + "evts with matching location");
        ArrayList<Event> matchingTimeEvList = new ArrayList<Event>();
        if (matchingLocationEv.isEmpty()){
            return null;
        } else{

            for (int i = 0; i < matchingLocationEv.size(); i++) {
                Date Mdate1=new SimpleDateFormat("yyyy-MM-dd").parse(matchingLocationEv.get(i).getStartDate());
                Date Mdate2=new SimpleDateFormat("yyyy-MM-dd").parse(matchingLocationEv.get(i).getEndDate());

                if ((date1.compareTo(Mdate1)>=0 && date1.compareTo(Mdate2)<=0) || (date2.compareTo(Mdate1)>=0 && date2.compareTo(Mdate2)<=0) ){
                    matchingTimeEvList.add(matchingLocationEv.get(i));
                }
            }
        }
        return matchingTimeEvList;
    }

    @Override
    public List<Object> getEventWithSortAndPageByStatus(String status, String sorttype, int pageNum) {
        int totalRecord = eventRepository.countEventByStatus(status);
        int totalPage = totalRecord / AppConstant.EVENT_MAX_RECORD_PER_PAGE;
        if (totalRecord % AppConstant.EVENT_MAX_RECORD_PER_PAGE > 0){
            totalPage +=1;
        }
        List<Object> result = new ArrayList<>();
        result.add(totalPage);
        List<Event> eventList = eventRepository.getEventByStatusAndSort(status, sorttype, pageNum);
        result.add(eventList);

        return result;
    }

    @Override
    public List<Object> searchEventWithSortAndPageByStatus(String title, String status, String sorttype, int pageNum) {
        int totalRecord = eventRepository.countEventBySearchStatus(title, status);
        int totalPage = totalRecord / AppConstant.EVENT_MAX_RECORD_PER_PAGE;
        if (totalRecord % AppConstant.EVENT_MAX_RECORD_PER_PAGE > 0){
            totalPage +=1;
        }
        List<Object> result = new ArrayList<>();
        result.add(totalPage);
        List<Event> eventList = eventRepository.searchEventByStatusAndSort(title, status, sorttype, pageNum);
        result.add(eventList);

        return result;
    }




}
