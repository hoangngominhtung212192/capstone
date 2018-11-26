package com.tks.gwa.dto;

import com.tks.gwa.entity.Event;

import java.util.List;

public class EventSDTO {
    private int totalPage;

    private List<Event> eventList;


    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }
}
