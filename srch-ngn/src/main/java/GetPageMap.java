import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class GetPageMap {

    private static final String userAgent = "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.1 Safari/605.1.15";
    private static final String referer = "https://www.google.com/";

    private Set<String> setHrefs;
    private Map<String, String> usedHrefs;
    private String url;

    public GetPageMap(String url) {
        this.url = url;
        this.usedHrefs = new Hashtable<>();
        this.setHrefs = new TreeSet<>();
    }

    public Set<String> getSetHrefs() {
        return setHrefs;
    }

    public void setSetHrefs(Set<String> setHrefs) {
        this.setHrefs = setHrefs;
    }

    public Map<String, String> getUsedHrefs() {
        return usedHrefs;
    }

    public void setUsedHrefs(Map<String, String> usedHrefs) {
        this.usedHrefs = usedHrefs;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public void getHrefsOfPage(String href) {

        Set<String> buffer = new TreeSet<>();
        try {
            Connection.Response response = Jsoup
                    .connect(url + href)
                    .userAgent(userAgent)
                    .referrer(referer)
                    .execute();
            Elements hrefs = response.parse().select("a[href^=/]");
            hrefs.forEach(line -> {
                String text = line.attr("href");
                if (!text.matches("/\\S+(?:jpg|jpeg|png|pdf|doc|PDF|xlsx|JPG|docx)$")) {
                    buffer.add(text);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Set<String> test = new TreeSet<>(buffer);
        for (String hrefsInBuffer : test) {
            if (setHrefs.contains(hrefsInBuffer) | usedHrefs.containsKey(hrefsInBuffer)) {
                buffer.remove(hrefsInBuffer);
            }
        }
        if (!buffer.isEmpty()) {
            setHrefs.addAll(buffer);
            for (String hrefInBuffer : buffer) {
                if (!usedHrefs.containsKey(hrefInBuffer)) {
                    usedHrefs.put(hrefInBuffer, getContext(hrefInBuffer));
                    //System.out.println(hrefInBuffer);
                    //System.out.println(Thread.currentThread().getName());
                    new TestFork(new GetPageMap(url), hrefInBuffer);
                    System.out.println(Thread.currentThread());
                    getHrefsOfPage(hrefInBuffer);
                }
            }
        }
        //return usedHrefs;
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

    public void print() {
        getUsedHrefs().keySet().forEach(System.out::println);
    }

    public static void main(String[] args) {
        GetPageMap test = new GetPageMap("https://www.svetlovka.ru");
        test.getHrefsOfPage("/");
        test.print();
    }
}
