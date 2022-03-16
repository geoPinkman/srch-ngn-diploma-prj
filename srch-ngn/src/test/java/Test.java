import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Test {

    public static String userAgent= "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.1 Safari/605.1.15";
    public static String referer = "https://www.google.com/";
    public static String url = "https://www.svetlovka.ru";

    public static Set<String> getSetHrefs (String url) {
        Set<String> hrefsSet = new HashSet<>();

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent(userAgent)
                    .referrer(referer)
                    .get();
            Elements hrefs = doc.select("a[href^=/]");
            hrefs.forEach(line -> hrefsSet.add(line.attr("href")));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return hrefsSet;

    }

    public static void main(String[] args) {

        getSetHrefs(url).stream().sorted().forEach(System.out::println);


    }
}
