package com.tks.gwa.controller;

import com.tks.gwa.entity.Event;
import com.tks.gwa.entity.Eventattendee;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/event")
public interface EventWS {
    @RequestMapping(value = "/createEvent", method = RequestMethod.POST)
    ResponseEntity<Event> createEvent(@RequestBody Event event);

    @RequestMapping(value = "/editEvent", method = RequestMethod.POST)
    ResponseEntity<Event> editEvent(@RequestBody Event event);

    @RequestMapping(value = "/deleteEvent", method = RequestMethod.POST)
    ResponseEntity<String> deleteEvent(@RequestParam int eventid);

    @RequestMapping(value = "/getAllEvent", method = RequestMethod.POST)
    ResponseEntity<List<Event>> getAllEvent();

    @RequestMapping(value = "/searchEvent", method = RequestMethod.POST)
    ResponseEntity<List<Event>> searchEvent(@RequestParam String title);

    @RequestMapping(value = "/getEvent", method = RequestMethod.POST)
    ResponseEntity<Event> getEvent(@RequestBody Integer eventid);

    @RequestMapping(value = "/getRemainingSlots", method = RequestMethod.POST)
    ResponseEntity<Integer> getRemainingSlots(@RequestBody Integer eventid);

    @RequestMapping(value = "/registerEvent", method = RequestMethod.POST)
    ResponseEntity<String> registerEvent(@RequestParam int eventid, @RequestParam int userid, @RequestParam int amount, @RequestParam String date);

    @RequestMapping(value = "/feedbackEvent", method = RequestMethod.POST)
    ResponseEntity<Event> feedbackEvent(@RequestParam int eventid, @RequestParam int attendeeid, @RequestParam int rating, @RequestParam String feedback);

    @RequestMapping(value = "/getEventRating", method = RequestMethod.POST)
    ResponseEntity<Integer> getEventRating(@RequestParam int eventid);

    @RequestMapping(value = "/checkMatchingLocaNtime", method = RequestMethod.POST)
    ResponseEntity<List<Event>> checkMatchingLocaNtime(@RequestParam String location, @RequestParam String staDate, @RequestParam String endDate) throws ParseException;

    @RequestMapping(value = "/getAttendeeInEvent", method = RequestMethod.POST)
    ResponseEntity<Eventattendee> getAttendeeInEvent(@RequestParam int userid, @RequestParam int eventid);


}
