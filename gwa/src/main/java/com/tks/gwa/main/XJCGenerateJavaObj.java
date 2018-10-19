package com.tks.gwa.main;
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
public class XJCGenerateJavaObj {
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

    /**
     * Calculates the similarity (a number within 0 and 1) between two strings.
     */
    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
    /* // If you have Apache Commons Text, you can use it to calculate the edit distance:
    LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
    return (longerLength - levenshteinDistance.apply(longer, shorter)) / (double) longerLength; */
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    // Example implementation of the Levenshtein Edit Distance
    // See http://rosettacode.org/wiki/Levenshtein_distance#Java
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

    public static void printSimilarity(String s, String t) {
        System.out.println(String.format(
                "%.3f is the similarity between \"%s\" and \"%s\"", similarity(s, t), s, t));
    }

    public static void main(String[] args) {
        printSimilarity("", "");
        printSimilarity("Tung", "tugg");
        printSimilarity("Khanh", "Kanhh");
        printSimilarity("Sang", "Angs");
        printSimilarity("Tung", "Tung Hoang");
        printSimilarity("Khanh dep trai", "Khanh dep gai");
        printSimilarity("Tung dep trai", "Sang dep gai");
        printSimilarity("47/2018", "47/2011");
        printSimilarity("47/2018", "AB/CDEF");
        printSimilarity("47/2018", "4B.CDEFG");
        printSimilarity("47/2018", "AB.CDEFG");
        printSimilarity("kitten", "sitting");
    }
}
