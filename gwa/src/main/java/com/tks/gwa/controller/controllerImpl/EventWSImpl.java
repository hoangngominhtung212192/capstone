package com.tks.gwa.controller.controllerImpl;

import com.tks.gwa.controller.EventWS;
import com.tks.gwa.entity.Account;
import com.tks.gwa.entity.Event;
import com.tks.gwa.entity.Eventattendee;
import com.tks.gwa.service.EventAttendeeService;
import com.tks.gwa.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
public class EventWSImpl implements EventWS {
    @Autowired
    private EventService eventService;

    @Autowired
    private EventAttendeeService attendeeService;


    @Override
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        event.setNumberOfAttendee(0);
        event.setNumberOfStars(0);
        event.setNumberOfRating(0);
        Event newEvent = eventService.createEvent(event);
        System.out.println("content: "+event.getContent());

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
        testacc.setId(1);

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
    public ResponseEntity<Eventattendee> getAttendeeInEvent(@RequestParam int userid,@RequestParam  int eventid) {
        System.out.println("WS userid: "+userid);
        System.out.println("WS eventid: "+eventid);
        Eventattendee aa = attendeeService.getAttendeeInEvent(userid, eventid);
        if (aa!=null){
            return new ResponseEntity<>(aa, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(aa, HttpStatus.NOT_FOUND);

        }

    }

}
