import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.*;

public class GetPageMap {

    private static final String userAgent = "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.1 Safari/605.1.15";
    private static final String referer = "https://www.google.com/";
    //private static String url = "https://www.svetlovka.ru";

    private Set<String> setHrefs;
    private Map<String, Integer> usedHrefs;
    private String url;

    public GetPageMap(String url) {
        this.url = url;
        this.setHrefs = new TreeSet<>();
        this.usedHrefs = new Hashtable<>();
    }

    public Set<String> getSetHrefs() {
        return setHrefs;
    }

    public void setSetHrefs(Set<String> setHrefs) {
        this.setHrefs = setHrefs;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static int test;

    public void getHrefsOfPage(String href) {
        Set<String> buffer = new TreeSet<>();
        int code = 0;
        try {
            Connection.Response response = Jsoup.connect(url + href).userAgent(userAgent).referrer(referer).execute();
            code = response.statusCode();
            //Connection.Request = Jsoup.connect(url + href).userAgent(userAgent).referrer(referer).request();
            Elements hrefs = response.parse().select("a[href^=/]");
            hrefs.forEach(line -> {
                String text = line.attr("href");
                if (text.endsWith("/")) {
                    buffer.add(line.attr("href"));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Set<String> test = new TreeSet<>(buffer);
        for (String hrefsInBuffer : test) {
            if (setHrefs.contains(hrefsInBuffer) | usedHrefs.keySet().contains(hrefsInBuffer)) {
                buffer.remove(hrefsInBuffer);
            }
        }
        if (!buffer.isEmpty()) {
            setHrefs.addAll(buffer);
            for (String hrefInBuffer : buffer) {
                if (!usedHrefs.keySet().contains(hrefInBuffer)) {
                    usedHrefs.put(hrefInBuffer, code);
                    System.out.println(hrefInBuffer + " " + code);
                    getHrefsOfPage(hrefInBuffer);
                }
            }
        }
    }

    public String getContext(String href) {
        StringBuilder context = new StringBuilder();
        try {
            Document doc = Jsoup.connect(url + href).userAgent(userAgent).referrer(referer).get();
            Elements page = doc.getAllElements();
            page.forEach(context::append);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return context.toString().replaceAll("\n", "");
    }

    public static void main(String[] args) {
        GetPageMap parser = new GetPageMap("https://www.svetlovka.ru");
        parser.getHrefsOfPage("/");

    }

}
