//package com.tks.gwa.main;
//
//import com.sun.codemodel.JCodeModel;
//import com.sun.tools.xjc.api.ErrorListener;
//import com.sun.tools.xjc.api.S2JJAXBModel;
//import com.sun.tools.xjc.api.SchemaCompiler;
//import com.sun.tools.xjc.api.XJC;
//import org.xml.sax.InputSource;
//import org.xml.sax.SAXParseException;
//
//import java.io.File;
//import java.io.IOException;
//
//public class XJCGenerateJavaObj {
//
//    public static void main(String[] args) {
//        String output = "src/main/java";
//        SchemaCompiler sc = XJC.createSchemaCompiler();
//        sc.setErrorListener(new ErrorListener() {
//            @Override
//            public void error(SAXParseException e) {
//                System.out.println("ERROR:");
//                e.printStackTrace();
//            }
//
//            @Override
//            public void fatalError(SAXParseException e) {
//                System.out.println("FATAL ERROR:");
//                e.printStackTrace();
//            }
//
//            @Override
//            public void warning(SAXParseException e) {
//                System.out.println("WARNING:");
//                e.printStackTrace();
//            }
//
//            @Override
//            public void info(SAXParseException e) {
//                System.out.println("Error:");
//                e.printStackTrace();
//            }
//        });
//
//        sc.forcePackageName("com.tks.gwa.jaxb");
//        File schema = new File("src/main/webapp/schema/models.xsd");
//        InputSource is = new InputSource(schema.toURI().toString());
//        sc.parseSchema(is);
//        S2JJAXBModel model = sc.bind();
//        JCodeModel code = model.generateCode(null, null);
//
//        try {
//            code.build(new File(output));
//            System.out.println("Successfully !!!");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
