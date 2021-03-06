package com.tks.gwa.dto;

public class LogSchedule {

    int id;
    String description;
    float cycle;
    String date;

    public LogSchedule(int id, String description, float cycle, String date) {
        this.id = id;
        this.description = description;
        this.cycle = cycle;
        this.date = date;
    }

    public LogSchedule(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getCycle() {
        return cycle;
    }

    public void setCycle(float cycle) {
        this.cycle = cycle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
