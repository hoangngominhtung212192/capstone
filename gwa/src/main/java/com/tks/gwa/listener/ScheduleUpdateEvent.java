package com.tks.gwa.listener;

import com.tks.gwa.service.EventService;
import com.tks.gwa.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ScheduleUpdateEvent implements Runnable {

    private AtomicBoolean running = new AtomicBoolean(false);
    private int interval;
    private String date;

    @Autowired
    private EventService eventService;

    @Override
    public void run() {
        System.out.println("[ScheduleUpdateEvent] Thread is starting to run");
        // reset
        running.set(true);
        date = getCurrentTimeStamp();

        while (running.get()) {
            try {
                // sleep for interval
                Thread.sleep(interval);

                // log file
                date = getCurrentTimeStamp();
                FileUtil.logSchedule("[Schedule Update Event] System is beginning to update all events after cycle sleep",
                        (float) interval/1000/60/60, date);

                // Begin process update
                eventService.checkNUpdateEventStatus();

                // End process update

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread ScheduleUpdateEvent is interrupted");
            }
        }
    }

    public boolean isRunning() {
        return running.get();
    }

    public void setRunning(boolean running) {
        this.running.set(running);
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String strDate = sdf.format(now);
        return strDate;
    }
}
