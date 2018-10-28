package com.tks.gwa.listener;

import com.tks.gwa.crawler.ModelCrawl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ModelThreadCrawler implements Runnable {

    @Autowired
    private ModelCrawl modelCrawl;

    @Value("${crawler.thread.timeupdate.miliseconds}")
    private String watingTime;

    @Override
    synchronized public void run() {
        modelCrawl.crawl();
    }

    public boolean isInprogress() {
        return modelCrawl.isInProgress();
    }

    public int getCrawlRecords() {
        return modelCrawl.getRecords();
    }

    public int getNewRecords() {
        return modelCrawl.getNewRecords();
    }
}

