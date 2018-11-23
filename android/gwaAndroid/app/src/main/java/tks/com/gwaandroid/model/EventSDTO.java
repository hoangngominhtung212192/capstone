package tks.com.gwaandroid.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventSDTO {
    @SerializedName("totalPage")
    private int totalPage;

    @SerializedName("eventList")
    private List<Event> eventList;

    public EventSDTO() {}

    public EventSDTO(int totalPage, List<Event> eventList) {
        this.totalPage = totalPage;
        this.eventList = eventList;
    }



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
