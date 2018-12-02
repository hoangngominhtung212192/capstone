package com.tks.gwa.parser;

import com.tks.gwa.jaxb.article.Article;
import com.tks.gwa.service.ArticleService;
import com.tks.gwa.transformer.ArticleTransformer;
import com.tks.gwa.utils.StAXParserHelper;
import com.tks.gwa.utils.XMLUltimate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleStAXParser {

    @Autowired
    private ArticleTransformer articleTransformer;

    @Autowired
    private ArticleService articleService;

    private int newRecords;

    public ArticleStAXParser() {
        this.newRecords = 0;
    }

    public int parsePageCount(StreamSource ss) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory = XMLUltimate.setProperty(factory);
        XMLStreamReader reader = null;

        reader = factory.createXMLStreamReader(ss);

        String lastPage = "";

        while (reader.hasNext()) {
            int cursor = reader.next();

            if (cursor == XMLStreamConstants.START_ELEMENT) {
                String tagName = reader.getLocalName();

                if (tagName.equals("span")) {
                    if (reader.next() == XMLStreamConstants.CHARACTERS) {
                        lastPage = reader.getText();
                    }
                }
            }
        }

        return Integer.parseInt(lastPage);
    }

    public List<String> parseArticles(StreamSource ss) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory = XMLUltimate.setProperty(factory);
        XMLStreamReader reader = null;

        reader = factory.createXMLStreamReader(ss);

        List<String> listLinkDetail = new ArrayList<>();

        int count = 0;
        while (reader.hasNext()) {
            int cursor = reader.next();
            if (cursor == XMLStreamConstants.START_ELEMENT) {
                String tagName = reader.getLocalName();

                if (tagName.equals("a")) {
                    String href = StAXParserHelper.getNodeStAXValue(reader, tagName, "", "href");

                    if (href != null) {
                        if (!href.equalsIgnoreCase("https://www.gunpla101.com/contact-us/")
                                && !href.equalsIgnoreCase("https://www.gunpla101.com/start-here-old/")
                                && !href.equalsIgnoreCase("https://www.gunpla101.com/about-us/")
                                && !href.equalsIgnoreCase("https://www.gunpla101.com/start-here/")
                                && !href.contains("https://www.gunpla101.com/holiday-2017")) {
                            count++;
                            System.out.println("Number " + count + ": " + href);
                            listLinkDetail.add(href);
                        }
                    }
                }
            }
        }

        return listLinkDetail;
    }

    @Transactional
    public void parseDetail(StreamSource ss, String content) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory = XMLUltimate.setProperty(factory);
        XMLStreamReader reader = null;

        reader = factory.createXMLStreamReader(ss);

        Article article = new Article();
        boolean imageFlag = false;

        while (reader.hasNext()) {
            int cursor = reader.next();
            if (cursor == XMLStreamConstants.START_ELEMENT) {
                String tagName = reader.getLocalName();

                if (tagName.equals("h1")) {
                    String h1_class = StAXParserHelper.getNodeStAXValue(reader, tagName, "", "class");

                    if (h1_class != null) {
                        if (h1_class.equalsIgnoreCase("w-blogpost-title entry-title")) {
                            String title = StAXParserHelper.getTextStAXContext(reader, tagName);

                            title = title.replaceAll("%amp;", "&");

                            System.out.println("Title: " + title);
                            article.setTitle(title);
                        }
                    }
                }

                if (tagName.equals("a")) {
                    String a_rel = StAXParserHelper.getNodeStAXValue(reader, tagName, "", "rel");

                    if (a_rel != null) {
                        if (a_rel.equalsIgnoreCase("category tag")) {
                            String category = StAXParserHelper.getTextStAXContext(reader, tagName);
                            System.out.println("Category: " + category);
                            article.setCategory(category);
                        }
                    }
                }

                if (tagName.equals("img")) {
                    if (!imageFlag) {
                        String img_src = StAXParserHelper.getNodeStAXValue(reader, tagName, "", "src");

                        if (img_src != null) {
                            String thumbImage = img_src;
                            System.out.println("Thumbimage: " + thumbImage);
                            article.setThumbImage(thumbImage);

                            imageFlag = true;
                        }
                    }
                }


            }
        }

        content = content.replaceAll("%amp;", "&");
        content = content.replaceAll("script", "div");
        System.out.println("Content: " + content);
        article.setContent(content);

        com.tks.gwa.entity.Article entity_article = articleTransformer.convertToEntity(article);
        if (entity_article != null) {
            com.tks.gwa.entity.Article result = articleService.createCrawlArticle(entity_article);
            if (result != null)  {
                newRecords++;
            }
        }
    }

    public int getNewRecords() {
        return newRecords;
    }

    public void setNewRecords(int newRecords) {
        this.newRecords = newRecords;
    }
}
