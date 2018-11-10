package com.tks.gwa.controller;

import com.tks.gwa.dto.LogSchedule;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
public interface ScheduleWS {

    @RequestMapping(value = "/startSModelCrawl", method = RequestMethod.GET)
    ResponseEntity<String> startModelCrawlSchedule(@RequestParam("hours") int hours);

    @RequestMapping(value = "/stopSModelCrawl", method = RequestMethod.GET)
    ResponseEntity<String> stopModelCrawlSchedule();

    @RequestMapping(value = "/startSUpdateTrade", method = RequestMethod.GET)
    ResponseEntity<String> startUpdateTradeSchedule(@RequestParam("hours") int hours);

    @RequestMapping(value = "/stopSUpdateTrade", method = RequestMethod.GET)
    ResponseEntity<String> stopUpdateTradeSchedule();

    @RequestMapping(value = "/startSUpdateEvent", method = RequestMethod.GET)
    ResponseEntity<String> startUpdateEventSchedule(@RequestParam("hours") int hours);

    @RequestMapping(value = "/stopSUpdateEvent", method = RequestMethod.GET)
    ResponseEntity<String> stopUpdateEventSchedule();

    @RequestMapping(value = "/getListScheduleStatus", method = RequestMethod.GET)
    ResponseEntity<List<Object>> getListScheduleStatus();

    @RequestMapping(value = "/getLogSchedule", method = RequestMethod.GET)
    ResponseEntity<List<LogSchedule>> getListLogSchedule();
}
