package com.tks.gwa.controller.controllerImpl;

import com.tks.gwa.controller.ScheduleWS;
import com.tks.gwa.dto.LogSchedule;
import com.tks.gwa.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ScheduleWSImpl implements ScheduleWS {

    @Autowired
    ScheduleService scheduleService;

    @Override
    public ResponseEntity<String> startModelCrawlSchedule(int hours) {

        System.out.println("[ScheduleWS] Begin startModelCrawlSchedule() with hours: " + hours);

        scheduleService.startScheduleModelCrawl(hours);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> stopModelCrawlSchedule() {

        System.out.println("[ScheduleWS] Begin stopModelCrawlSchedule()");

        scheduleService.stopScheduleModelCrawl();

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> startUpdateTradeSchedule(int hours) {

        System.out.println("[ScheduleWS] Begin startUpdateTradeSchedule() with hours: " + hours);

        scheduleService.startScheduleUpdateTrade(hours);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> stopUpdateTradeSchedule() {
        System.out.println("[ScheduleWS] Begin stopUpdateTradeSchedule()");

        scheduleService.stopScheduleUpdateTrade();

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> startUpdateEventSchedule(int hours) {

        System.out.println("[ScheduleWS] Begin startUpdateEventSchedule() with hours: " + hours);

        scheduleService.startScheduleUpdateEvent(hours);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> stopUpdateEventSchedule() {

        System.out.println("[ScheduleWS] Begin stopUpdateTradeSchedule()");

        scheduleService.stopScheduleUpdateEvent();

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Object>> getListScheduleStatus() {

        System.out.println("[ScheduleWS] Begin getListScheduleStatus()");

        List<Object> result = scheduleService.getScheduleStatus();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<LogSchedule>> getListLogSchedule() {

        System.out.println("[ScheduleWS] Begin getListLogSchedule()");

        List<LogSchedule> logSchedules = scheduleService.getLogSchedule();

        return new ResponseEntity<>(logSchedules, HttpStatus.OK);
    }


}
