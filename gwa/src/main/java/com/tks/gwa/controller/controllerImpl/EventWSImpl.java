package com.tks.gwa.controller.controllerImpl;

import com.tks.gwa.controller.EventWS;
import com.tks.gwa.dto.UploadFileResponse;
import com.tks.gwa.entity.Account;
import com.tks.gwa.entity.Event;
import com.tks.gwa.entity.Eventattendee;
import com.tks.gwa.service.EventAttendeeService;
import com.tks.gwa.service.EventService;
import com.tks.gwa.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class EventWSImpl implements EventWS {
    @Autowired
    private EventService eventService;

    @Autowired
    private EventAttendeeService attendeeService;

    @Autowired
    private FileControllerWsImpl fileControllerWs;

    @Override
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        event.setNumberOfAttendee(0);
        event.setNumberOfStars(0);
        event.setNumberOfRating(0);
        Event newEvent = eventService.createEvent(event);


        return new ResponseEntity<>(newEvent, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Event> editEvent(@RequestBody Event event) {
        Event updatedEvent = eventService.updateEvent(event);
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteEvent(@RequestParam int eventid) {
        eventService.deleteEvent(eventid);
        return new ResponseEntity<>("Deleted successfully.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Event>> getAllEvent() {
        System.out.println("gettin evn");
        List<Event> listev = eventService.getAllEvent();
        System.out.println("got even");
        return new ResponseEntity<>(listev, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Event>> searchEvent(String title) {

        return null;
    }

    @Override
    public ResponseEntity<Event> getEvent(@RequestBody Integer eventid) {

        return new ResponseEntity<>(eventService.getEvent(eventid), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Integer> getRemainingSlots(Integer eventid) {
        Event ee = eventService.getEvent(eventid);
        Integer rmnTk = ee.getMaxAttendee() - ee.getNumberOfAttendee();

        return new ResponseEntity<>(rmnTk, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> registerEvent(@RequestParam int eventid, @RequestParam int userid, @RequestParam int amount, @RequestParam String date) {
        Account testacc = new Account();
        testacc.setId(userid);

        Event nev = eventService.getEvent(eventid);
        nev.setNumberOfAttendee(nev.getNumberOfAttendee()+amount);
        Eventattendee attet = new Eventattendee();
        attet.setAccount(testacc);
        attet.setEvent(nev);
        attet.setDate(date.toString());
        attet.setAmount(amount);
        //
        attendeeService.addNewAttendee(attet);
        Event eee = attet.getEvent();
        int curAttNum = eee.getNumberOfAttendee();
        eee.setNumberOfAttendee(curAttNum+1);
        return new ResponseEntity<>("Register successfully.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Event> feedbackEvent(@RequestParam int eventid,@RequestParam int attendeeid, @RequestParam int rating, @RequestParam String feedback) {
        //update attendee
        System.out.println("attendee id+ "+attendeeid);
        Eventattendee evatt = attendeeService.getAttendeeByID(attendeeid);
        System.out.println("current attendee rating: " + evatt.getRating());
        System.out.println("rating: "+rating);
        System.out.println("feedback: "+feedback);
        evatt.setFeedback(feedback);
        evatt.setRating(rating);

        attendeeService.updateAttendee(evatt);
        //update rating in event
        //ratings: times rated
        //stars: total stars got

        Event ev = eventService.getEvent(eventid);
        int curStars = ev.getNumberOfStars();
        int rateStars = rating;
        int curNumRating = ev.getNumberOfRating();
        ev.setNumberOfRating(curNumRating+1);
        ev.setNumberOfStars(curStars + rateStars);
        eventService.updateEvent(ev);

        return new ResponseEntity<>(ev, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Integer> getEventRating(@RequestParam int eventid) {
        Event getevent = eventService.getEvent(eventid);
        int numstars = getevent.getNumberOfStars();
        int numrate = getevent.getNumberOfRating();
        int finalrating = Math.floorDiv(numrate, numstars);
        return new ResponseEntity<>(finalrating, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Event>> checkMatchingLocaNtime(@RequestParam String location,@RequestParam String staDate,@RequestParam String endDate) throws ParseException {
        System.out.println("WS got location: "+location);
        List<Event> matchinEv = eventService.checkForMatchingLocationNTime(location, staDate, endDate);

        return new ResponseEntity<>(matchinEv, HttpStatus.OK);


    }

    @Override
    public ResponseEntity<List<Event>> checkMatchingLocaNtimeExcept(@RequestParam int id,
                                                                    @RequestParam String location,
                                                                    @RequestParam String staDate,
                                                                    @RequestParam String endDate) throws ParseException {
        List<Event> matchinEv = eventService.checkForMatchingLocationNTimeExcept(id, location, staDate, endDate);

        return new ResponseEntity<>(matchinEv, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Eventattendee> getAttendeeInEvent(@RequestParam int userid,@RequestParam  int eventid) {
        System.out.println("WS userid: "+userid);
        System.out.println("WS eventid: "+eventid);
        Eventattendee aa = attendeeService.getAttendeeInEvent(userid, eventid);
        if (aa!=null){
            System.out.println(userid +" is attendee");
            return new ResponseEntity<>(aa, HttpStatus.OK);
        } else {
            System.out.println(userid +" is not attendee");
            return new ResponseEntity<>(aa, HttpStatus.NOT_FOUND);

        }

    }

    @Autowired
    private FileUploadService fileUploadService;

    @Override
    public ResponseEntity<Event> updateEventImage(MultipartFile photoBtn, int id) {
        String filename = fileUploadService.storeFile(photoBtn);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/").path(filename).toUriString();

        Event event = eventService.getEvent(id);
        if (event != null) {
            event.setThumbImage(fileDownloadUri);
            Event newEvent = eventService.updateEvent(event);

            if (newEvent != null) {
                return new ResponseEntity<Event>(newEvent, HttpStatus.OK);
            }
        }

        return new ResponseEntity<Event>(event, HttpStatus.valueOf(400));
    }


    @Override
    public ResponseEntity<List<Eventattendee>> getRatedAttendeeInEvent(@RequestParam Integer eventid) {
        System.out.println("earching by id "+eventid);
        List<Eventattendee> alist = attendeeService.searchAttendeeByEvent(eventid);
        System.out.println("WS list size: "+alist.size());
        System.out.println("result list size: "+alist.size());
        return new ResponseEntity<>(alist, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Object>> getEventByStatusAndPage(@RequestParam String status,
                                                                @RequestParam String sorttype,
                                                                @RequestParam int pageNum) {
        List<Object> result = eventService.getEventWithSortAndPageByStatus(status, sorttype, pageNum);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Object>> searchEventByStatusAndPage(@RequestParam String title,
                                                                @RequestParam String status,
                                                                @RequestParam String sorttype,
                                                                @RequestParam int pageNum) {
        List<Object> result = eventService.searchEventWithSortAndPageByStatus(title, status, sorttype, pageNum);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Object>> getMyListEvent(@RequestParam Integer accountID,
                                                      @RequestParam String sorttype,
                                                      @RequestParam int pageNum) {
        List<Object> firstresult = attendeeService.getAttendeeByAccountID(accountID, sorttype, pageNum);
        List<Object> finalresult = new ArrayList<>();

        finalresult.add(0, firstresult.get(0));

        List<Eventattendee> attendees = (List<Eventattendee>) firstresult.get(1);
        List<Event> events = new ArrayList<Event>();
        for (int i = 0; i < attendees.size(); i++) {
            events.add(attendees.get(i).getEvent());
        }
        finalresult.add(1, events);

        return new ResponseEntity<>(finalresult, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Object>> getNearbyEvent(@RequestParam String location,
                                                       @RequestParam long range,
                                                       @RequestParam String sorttype,
                                                       @RequestParam int pageNum) {
        List<Object> result = eventService.getNearEventByLocation(location, range, sorttype, pageNum);


        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
