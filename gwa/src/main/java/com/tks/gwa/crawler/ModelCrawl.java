package com.tks.gwa.crawler;

import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.parser.ModelStAXParser;
import com.tks.gwa.utils.CrawlHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@Service
public class ModelCrawl {

    @Autowired
    private ModelStAXParser parser;

    private Crawler crawler;

    private InputStream is;

    private StreamSource ss;

    public ModelCrawl() {
        this.crawler = new Crawler();
    }

    public void crawl() {
        crawlPageCount(AppConstant.URL_GUNDAM_REAL_GRADE);

        crawlPageCount(AppConstant.URL_GUNDAM_HIGH_GRADE);

        crawlPageCount(AppConstant.URL_GUNDAM_MASTER_GRADE);

        crawlPageCount(AppConstant.URL_GUNDAM_PERFECT_GRADE);
    }

    public void crawlPageCount(String url) {
        crawler.parseHTML(url,
                AppConstant.URL_GUNDAM_MODELS_PAGE_BEGIN_SIGN, AppConstant.URL_GUNDAM_MODELS_PAGE_END_SIGN);

        String htmlContent = crawler.getHtmlContent().replaceAll("&", "%amp;");
        htmlContent = CrawlHelper.fixString(htmlContent);

        is = new ByteArrayInputStream(htmlContent.getBytes());
        ss = new StreamSource(is);

        try {
            int lastPage = parser.parsePageCount(ss);

            if (lastPage == 0) lastPage = 1;

            if (lastPage == 10) {
                String newUrl = url.substring(0, url.length() - 1) + "10";
                crawlPageCount(newUrl);
            } else {
                if (lastPage == 14) {
                    String newUrl = url.substring(0, url.length() - 1) + "14";
                    crawlPageCount(newUrl);
                } else {
                    System.out.println("Total page: " + lastPage);

                    for (int i = 1; i <= lastPage; i++) {
                        System.out.println("Crawling page number: " + i);

                        String page_url = url.substring(0, url.length() - 1) + i;

                        crawlModel(page_url);
                    }
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    public void crawlModel(String url) {
        crawler.parseHTML(url, AppConstant.URL_GUNDAM_MODELS_BEGIN_SIGN, AppConstant.URL_GUNDAM_MODELS_END_SIGN);

        // get html string
        String htmlContent = crawler.getHtmlContent().replaceAll("&", "%amp;");
        htmlContent = CrawlHelper.fixString(htmlContent);

        is = new ByteArrayInputStream(htmlContent.getBytes());
        ss = new StreamSource(is);

        try {
            List<String> listDetailLink = parser.parseModel(ss);

            System.out.println("");

            if (listDetailLink.size() > 0) {
                for (int i = 0; i < listDetailLink.size(); i++) {
                    int count = i + 1;
                    System.out.println("");
                    System.out.println("Model number " + count + ":");
                    crawlModelDetail(AppConstant.URL_GUNDAM_HOME_PAGE_CRAWL + listDetailLink.get(i));
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }

    public void crawlModelDetail(String url) {
        crawler.parseHTML(url, AppConstant.URL_GUNDAM_MODEL_DETAIL_BEGIN_SIGN, AppConstant.URL_GUNDAM_MODEL_DETAIL_END_SIGN);

        // get html string
        String htmlContent = crawler.getHtmlContent().replaceAll("&", "%amp;");
        htmlContent = CrawlHelper.fixString(htmlContent);

        is = new ByteArrayInputStream(htmlContent.getBytes());
        ss = new StreamSource(is);

        try {
            parser.parseModelDetail(ss);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    public void crawlModelImage(String url) {
        crawler.parseHTML(AppConstant.URL_GUNDAM_HOME_PAGE_CRAWL + url,
                AppConstant.URL_GUNDAM_MODEL_IMAGE_BEGIN_SIGN, AppConstant.URL_GUNDAM_MODEL_IMAGE_END_SIGN);

        // get html string
        String htmlContent = crawler.getHtmlContent().replaceAll("&", "%amp;");
        htmlContent = CrawlHelper.fixString(htmlContent);

        htmlContent = htmlContent.replaceAll("style=\"width:\\d+;height:\\d+;\"", "");

        is = new ByteArrayInputStream(htmlContent.getBytes());
        ss = new StreamSource(is);

        try {
            parser.parseModelImage(ss);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
