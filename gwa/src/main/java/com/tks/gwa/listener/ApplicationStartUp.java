package com.tks.gwa.listener;

import com.tks.gwa.crawler.ModelCrawl;
import com.tks.gwa.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartUp implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    ModelThreadCrawler modelThreadCrawler;

    @Autowired
    ModelCrawl modelCrawl;

    @Autowired
    ModelService modelService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        System.out.println("#" + ApplicationStartUp.class.getName() + ": Application Starting Up...");

        // begin crawl models
//        modelCrawl.crawl();
    }
}
