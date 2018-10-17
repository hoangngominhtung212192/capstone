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

    // crawl html contain page count from toto shop
    public void parseHTML_getPageCount_Toto(String uri, String beginSign, String key) {
        this.pageCount = 0;

        try {
            URL url = new URL(uri);
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String inputLine = null;

            while ((inputLine = br.readLine()) != null) {
                // if line contain beginSign/
                if (inputLine.contains(beginSign)) {
                    // get page count
                    updatePageCount_Toto(inputLine, key);
                }

            }
            is.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // get page count of toto page
    public void updatePageCount_Toto(String content, String key) {
        this.pageCount = 0;

        // if content contains key
        if (content.contains(key)) {
            // get index of key
            int pos = content.indexOf(key);
            // the index of the last character left of page count
            int beginPage = pos + key.length();
            int endPage = -1;

            for (int i = beginPage; i < content.length(); i++) {
                // get the character right of page count
                if (content.charAt(i) == '"') {
                    endPage = i;
                    break;
                }
            }

            if (endPage != -1) {
                // split the string with beginPage and endPage index
                String subString = content.substring(beginPage, endPage);

                int num = Integer.parseInt(subString);
                pageCount = Math.max(num, pageCount);
            }
        }
    }

    // crawl html contain page count from k300 shop
    public void parseHTML_getPageCount_K300(String uri, String beginSign, String key) {
        boolean isInside = false;

        try {
            URL url = new URL(uri);
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String inputLine = null;

            while ((inputLine = br.readLine()) != null) {
                if (inputLine.contains(beginSign)) {
                    isInside = true;
                }
                if (isInside) {
                    if (inputLine.contains(key)) {
                        updatePageCount_K300(inputLine, key);
                    }
                }

            }
            is.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void updatePageCount_K300(String content, String key) {
        this.pageCount = 0;

        if (content.contains(key + "&iota;")) {
            int pos = content.indexOf(key);
            int beginPage = pos + key.length();
            int endPage = -1;
            for (int i = beginPage; i < content.length(); i++) {
                if (content.charAt(i) == '.') {
                    endPage = i;
                    break;
                }
            }

            if (endPage != -1) {
                String subString = content.substring(beginPage, endPage);
                String pageString = "" + subString.charAt(subString.length() - 1);

                int num = Integer.parseInt(pageString);
                pageCount = Math.max(num, pageCount);
            }
        } else {
            String[] stringSplit = content.split("<a");
            String stringContainLastPage = stringSplit[stringSplit.length - 2];

            String[] stringSplitContainLastPage = stringContainLastPage.split("span>");
            String lastPage = stringSplitContainLastPage[stringSplitContainLastPage.length - 2].replace("</", "");

            pageCount = Integer.parseInt(lastPage);
        }
    }
}
