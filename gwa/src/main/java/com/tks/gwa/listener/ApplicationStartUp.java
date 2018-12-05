package com.tks.gwa.listener;

import com.tks.gwa.crawler.ArticleCrawl;
import com.tks.gwa.crawler.ModelCrawl;
import com.tks.gwa.service.ModelService;
import com.tks.gwa.service.ScheduleService;
import com.tks.gwa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;

@Component
public class ApplicationStartUp implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    ModelThreadCrawler modelThreadCrawler;

    @Autowired
    ModelCrawl modelCrawl;

    @Autowired
    ModelService modelService;

    @Autowired
    ArticleCrawl articleCrawl;

    @Autowired
    UserService userService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        System.out.println("#" + ApplicationStartUp.class.getName() + ": Application Starting Up...");

    }
}
