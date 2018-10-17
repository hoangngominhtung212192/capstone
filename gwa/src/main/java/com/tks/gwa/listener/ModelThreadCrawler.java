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
        while (true) {
            try {
                modelCrawl.crawl();
                wait(Long.parseLong(watingTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
