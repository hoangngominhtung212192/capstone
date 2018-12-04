package com.tks.gwa.crawler;

import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.jaxb.Image;
import com.tks.gwa.parser.ModelStAXParser;
import com.tks.gwa.utils.CrawlHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.Buffer;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ModelCrawl {

    @Autowired
    private ModelStAXParser parser;

    private Crawler crawler;

    private InputStream is;

    private StreamSource ss;

    private boolean inProgress = false;

    private int records;

    public ModelCrawl() {
        this.crawler = new Crawler();
        this.records = 0;
    }

    public void crawl() {
        inProgress = true;

        // begin crawl
        int logID = logFile();
        // re-initialize
        records = 0;
        this.parser.setNewRecords(0);

        for (int i = 0; i < AppConstant.listModelCrawlUrls.length; i++) {
            crawlPageCount(AppConstant.listModelCrawlUrls[i]);
        }

        // finish crawl
        editLogFile(logID, records, this.parser.getNewRecords());

        inProgress = false;
    }

    public void crawlPageCount(String url) {
        System.out.println("Crawling url: " + url);

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
                int nextFourPages = 0;

                if (lastPage > 10) {
                    nextFourPages = Integer.parseInt(url.substring(url.length() - 2, url.length())) + 4;
                }

                if (lastPage == nextFourPages) {
                    String newUrl = url.substring(0, url.length() - 2) + lastPage;
                    crawlPageCount(newUrl);
                } else {
                    System.out.println("Total page: " + lastPage);

                    // run crawling; loop until the last page
                    for (int i = 1; i <= lastPage; i++) {
                        String page_url = "";
                        if (lastPage < 10) {
                            page_url = url.substring(0, url.length() - 1) + i;
                        } else {
                            page_url = url.substring(0, url.length() - 2) + i;
                        }

                        System.out.println("Crawling page number: " + i + " - url: " + page_url);
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
                    records += 1;
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
        try {
            htmlContent = new String(CrawlHelper.fixString(htmlContent).getBytes(), "UTF-8");

            is = new ByteArrayInputStream(htmlContent.getBytes());
            ss = new StreamSource(is);

            parser.parseModelDetail(ss);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public List<Image> crawlModelImage(String url) {
        crawler.parseHTML(AppConstant.URL_GUNDAM_HOME_PAGE_CRAWL + url,
                AppConstant.URL_GUNDAM_MODEL_IMAGE_BEGIN_SIGN, AppConstant.URL_GUNDAM_MODEL_IMAGE_END_SIGN);

        // get html string
        String htmlContent = crawler.getHtmlContent().replaceAll("&", "%amp;");
        htmlContent = CrawlHelper.fixString(htmlContent);

        htmlContent = htmlContent.replaceAll("style=\"width:\\d+;height:\\d+;\"", "");

        is = new ByteArrayInputStream(htmlContent.getBytes());
        ss = new StreamSource(is);

        try {
            List<Image> jaxb_list_images = parser.parseModelImage(ss);

            return jaxb_list_images;
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void editLogFile(int currentID, int records, int newRecords) {
        List<String> lines = new ArrayList<>();
        String line = null;
        File f = null;
        FileWriter fw = null;
        PrintWriter pw = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            f = new File(AppConstant.LOG_FILE_MODEL_CRAWL);
            if (!f.exists()) {
                return;
            }

            fr = new FileReader(f);
            br = new BufferedReader(fr);
            String details;

            while ((details = br.readLine()) != null) {
                line = "";
                StringTokenizer stk = new StringTokenizer(details, ";");
                int id = Integer.parseInt(stk.nextToken());
                line += id;
                if (id == currentID) {
                    line += ";" + stk.nextToken() + ";" + records + ";" + newRecords + ";Done";
                } else {
                    line += ";" + stk.nextToken() + ";" + stk.nextToken() + ";" + stk.nextToken() + ";" + stk.nextToken();
                }
                lines.add(line);
            }
            br.close();
            fr.close();

            if (lines.size() > 0) {
                fw = new FileWriter(f, false);
                pw = new PrintWriter(fw);

                for (String s : lines) {
                    pw.println(s);
                }
                pw.close();
                fw.close();

                System.out.println("Log ID " + currentID + " has been updated with records: " + records + ", " +
                        "new records: " + newRecords + " and status: Done");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int logFile() {
        File f = null;
        FileWriter fw = null;
        PrintWriter pw = null;

        try {
            int id = getLastIDFromFile();
            id += 1;

            f = new File(AppConstant.LOG_FILE_MODEL_CRAWL);
            if (!f.exists()) {
                f.createNewFile();
            }
            fw = new FileWriter(f, true);
            pw = new PrintWriter(fw);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            String strDate = sdf.format(now);

            if (id == 1) {
                pw.print(id + ";" + strDate + ";" + "N/A;N/A;Crawling");
                pw.println("");
            } else {
                pw.println(id + ";" + strDate + ";" + "N/A;N/A;Crawling");
            }
            System.out.println("Append to log crawl model: " + id + ";" + strDate + ";N/A;N/A;Crawling");

            return id;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pw != null) {
                    pw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return 0;
    }

    public int getLastIDFromFile() {

        File f = null;
        FileReader fr = null;
        BufferedReader br = null;

        int id = 0;

        try {
            f = new File(AppConstant.LOG_FILE_MODEL_CRAWL);
            if (!f.exists()) {
                return id;
            }

            fr = new FileReader(f);
            br = new BufferedReader(fr);
            String details;

            while ((details = br.readLine()) != null) {
                StringTokenizer stk = new StringTokenizer(details, ";");
                id = Integer.parseInt(stk.nextToken());
            }

            System.out.println("The last id of log file: " + id);

            return id;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            System.out.println("There is no data in log file");
            return 0;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return 0;
    }

    public int getRecords() {
        return records;
    }

    public int getNewRecords() {
        return this.parser.getNewRecords();
    }
}
