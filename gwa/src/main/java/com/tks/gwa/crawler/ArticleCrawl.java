package com.tks.gwa.crawler;

import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.parser.ArticleStAXParser;
import com.tks.gwa.utils.CrawlHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ArticleCrawl {

    @Autowired
    private ArticleStAXParser parser;

    private Crawler crawler;

    private InputStream is;

    private StreamSource ss;

    private boolean inProgress = false;

    private int records;

    public ArticleCrawl() {
        this.crawler = new Crawler();
        this.records = 0;

        System.setProperty("http.agent", "Chrome");
        System.out.println("[ArticleCrawl] Set system http.agent to Chrome");
    }

    public void crawl() {
        inProgress = true;

        // begin crawl
        int logID = logFile();
        // re-initialize
        records = 0;
        this.parser.setNewRecords(0);

        System.out.println("[ArticleCrawl] Begin crawling articles");
        // crawl last page
        crawler.parseHTML(AppConstant.URL_ARTICLE_CRAWL,
                AppConstant.URL_ARTICLE_PAGE_BEGIN_SIGN, AppConstant.URL_ARTICLE_PAGE_END_SIGN);

        String htmlContent = crawler.getHtmlContent().replaceAll("&", "%amp;");
        htmlContent = CrawlHelper.fixString(htmlContent);

        is = new ByteArrayInputStream(htmlContent.getBytes());
        ss = new StreamSource(is);

        try {
            int lastPage = parser.parsePageCount(ss);

            for (int i = 1; i <= lastPage; i++) {

                crawlListArticle(AppConstant.URL_ARTICLE_CONTEXT_PATH + "/page/" + i + "/?s");
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

        // finish crawl
        editLogFile(logID, records, this.parser.getNewRecords());

        inProgress = false;
    }

    public void crawlListArticle(String url) {
        System.out.println("Crawling page url: " + url);
        crawler.parseHTML(url, AppConstant.URL_ARTICLE_CRAWL_BEGIN_SIGN, AppConstant.URL_ARTICLE_CRAWL_END_SIGN);

        String htmlContent = crawler.getHtmlContent().replaceAll("&", "%amp;");
        htmlContent = CrawlHelper.fixString(htmlContent);

        is = new ByteArrayInputStream(htmlContent.getBytes());
        ss = new StreamSource(is);

        try {
            List<String> listArticleDetail = parser.parseArticles(ss);

            for (String detail : listArticleDetail) {
                crawlDetail(detail);

                // count number of crawled records
                records++;
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }

    public void crawlDetail(String url) {
        System.out.println("Crawling article detail with url: " + url);

        crawler.parseHTML(url, AppConstant.URL_ARTICLE_DETAIL_BEGIN_SIGN, AppConstant.URL_ARTICLE_DETAIL_END_SIGN);

        String htmlContent = crawler.getHtmlContent().replaceAll("&", "%amp;");
        htmlContent = CrawlHelper.fixString(htmlContent);

        is = new ByteArrayInputStream(htmlContent.getBytes());
        ss = new StreamSource(is);

        try {
            String content = "";

            if (htmlContent.contains("<div class=\"l-section-h i-cf\" itemprop=\"text\">")) {
                content = htmlContent.split("<div class=\"l-section-h i-cf\" itemprop=\"text\"></div>")[1]
                        .replace("</root>", "");
            } else {
                content = htmlContent.split("</article>")[1]
                        .replace("</root>", "");
            }

            parser.parseDetail(ss, content);

        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
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
            f = new File(AppConstant.LOG_FILE_ARTICLE_CRAWL);
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

            f = new File(AppConstant.LOG_FILE_ARTICLE_CRAWL);
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
            f = new File(AppConstant.LOG_FILE_ARTICLE_CRAWL);
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
        }
        finally {
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

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    public int getNewRecords() {
        return this.parser.getNewRecords();
    }
}
