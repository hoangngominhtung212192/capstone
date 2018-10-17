package com.tks.gwa.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Crawler {

    public String htmlContent;

    public int pageCount;

    /**
     *
     * @return
     */
    public String getHtmlContent() {
        return htmlContent;
    }

    /**
     * get Page count
     * @return
     */
    public int getPageCount() {
        return pageCount;
    }

    // crawl html
    public void parseHTML(String uri, String beginSign, String endSign) {

        this.htmlContent = "";
        boolean isInside = false;
        int count = 0;

        try {
            URL url = new URL(uri);
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String inputLine = null;

            while ((inputLine = br.readLine()) != null) {
                // if contain beginSign, turn isInside on
                if (inputLine.contains(beginSign)) {
                    if (count == 0) isInside = true;
                    count++;
                }
                // if contain endSign, turn endSign off
                if (inputLine.contains(endSign)) {
                    isInside = false;
                }
                // append line to htmlContent
                if (isInside) {
                    htmlContent = htmlContent + inputLine + "\n";
                }
            }
            is.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
