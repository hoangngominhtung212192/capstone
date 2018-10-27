package com.tks.gwa.dto;

public class LogCrawl {

    int id;
    String crawlDateTime;
    String numberOfRecords;
    String numberOfNewRecords;
    String status;
    boolean inProgress;

    public LogCrawl(){}

    public LogCrawl(int id, String crawlDateTime, String numberOfRecords, String numberOfNewRecords,String status) {
        this.id = id;
        this.crawlDateTime = crawlDateTime;
        this.numberOfRecords = numberOfRecords;
        this.numberOfNewRecords = numberOfNewRecords;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCrawlDateTime() {
        return crawlDateTime;
    }

    public void setCrawlDateTime(String crawlDateTime) {
        this.crawlDateTime = crawlDateTime;
    }

    public String getNumberOfRecords() {
        return numberOfRecords;
    }

    public void setNumberOfRecords(String numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNumberOfNewRecords() {
        return numberOfNewRecords;
    }

    public void setNumberOfNewRecords(String numberOfNewRecords) {
        this.numberOfNewRecords = numberOfNewRecords;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }
}
