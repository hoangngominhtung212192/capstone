package com.tks.gwa.utils;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class StAXParserHelper {

    // get attribute
    public static String getNodeStAXValue(XMLStreamReader reader, String elementName,
                                          String namespaceURI, String attrName) throws XMLStreamException {
        if (reader != null) {
            while (reader.hasNext()) {
                int cursor = reader.getEventType();
                if (cursor == XMLStreamConstants.START_ELEMENT) {
                    String tagName = reader.getLocalName();
                    if (tagName.equals(elementName)) {
                        String result = reader.getAttributeValue(namespaceURI, attrName);

                        return result;
                    }//end tagName
                }//end cursor
                reader.next();
            }//end while
        }//end if reader
        return null;
    }

    // get character text
    public static String getTextStAXContext(XMLStreamReader reader, String elementName) throws XMLStreamException {
        if (reader != null) {
            while (reader.hasNext()) {
                int cursor = reader.getEventType();
                if (cursor == XMLStreamConstants.START_ELEMENT) {
                    String tagName = reader.getLocalName();
                    if (tagName.equals(elementName)) {
                        reader.next(); //value
                        String result = reader.getText();
                        reader.nextTag();//end element
                        //String result = reader.getElementText();
                        return result;
                    }//end if tagName
                }//end if start element
                reader.next();
            }//end while
        }//end if reader
        return null;
    }
}
