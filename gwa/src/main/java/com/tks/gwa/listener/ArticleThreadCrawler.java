package com.tks.gwa.listener;

import com.tks.gwa.crawler.ArticleCrawl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleThreadCrawler implements Runnable {

    @Autowired
    private ArticleCrawl articleCrawl;

    @Override
    public void run() {
        articleCrawl.crawl();
    }

    public boolean isInprogress() {
        return articleCrawl.isInProgress();
    }

    public int getCrawlRecords() {
        return articleCrawl.getRecords();
    }

    public int getNewRecords() {
        return articleCrawl.getNewRecords();
    }
}
