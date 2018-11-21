package com.tks.gwa.service;

import com.tks.gwa.dto.LogSchedule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ScheduleService {

    /**
     *
     * @param hours
     */
    public void startScheduleModelCrawl(String hours);

    /**
     *
     */
    public void stopScheduleModelCrawl();

    /**
     *
     * @param hours
     */
    public void startScheduleUpdateTrade(String hours);

    /**
     *
     */
    public void stopScheduleUpdateTrade();

    /**
     *
     * @param hours
     */
    public void startScheduleUpdateEvent(String hours);

    /**
     *
     */
    public void stopScheduleUpdateEvent();

    /**
     *
     * @return
     */
    public List<LogSchedule> getLogSchedule();

    /**
     *
     * @return
     */
    public List<Object> getScheduleStatus();
}
