package com.tks.gwa.controller;

import com.tks.gwa.dto.EventSDTO;
import com.tks.gwa.entity.Event;
import com.tks.gwa.entity.Eventattendee;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @RequestMapping(value = "/getEventAlt", method = RequestMethod.GET)
    ResponseEntity<Event> getEventAlt(@RequestParam("id") Integer id);

    @RequestMapping(value = "/getRemainingSlots", method = RequestMethod.POST)
    ResponseEntity<Integer> getRemainingSlots(@RequestBody Integer eventid);

    @RequestMapping(value = "/registerEvent", method = RequestMethod.POST)
    ResponseEntity<String> registerEvent(@RequestParam int eventid,
                                         @RequestParam int userid,
                                         @RequestParam int amount,
                                         @RequestParam String date);

    @RequestMapping(value = "/registerEventAlt", method = RequestMethod.GET)
    ResponseEntity<String> registerEventAlt(@RequestParam("eventid") int eventid,
                                         @RequestParam("userid") int userid,
                                         @RequestParam("amount") int amount,
                                         @RequestParam("date") String date);


    @RequestMapping(value = "/feedbackEvent", method = RequestMethod.POST)
    ResponseEntity<Event> feedbackEvent(@RequestParam int eventid, @RequestParam int attendeeid, @RequestParam int rating, @RequestParam String feedback);

    @RequestMapping(value = "/getEventRating", method = RequestMethod.POST)
    ResponseEntity<Integer> getEventRating(@RequestParam int eventid);

    @RequestMapping(value = "/checkMatchingLocaNtime", method = RequestMethod.POST)
    ResponseEntity<List<Event>> checkMatchingLocaNtime(@RequestParam String location, @RequestParam String staDate, @RequestParam String endDate) throws ParseException;

    @RequestMapping(value = "/checkMatchingLocaNtimeExcept", method = RequestMethod.POST)
    ResponseEntity<List<Event>> checkMatchingLocaNtimeExcept(@RequestParam int id, @RequestParam String location, @RequestParam String staDate, @RequestParam String endDate) throws ParseException;

    @RequestMapping(value = "/getAttendeeInEvent", method = RequestMethod.POST)
    ResponseEntity<Eventattendee> getAttendeeInEvent(@RequestParam int userid, @RequestParam int eventid);

    @RequestMapping(value = "/getAttendeeInEventAlt", method = RequestMethod.GET)
    ResponseEntity<Eventattendee> getAttendeeInEventAlt(@RequestParam("userid") int userid,
                                                  @RequestParam("eventid") int eventid);


    @RequestMapping(value = "/uploadEventImage", method = RequestMethod.POST)
    ResponseEntity<Event> updateEventImage(@RequestParam(value = "photoBtn", required = false) MultipartFile photoBtn,
                                               @RequestParam("id") int id);

    @RequestMapping(value = "/getListAttendeeInEvent", method = RequestMethod.POST)
    ResponseEntity<List<Eventattendee>> getRatedAttendeeInEvent(@RequestParam("eventid") Integer eventid);

    @RequestMapping(value = "/getEventByStatusAndPage", method = RequestMethod.POST)
    ResponseEntity<List<Object>> getEventByStatusAndPage(@RequestParam("status") String status,
                                                         @RequestParam("sorttype") String sorttype,
                                                         @RequestParam("pageNum") int pageNum);

    @RequestMapping(value = "/searchEventByStatusAndPage", method = RequestMethod.POST)
    ResponseEntity<List<Object>> searchEventByStatusAndPage(@RequestParam("title") String title,
                                                         @RequestParam("status") String status,
                                                         @RequestParam("sorttype") String sorttype,
                                                         @RequestParam("pageNum") int pageNum);

    @RequestMapping(value = "/searchEventAlt", method = RequestMethod.GET)
    ResponseEntity<EventSDTO> searchEventAlt(@RequestParam("title") String title,
                                             @RequestParam("status") String status,
                                             @RequestParam("sorttype") String sorttype,
                                             @RequestParam("pageNum") int pageNum);

    @RequestMapping(value = "/getMyListEvent", method = RequestMethod.POST)
    ResponseEntity<List<Object>> getMyListEvent(@RequestParam("accountID") Integer accountID,
                                               @RequestParam("sorttype") String sorttype,
                                               @RequestParam("pageNum") int pageNum);

    @RequestMapping(value = "/getMyListEventAlt", method = RequestMethod.POST)
    ResponseEntity<EventSDTO> getMyListEventAlt(@RequestParam("accountID") Integer accountID,
                                                @RequestParam("sorttype") String sorttype,
                                                @RequestParam("pageNum") int pageNum);

    @RequestMapping(value = "/getNearbyEvent", method = RequestMethod.POST)
    ResponseEntity<List<Object>> getNearbyEvent(@RequestParam("location") String location,
                                                @RequestParam("range") long range,
                                                @RequestParam("sorttype") String sorttype,
                                                @RequestParam("pageNum") int pageNum);
    @RequestMapping(value = "/checkcheck", method = RequestMethod.POST)
    ResponseEntity<String> checkcheck();

}
