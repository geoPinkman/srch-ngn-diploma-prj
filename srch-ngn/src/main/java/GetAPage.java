import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class GetAPage {

    public static String userAgent= "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.1 Safari/605.1.15";
    public static String referer = "https://www.google.com/";
    public static String url = "https://www.google.com";


    public static Set<String> getSetHrefs (String url) {
       Set<String> hrefsSet = new HashSet<>();
       try {
            Document doc = Jsoup.connect(url)
                    .userAgent(userAgent)
                    .referrer(referer)
                    .get();

            Elements hrefs = doc.select("a[href]");
            for (Element href : hrefs) {
                String toCorrect = href.attr("href");
                if (toCorrect.charAt(0) == '/' & toCorrect.length() > 1) {
                    hrefsSet.add(toCorrect);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return hrefsSet;
    }
    public static Set<String> getSiteMap (Set<String> set) {
        Set<String> resultSet = new HashSet<>(getSetHrefs(url));
        set.forEach(line ->
                getSetHrefs(url + line).forEach(l ->
                        resultSet.add(l)));
        return resultSet;
    }

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        System.out.println(getSetHrefs(url).size());
        getSetHrefs(url).stream().sorted().forEach(System.out :: println);
        getSiteMap(getSetHrefs(url)).stream().sorted().forEach(System.out::println);

        System.out.println(System.currentTimeMillis() - start + " ms");
    }
}
