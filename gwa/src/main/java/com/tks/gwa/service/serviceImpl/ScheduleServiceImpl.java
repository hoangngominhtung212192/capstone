package com.tks.gwa.service.serviceImpl;

import com.tks.gwa.dto.LogSchedule;
import com.tks.gwa.listener.ScheduleModelCrawl;
import com.tks.gwa.listener.ScheduleUpdateEvent;
import com.tks.gwa.listener.ScheduleUpdateTrade;
import com.tks.gwa.service.ScheduleService;
import com.tks.gwa.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleModelCrawl scheduleModelCrawl;

    @Autowired
    private ScheduleUpdateTrade scheduleUpdateTrade;

    @Autowired
    private ScheduleUpdateEvent scheduleUpdateEvent;

    Thread scheduleModelThread;

    Thread scheduleTradeThread;

    Thread scheduleEventThread;

    @Override
    public void startScheduleModelCrawl(int hours) {

        // if not running
        if (!scheduleModelCrawl.isRunning()) {

            // convert hour to milliseconds
            int interval = hours * 60 * 60 * 1000;

            // set default value
            scheduleModelCrawl.setInterval(interval);

            scheduleModelThread = new Thread(scheduleModelCrawl, "Schedule Model Crawl");
            scheduleModelThread.start();
        }
    }

    @Override
    public void stopScheduleModelCrawl() {
        scheduleModelCrawl.setRunning(false);
        scheduleModelCrawl.setDate(null);
        // interrupt thread
        scheduleModelThread.interrupt();
    }

    @Override
    public void startScheduleUpdateTrade(int hours) {

        // if not running
        if (!scheduleUpdateTrade.isRunning()) {

            // convert hour to milliseconds
            int interval = hours * 60 * 60 * 1000;

            // set default value
            scheduleUpdateTrade.setInterval(interval);

            scheduleTradeThread = new Thread(scheduleUpdateTrade, "Schedule Update Trade");
            scheduleTradeThread.start();
        }
    }

    @Override
    public void stopScheduleUpdateTrade() {
        scheduleUpdateTrade.setRunning(false);
        scheduleUpdateTrade.setDate(null);
        // interrupt thread
        scheduleTradeThread.interrupt();
    }

    @Override
    public void startScheduleUpdateEvent(int hours) {

        // if not running
        if (!scheduleUpdateEvent.isRunning()) {

            // convert hour to milliseconds
            int interval = hours * 60 * 60 * 1000;

            // set default value
            scheduleUpdateEvent.setInterval(interval);

            scheduleEventThread = new Thread(scheduleUpdateEvent, "Schedule Update Event");
            scheduleEventThread.start();
        }
    }

    @Override
    public void stopScheduleUpdateEvent() {
        scheduleUpdateEvent.setRunning(false);
        scheduleUpdateEvent.setDate(null);
        // interrupt thread
        scheduleEventThread.interrupt();
    }

    @Override
    public List<LogSchedule> getLogSchedule() {

        List<LogSchedule> logScheduleList = FileUtil.getLogScheduleFromFile();

        List<LogSchedule> result = new ArrayList<>();

        // reverse list to sort by date DESC
        for (int i = logScheduleList.size() - 1; i >= 0; i--) {
            result.add(logScheduleList.get(i));
        }

        return result;
    }

    @Override
    public List<Object> getScheduleStatus() {

        // all schedules's status
        List<Object> scheduleStatusList = new ArrayList<>();

        // schedule model crawl's status
        List<Object> scheduleModelCrawlStatus = new ArrayList<>();

        scheduleModelCrawlStatus.add(scheduleModelCrawl.isRunning());

        // check null
        if (scheduleModelCrawl.getDate() != null) {
            try {
                String countdownTime = calculateCountdownTime(scheduleModelCrawl.getDate(), scheduleModelCrawl.getInterval());

                scheduleModelCrawlStatus.add(countdownTime);
                scheduleModelCrawlStatus.add(scheduleModelCrawl.getInterval()/1000/60/60);
            } catch (ParseException e) {
                scheduleModelCrawlStatus.add("Parse error, invalid format of yyyy-MM-dd HH:mm:ss");
                e.printStackTrace();
            }
        }
        // add to list schedules status
        scheduleStatusList.add(scheduleModelCrawlStatus);


        // schedule update trade's status
        List<Object> scheduleUpdateTradeStatus = new ArrayList<>();

        scheduleUpdateTradeStatus.add(scheduleUpdateTrade.isRunning());

        // check null
        if (scheduleUpdateTrade.getDate() != null) {
            try {
                String countdownTime = calculateCountdownTime(scheduleUpdateTrade.getDate(), scheduleUpdateTrade.getInterval());

                scheduleUpdateTradeStatus.add(countdownTime);
                scheduleUpdateTradeStatus.add(scheduleUpdateTrade.getInterval()/1000/60/60);
            } catch (ParseException e) {
                scheduleUpdateTradeStatus.add("Parse error, invalid format of yyyy-MM-dd HH:mm:ss");
                e.printStackTrace();
            }
        }
        // add to list schedules status
        scheduleStatusList.add(scheduleUpdateTradeStatus);


        // schedule update event's status
        List<Object> scheduleUpdateEventStatus = new ArrayList<>();

        scheduleUpdateEventStatus.add(scheduleUpdateEvent.isRunning());

        // check null
        if (scheduleUpdateEvent.getDate() != null) {
            try {
                String countdownTime = calculateCountdownTime(scheduleUpdateEvent.getDate(), scheduleUpdateEvent.getInterval());

                scheduleUpdateEventStatus.add(countdownTime);
                scheduleUpdateEventStatus.add(scheduleUpdateEvent.getInterval()/1000/60/60);
            } catch (ParseException e) {
                scheduleUpdateEventStatus.add("Parse error, invalid format of yyyy-MM-dd HH:mm:ss");
                e.printStackTrace();
            }
        }
        // add to list schedules status
        scheduleStatusList.add(scheduleUpdateEventStatus);


        return scheduleStatusList;
    }

    // calculate difference between two datetime
    public String calculateCountdownTime(String startDateS, int interval) throws ParseException {

        // convert from milliseconds to seconds
        int seconds = interval / 1000;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = sdf.parse(startDateS);

        // plus time
        Date newDate = plusTime(startDate, seconds);
        System.out.println("New date after converted: " + sdf.format(newDate));

        // get currentTimeStamp
        Date now = new Date();
        System.out.println("Now: " + sdf.format(now));

        // calculate difference between two date
        long diffInMinutes = newDate.getTime() - now.getTime();

        // convert to seconds
        int convertedSecond = (int) TimeUnit.SECONDS.convert(diffInMinutes, TimeUnit.MILLISECONDS);

        // convert to HH:mm:ss format
        Date convertedDate = new Date(convertedSecond * 1000L);
        sdf = new SimpleDateFormat("d HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String convertTime = sdf.format(convertedDate);

        System.out.println("Difference: " + convertTime);

        return convertTime;
    }

    // plus time to a date
    public Date plusTime(Date date, int seconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, seconds);
        Date newDate = cal.getTime();

        return newDate;
    }
}
