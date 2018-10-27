package com.tks.gwa.dto;

public class ResponseImage {
    private int uploaded;
    private String fileName;
    private String url;

    public ResponseImage(){

    }
    public ResponseImage(int uploaded, String fileName, String url){
        this.uploaded = uploaded;
        this.fileName = fileName;
        this.url = url;
    }

    public int getUploaded() {
        return uploaded;
    }

    public void setUploaded(int uploaded) {
        this.uploaded = uploaded;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
