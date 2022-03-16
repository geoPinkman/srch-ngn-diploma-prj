import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class GetAPage {

    private static final String userAgent= "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.1 Safari/605.1.15";
    private static final String referer = "https://www.google.com/";
    private static String url;// = "https://www.google.com";

    public GetAPage(String url) {
        GetAPage.url = url;
    }

    private Set<String> getSetHrefs (String url) {
        Set<String> hrefsSet = new HashSet<>();
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent(userAgent)
                    .referrer(referer)
                    .get();

            //Elements hrefs = doc.select("a[href^=/]");
            Elements hrefs = doc.select("a[href^=/]");
            hrefs.forEach(line -> hrefsSet.add(line.attr("href")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return hrefsSet;
    }
    private String getContext(String href) {
        StringBuilder context = new StringBuilder();
        try {
            Document doc = Jsoup.connect(href)
                    .userAgent(userAgent)
                    .referrer(referer)
                    .get();
            Elements page = doc.getAllElements();
            for (Element text : page) {
                context.append(text);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return context.toString().replaceAll("\n", "");
    }
    @NotNull
    public Map<String, String> getSiteMap (@NotNull Set<String> set) {
        Map<String, String> map = new Hashtable<>();
        Set<String> resultSet = new HashSet<>();
        set.forEach(line -> resultSet.addAll(getSetHrefs(url + line)));
        resultSet.forEach(line -> {
            if (!line.contains(".")) {
                map.put(line, "");
            }
        });
        return map;
    }
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        GetAPage page = new GetAPage("https://www.svetlovka.ru");
        page.getSiteMap(page.getSetHrefs(url)).keySet().forEach(System.out::println);
        System.out.println(System.currentTimeMillis() - start + " ms");
    }
}
