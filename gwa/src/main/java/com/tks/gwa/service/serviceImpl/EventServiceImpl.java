package com.tks.gwa.service.serviceImpl;

import com.google.maps.model.LatLng;
import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.entity.*;
import com.tks.gwa.repository.EventAttendeeRepository;
import com.tks.gwa.repository.EventRepository;
import com.tks.gwa.repository.NotificationRepository;
import com.tks.gwa.service.EventService;
import com.tks.gwa.service.NotificationService;
import com.tks.gwa.utils.DatetimeHelper;
import com.tks.gwa.utils.GoogleMapHelper;
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

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EventAttendeeRepository eventAttendeeRepository;

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

    @Override
    public List<Object> getNearEventByLocation(String location, long range, String sorttype, int pageNum) {

        List<Object> result = new ArrayList<>();

        List<Event> eventList = eventRepository.searchEventByStatusAndSort("", "Active", sorttype, pageNum);
        List<Event> resultEList = new ArrayList<>();
        System.out.println("my location: "+location);
        String address = location.split("@")[0];
        System.out.println("my address: "+address);
        Double lat = Double.valueOf(location.split("@")[1]);
        Double lng = Double.valueOf(location.split("@")[2]);
        LatLng from = new LatLng();
        from.lat = lat;
        from.lng = lng;
//        LatLng from = GoogleMapHelper.getLatLngFromAddress(location);
        for (int i = 0; i < eventList.size(); i++) {
            Event event = eventList.get(i);
            System.out.println("checking distance of event id"+event.getId());
//            LatLng to = GoogleMapHelper.getLatLngFromAddress(event.getLocation());
            LatLng to = new LatLng();
//            Double comparelat = Double.valueOf(event.getLocation().split("@")[1]);
            try {
                to.lat = Double.valueOf(event.getLocation().split("@")[1]);
                to.lng = Double.valueOf(event.getLocation().split("@")[2]);
            } catch (ArrayIndexOutOfBoundsException e){
                continue;
            }

            if (to.equals(null)){
                continue;
            }

            long distance = GoogleMapHelper.calculateDistanceBetweenTwoPoint(from,to);
            System.out.println("searching nearby event, foudn event with distance: "+distance);
            if (distance <= range){
                resultEList.add(eventList.get(i));
            }
        }
        int totalRecord = resultEList.size();
        int totalPage = totalRecord / AppConstant.EVENT_MAX_RECORD_PER_PAGE;
        if (totalRecord % AppConstant.EVENT_MAX_RECORD_PER_PAGE > 0){
            totalPage +=1;
        }
        result.add(0, totalPage);
        result.add(1, resultEList);

        return result;
    }

    @Override
    public void checkNUpdateEventStatus() {
        List<Event> listAll = eventRepository.getAll();
        System.out.println("Checking for outdated events");
        Date date = new Date();
        for (int i = 0; i < listAll.size(); i++) {
            Event curE = listAll.get(i);
            try {
                Date evDate = new SimpleDateFormat("yyyy-MM-dd").parse(curE.getRegDateEnd());
                if (date.compareTo(evDate)>0 && (curE.getNumberOfAttendee() < curE.getMinAttendee()) ){
                    //got affected event
                    System.out.println("Event id "+curE.getId()+ "found with lower current attendee amount than min attendee, updating");
                    eventRepository.updateEventStatus(curE.getId());
                    //list of attendee of this event
                    List<Eventattendee> listatt = eventAttendeeRepository.searchAttendeeByEvent(curE.getId());
                        for (int j = 0; j < listatt.size(); j++) {
                            Eventattendee attendee = listatt.get(j);
                            System.out.println("Sending notification to user id "+listatt.get(j).getAccount().getId());

                            Notification notification = new Notification();
                            notification.setSeen(AppConstant.NOTIFICATION_NOT_SEEN);
                            notification.setDate(DatetimeHelper.dateConvertToString(date));
                            notification.setDescription("An event you signed up for was cancelled.");
                            notification.setAccount(attendee.getAccount());

                            Notificationtype notificationtype = new Notificationtype();
                            notificationtype.setId(7);
                            notificationtype.setName("Event");

                            notification.setNotificationtype(notificationtype);
                            notification.setObjectID(curE.getId());

                            notificationRepository.create(notification);
//                            notificationService.addNewNotification(notification);
                        }
                }
            } catch (ParseException e) {
                e.printStackTrace();
                continue;
            }
        }
    }
}
