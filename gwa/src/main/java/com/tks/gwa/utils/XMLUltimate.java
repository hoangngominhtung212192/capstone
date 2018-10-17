package com.tks.gwa.utils;

import com.tks.gwa.handler.MyErrorHandler;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.stream.XMLInputFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

public class XMLUltimate {

//    public static <T> boolean validateSource(T t) throws JAXBException, SAXException, IOException {
//        JAXBContext context = JAXBContext.newInstance(ModelJaxB.class);
//        JAXBSource source = new JAXBSource(context, t);
//
//        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//        Schema schema = sf.newSchema(new File("src/main/webapp/pages/schema/Models.xsd"));
//
//        Validator validator = schema.newValidator();
//        validator.setErrorHandler(new MyErrorHandler());
//        validator.validate(source);
//
//        return true;
//    }

    public static <T> String marshalData(T t) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(t.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        StringWriter sw = new StringWriter();

        marshaller.marshal(t, sw);

        return sw.toString();
    }

    public static XMLInputFactory setProperty(XMLInputFactory factory) {

        // turn off implementation specific validation
        factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
        // parser to replace internal entity references with their replacement text and report them as characters
        factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);
        // requires the parser to coalesce adjacent character data sections
        factory.setProperty(XMLInputFactory.IS_COALESCING, true);
        // requires the parser to resolve external parsed entities
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);

        return factory;
    }
}
