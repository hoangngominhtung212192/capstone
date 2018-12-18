package com.tks.gwa.parser;

import com.tks.gwa.crawler.ModelCrawl;
import com.tks.gwa.jaxb.Image;
import com.tks.gwa.jaxb.Model;
import com.tks.gwa.service.ModelService;
import com.tks.gwa.transformer.ModelTransformer;
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
public class ModelStAXParser {

    @Autowired
    private ModelCrawl modelCrawl;

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelTransformer modelTransformer;

    private int newRecords;

    public ModelStAXParser() {
        this.newRecords = 0;
    }

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

    @Transactional
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
        boolean janCodeFlag = false;

        Model jaxb_model = new Model();
        List<Image> jaxb_list_images = new ArrayList<Image>();

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
                            name = name.replaceAll("%amp;", "&");

                            // set model name
                            jaxb_model.setName(name);
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
                            if (td_detail_content.equals("JAN code")) {
                                janCodeFlag = true;
                            }
                        }
                    }

                    if (td_class != null && td_style == null) {
                        if (manufacturerFlag) {
                            reader.nextTag();
                            String manufacturer = StAXParserHelper.getTextStAXContext(reader, "a");

                            jaxb_model.setManufacturer(manufacturer);
                            System.out.println("Manufacturer: " + manufacturer);
                            manufacturerFlag = false;
                        }
                        if (scaleFlag) {
                            reader.nextTag();
                            String scale = StAXParserHelper.getTextStAXContext(reader, "a");

                            int cursorScale = reader.next();
                            if (cursorScale == XMLStreamConstants.CHARACTERS) {
                                if (reader.getText().contains(",")) {
                                    reader.nextTag();
                                    scale += " " + StAXParserHelper.getTextStAXContext(reader, "a");
                                }
                            }
                            jaxb_model.setProductSeries(scale);
                            System.out.println("Scale: " + scale);
                            scaleFlag = false;
                        }
                        if (releasedDateFlag) {
                            if (reader.next() == XMLStreamConstants.START_ELEMENT) {
                                System.out.println("Released Date: ");
                                reader.next();
                                String releasedDate = reader.getText();
                                System.out.print(reader.getText());

                                reader.nextTag();
                                reader.next();
                                releasedDate += reader.getText();
                                System.out.print(reader.getText());

                                jaxb_model.setReleasedDate(releasedDate);
                            } else {
                                jaxb_model.setReleasedDate(reader.getText());
                                System.out.println("Released Date: " + reader.getText());
                            }

                            releasedDateFlag = false;
                        }
                        if (seriesFlag) {
                            reader.nextTag();
                            String series = StAXParserHelper.getTextStAXContext(reader, "a");

                            jaxb_model.setSeriesTitle(series);
                            System.out.println("Series: " + series);
                            seriesFlag = false;
                        }
                        if (listPriceFlag) {
                            reader.nextTag();
                            String price = StAXParserHelper.getTextStAXContext(reader, "span");

                            jaxb_model.setPrice(price);
                            System.out.println("List Price:" + price);
                            listPriceFlag = false;
                        }
                        if (salesPriceFlag) {
                            reader.nextTag();
                            reader.nextTag();
                            String price = StAXParserHelper.getTextStAXContext(reader, "span") + " yen";

                            jaxb_model.setPrice(price);
                            System.out.println("Sale Price: " + price);
                            salesPriceFlag = false;
                        }
                        if (janCodeFlag) {
                            String code = StAXParserHelper.getTextStAXContext(reader, tagName);

                            jaxb_model.setCode(code);
                            System.out.println("JAN code: " + code);
                            janCodeFlag = false;
                        }
                        if (itemCodeFlag) {
                            String code = StAXParserHelper.getTextStAXContext(reader, tagName);

                            jaxb_model.setCode(code);
                            System.out.println("Item code: " + code);
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
                            jaxb_list_images = modelCrawl.crawlModelImage(listAllImageUrl);
                            jaxb_model.setImage(jaxb_list_images);
                        }
                    }
                }

                if (tagName.equals("div")) {
                    String div_class = StAXParserHelper.getNodeStAXValue(reader, tagName, "", "class");

                    if (div_class != null) {
                        if (div_class.equals("bikou marginTop10")) {
                            String description = "";

                            boolean flag = false;

                            while (!flag) {
                                int cursorDes = reader.next();

                                if (cursorDes == XMLStreamConstants.START_ELEMENT) {
                                    String tagNameDes = reader.getLocalName();

                                    if (tagNameDes.equals("div")) {
                                        String tempDivClassFlag = StAXParserHelper.getNodeStAXValue(reader,
                                                tagNameDes, "", "class");

                                        if (tempDivClassFlag != null) {
                                            description += "</div>";
                                            flag = true;
                                        } else {
                                            description += "<" + tagNameDes + ">";
                                        }
                                    } else {
                                        description += "<" + tagNameDes + ">";
                                    }
                                }
                                if (cursorDes == XMLStreamConstants.END_ELEMENT) {
                                    description += "</" + reader.getLocalName() + ">";
                                }
                                if (cursorDes == XMLStreamConstants.CHARACTERS) {
                                    description += reader.getText();
                                }
                            }

                            description = description.replaceAll("%amp;", "&");

                            jaxb_model.setDescription(description);
                            System.out.println("Description: " + description);
                        }
                    }
                }
            }
        }

        // begin progress saving to database
        com.tks.gwa.entity.Model entity_model = modelTransformer.convertToEntity(jaxb_model);
        com.tks.gwa.entity.Model newEntityModel = modelService.createNewModel(entity_model, "crawl");

        if (newEntityModel != null) {
            if (newEntityModel.getMessage() == null) {
                newRecords += 1;
            }
        }
    }

    public List<Image> parseModelImage(StreamSource ss) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory = XMLUltimate.setProperty(factory);
        XMLStreamReader reader = null;

        reader = factory.createXMLStreamReader(ss);
        List<Image> jaxb_list_images = new ArrayList<Image>();

        while (reader.hasNext()) {
            int cursor = reader.next();

            if (cursor == XMLStreamConstants.START_ELEMENT) {
                String tagName = reader.getLocalName();

                if (tagName.equals("img")) {
                    String imgSrc = StAXParserHelper.getNodeStAXValue(reader, tagName, "", "src");

                    Image jaxb_image = null;

                    if (imgSrc != null) {
                        jaxb_image = new Image();

                        String imgUrl = "https://www.1999.co.jp" + imgSrc;

                        jaxb_image.setUrl(imgUrl);
                        System.out.println("");
                        System.out.print("Image: " + imgUrl);
                    }

                    String imagetype = StAXParserHelper.getNodeStAXValue(reader, tagName, "", "title");

                    if (imagetype != null) {
                        if (imagetype.contains("Package")) {
                            jaxb_image.setType("Package");
                            System.out.print(" - Category: Package");
                        }
                        if (imagetype.contains("Item picture")) {
                            jaxb_image.setType("Item picture");
                            System.out.print(" - Category: Item picture");
                        }
                        if (imagetype.contains("Other picture")) {
                            jaxb_image.setType("Other picture");
                            System.out.print(" - Category: Other picture");
                        }
                        if (imagetype.contains("Contents")) {
                            jaxb_image.setType("Contents");
                            System.out.print(" - Category: Contents");
                        }
                        if (imagetype.contains("About item")) {
                            jaxb_image.setType("About item");
                            System.out.print(" - Category: About item");
                        }
                        if (imagetype.contains("Color")) {
                            jaxb_image.setType("Color");
                            System.out.print(" - Category: Color");
                        }
                        if (imagetype.contains("Assembly guide")) {
                            jaxb_image.setType("Assembly guide");
                            System.out.print(" - Category: Assembly guide");
                        }

                        jaxb_list_images.add(jaxb_image);
                    }
                }
            }
        }

        System.out.println("");
        return jaxb_list_images;
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

    public int getNewRecords() {
        return newRecords;
    }

    public void setNewRecords(int newRecords) {
        this.newRecords = newRecords;
    }
}

