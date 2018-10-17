package com.tks.gwa.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartUp implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    ModelThreadCrawler modelThreadCrawler;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        System.out.println("#" + ApplicationStartUp.class.getName() + ": Application Starting Up...");

        Thread autoCrawling_Model = new Thread(modelThreadCrawler, "ThreadAutoCrawling_Model");
        autoCrawling_Model.start();
    }
}
