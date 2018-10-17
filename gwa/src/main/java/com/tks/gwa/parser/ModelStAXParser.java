package com.tks.gwa.parser;

import com.tks.gwa.crawler.ModelCrawl;
import com.tks.gwa.utils.StAXParserHelper;
import com.tks.gwa.utils.XMLUltimate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ModelStAXParser {

    @Autowired
    private ModelCrawl modelCrawl;

    public List<String> parseModel(StreamSource ss) throws XMLStreamException {
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
                    String getID = StAXParserHelper.getNodeStAXValue(reader, tagName, "", "id");

                    if (getID != null) {
                        String detailLink = StAXParserHelper.getNodeStAXValue(reader, tagName, "", "href");

                        if (detailLink != null) {
                            count++;
                            System.out.println(count + detailLink);
                            listLinkDetail.add(detailLink);
                        }
                    }
                }
            }
        }

        return listLinkDetail;
    }

    public void parseModelDetail(StreamSource ss) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory = XMLUltimate.setProperty(factory);
        XMLStreamReader reader = null;

        reader = factory.createXMLStreamReader(ss);

        boolean manufacturerFlag = false;
        boolean scaleFlag = false;
        boolean seriesFlag = false;
        boolean releasedDateFlag = false;
        boolean listPriceFlag = false;
        boolean salesPriceFlag = false;
        boolean itemCodeFlag = false;

        int count = 0;
        while (reader.hasNext()) {
            int cursor = reader.next();
            if (cursor == XMLStreamConstants.START_ELEMENT) {
                String tagName = reader.getLocalName();

                if (tagName.equals("h2")) {
                    String h2_class = StAXParserHelper.getNodeStAXValue(reader, tagName, "", "class");

                    if (h2_class != null) {
                        if (h2_class.equals("h2_itemDetail")) {
                            String longName = StAXParserHelper.getTextStAXContext(reader, tagName);

                            String name = longName.replaceAll("[(][A-z\\s]+[)]", "").trim();
                            name = name.replaceAll("\\*w/", "");

                            System.out.println("Name: " + name);
                        }
                    }
                }

                if (tagName.equals("td")) {

                    String td_class = StAXParserHelper.getNodeStAXValue(reader, tagName, "", "class");
                    String td_style = StAXParserHelper.getNodeStAXValue(reader, tagName, "", "style");

                    if (td_class != null && td_style != null) {
                        if (td_class.equals("tdItemDetail") && td_style.equals("width:103px;")) {
                            String td_detail_content = StAXParserHelper.getTextStAXContext(reader, tagName);

                            if (td_detail_content.equals("Manufacturer")) {
                                manufacturerFlag = true;
                            }
                            if (td_detail_content.equals("Series")) {
                                seriesFlag = true;
                            }
                            if (td_detail_content.equals("Scale")) {
                                scaleFlag = true;
                            }
                            if (td_detail_content.equals("Release Date")) {
                                releasedDateFlag = true;
                            }
                            if (td_detail_content.equals("List Price")) {
                                listPriceFlag = true;
                            }
                            if (td_detail_content.equals("Sales Price")) {
                                salesPriceFlag = true;
                            }
                            if (td_detail_content.equals("Item code")) {
                                itemCodeFlag = true;
                            }
                        }
                    }

                    if (td_class != null && td_style == null) {
                        if (manufacturerFlag) {
                            reader.nextTag();
                            System.out.println("Manufacturer: " + StAXParserHelper.getTextStAXContext(reader, "a"));
                            manufacturerFlag = false;
                        }
                        if (scaleFlag) {
                            reader.nextTag();
                            System.out.println("Scale: " + StAXParserHelper.getTextStAXContext(reader, "a"));
                            scaleFlag = false;
                        }
                        if (releasedDateFlag) {
                            if (reader.next() == XMLStreamConstants.START_ELEMENT) {
                                System.out.println("Released Date: ");
                                reader.next();
                                System.out.print(reader.getText());
                                reader.nextTag();
                                reader.next();
                                System.out.print(reader.getText());
                            } else {
                                System.out.println("Released Date: " + reader.getText());
                            }

                            releasedDateFlag = false;
                        }
                        if (seriesFlag) {
                            reader.nextTag();
                            System.out.println("Series: " + StAXParserHelper.getTextStAXContext(reader, "a"));
                            seriesFlag = false;
                        }
                        if (listPriceFlag) {
                            reader.nextTag();
                            System.out.println("List Price:" + StAXParserHelper.getTextStAXContext(reader, "span"));
                            listPriceFlag = false;
                        }
                        if (salesPriceFlag) {
                            reader.nextTag();
                            reader.nextTag();
                            System.out.println("Sale Price: " + StAXParserHelper.getTextStAXContext(reader,
                                    "span") + " yen");
                            salesPriceFlag = false;
                        }
                        if (itemCodeFlag) {
                            System.out.println("Item code: " + StAXParserHelper.getTextStAXContext(reader, tagName));
                            itemCodeFlag = false;
                        }
                    }
                }

                if (tagName.equals("a")) {
                    String title_attribute = StAXParserHelper.getNodeStAXValue(reader, tagName, "", "title");

                    if (title_attribute != null) {
                        if (title_attribute.equals("List all Images")) {
                            String listAllImageUrl = StAXParserHelper.getNodeStAXValue(reader,
                                    tagName, "", "href");

                            System.out.println("List all images: https://www.1999.co.jp" + listAllImageUrl);
                            modelCrawl.crawlModelImage(listAllImageUrl);
                        }
                    }
                }
            }
        }
    }

    public void parseModelImage(StreamSource ss) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory = XMLUltimate.setProperty(factory);
        XMLStreamReader reader = null;

        reader = factory.createXMLStreamReader(ss);

        while (reader.hasNext()) {
            int cursor = reader.next();

            if (cursor == XMLStreamConstants.START_ELEMENT) {
                String tagName = reader.getLocalName();

                if (tagName.equals("img")) {
                    String imgSrc = StAXParserHelper.getNodeStAXValue(reader, tagName, "", "src");

                    if (imgSrc != null) {
                        System.out.println("");
                        System.out.print("Image: " + "https://www.1999.co.jp" + imgSrc);
                    }

                    String imagetype = StAXParserHelper.getNodeStAXValue(reader, tagName, "", "title");

                    if (imagetype != null) {
                        if (imagetype.contains("Package")) {
                            System.out.print(" - Category: Package");
                        }
                        if (imagetype.contains("Item picture")) {
                            System.out.print(" - Category: Item picture");
                        }
                        if (imagetype.contains("Other picture")) {
                            System.out.print(" - Category: Other picture");
                        }
                        if (imagetype.contains("Contents")) {
                            System.out.print(" - Category: Contents");
                        }
                        if (imagetype.contains("About item")) {
                            System.out.print(" - Category: About item");
                        }
                        if (imagetype.contains("Color")) {
                            System.out.print(" - Category: Color");
                        }
                        if (imagetype.contains("Assembly guide")) {
                            System.out.print(" - Category: Assembly guide");
                        }
                    }
                }
            }
        }

        System.out.println("");
    }

    public int parsePageCount(StreamSource ss) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory = XMLUltimate.setProperty(factory);
        XMLStreamReader reader = null;

        reader = factory.createXMLStreamReader(ss);

        int lastPage = 0;

        while (reader.hasNext()) {
            int cursor = reader.next();

            if (cursor == XMLStreamConstants.START_ELEMENT) {
                String tagName = reader.getLocalName();

                if (tagName.equals("a")) {
                    lastPage = Integer.parseInt(StAXParserHelper.getTextStAXContext(reader, tagName));
                }
            }
        }

        return lastPage;
    }
}

