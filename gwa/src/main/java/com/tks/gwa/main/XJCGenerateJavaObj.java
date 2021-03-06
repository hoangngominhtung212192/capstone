package com.tks.gwa.main;

//import com.tks.gwa.crawler.ModelCrawl;
//import com.tks.gwa.utils.StringHelper;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.TimeZone;
//import java.util.concurrent.TimeUnit;
//
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

public class XJCGenerateJavaObj {

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
//        sc.forcePackageName("com.tks.gwa.jaxb.article");
//        File schema = new File("src/main/webapp/schema/articles.xsd");
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
//        printSimilarity("", "");
//        printSimilarity("Tung", "tugg");
//        printSimilarity("Khanh", "Kanhh");
//        printSimilarity("Sang", "Angs");
//        printSimilarity("Tung", "Tung Hoang");
//        printSimilarity("Khanh dep trai", "Khanh dep gai");
//        printSimilarity("Tung dep trai", "Sang dep gai");
//        printSimilarity("47/2018", "47/2011");
//        printSimilarity("47/2018", "AB/CDEF");
//        printSimilarity("47/2018", "4B.CDEFG");
//        printSimilarity("47/2018", "AB.CDEFG");
//        printSimilarity("kitten", "sitting");

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        try {
//            Date startDate = sdf.parse("2018-11-08 04:19:20");
//
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(startDate);
//            cal.add(Calendar.HOUR_OF_DAY, 2);
//            Date newDate = cal.getTime();
//            System.out.println("New date: " + sdf.format(newDate));
//
//            Date now = new Date();
//            System.out.println("Now: " + sdf.format(now));
//
//            long diffInMinutes = newDate.getTime() - now.getTime();
//
//            int convertedSecond = (int) TimeUnit.SECONDS.convert(diffInMinutes, TimeUnit.MILLISECONDS);
//
//            Date convertedDate = new Date(convertedSecond * 1000L);
//            sdf = new SimpleDateFormat("HH:mm:ss");
//            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
//            String convertTime = sdf.format(convertedDate);
//
//            System.out.println("Difference: " + convertTime);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        String s = "<p><script type=\"text/javascript\">\n" +
                "amzn_assoc_placement = \"adunit0\";\n" +
                "amzn_assoc_search_bar = \"false\";\n" +
                "amzn_assoc_tracking_id = \"gunplacontrib-20\";\n" +
                "amzn_assoc_ad_mode = \"manual\";\n" +
                "amzn_assoc_ad_type = \"smart\";\n" +
                "amzn_assoc_marketplace = \"amazon\";\n" +
                "amzn_assoc_region = \"US\";\n" +
                "amzn_assoc_title = \"Shop this tutorial:\";\n" +
                "amzn_assoc_linkid = \"e2576ae4e2206245367b0ecfbcc71015\";\n" +
                "amzn_assoc_asins = \"B0778YPL6R,B002MB61RQ,B001003W8Q,B000FODYHW\";\n" +
                "</script></p>";

        // DOTALL option, replace from string to string include new line character (\n)
        s = s.replaceAll("(?s)<script.*</script>", "");
        System.out.println(s);
    }
}
