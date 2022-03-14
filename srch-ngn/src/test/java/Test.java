import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Test {

    public static String userAgent= "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.1 Safari/605.1.15";
    public static String referer = "https://www.google.com/";
    public static String url = "https://www.lenta.ru";

    public static Set<String> getSetHrefs (String url) {
        Set<String> hrefsSet = new HashSet<>();
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent(userAgent)
                    .referrer(referer)
                    .get();

            Elements hrefs = doc.select("a[href]");
            for (Element href : hrefs) {

                //System.out.println(href.attr("href"));
                String toCorrect = href.attr("href");
                if (toCorrect.length() > 1 & toCorrect.charAt(0) == '/') {
                    hrefsSet.add(toCorrect);
                }



            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return hrefsSet;

    }

    public static void main(String[] args) {

        Set<String> test = getSetHrefs(url);
        test.stream().sorted().forEach(System.out :: println);

    }
}
