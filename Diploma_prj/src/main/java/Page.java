import org.hibernate.Session;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ForkJoinPool;

public class Page {

    private static final String userAgent = "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.1 Safari/605.1.15";
    private static final String referer = "https://www.google.com/";

    private final Connector connector;
    private Set<String> fullSet;
    private String url;

    public Page(String url) {
        this.fullSet = new TreeSet<>();
        this.connector = new Connector();
        this.url = url;
    }

    public Set<String> getFullSet() {
        return fullSet;
    }

    public void setFullSet(Set<String> fullSet) {
        this.fullSet = fullSet;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<String> getHrefsOnPage(String url) {
        Set<String> hrefSet = new HashSet<>();
        Document doc;
        try {
            Connection.Response response = Jsoup
                    .connect(url)
                    .userAgent(userAgent)
                    .referrer(referer)
                    .execute();
            doc = response.parse();
            Elements elements = doc.select("a[href^=/]");
            int statusCode = response.statusCode();

            addToBase(url, statusCode, doc);

            for (Element href : elements) {
                String trueHref = href.attr("abs:href");
                if (!trueHref.matches("\\S+(?:jpg|jpeg|png|pdf|doc|PDF|xlsx|JPG|docx|eps)$") & !trueHref.contains(" ")) {
                    hrefSet.add(trueHref);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (fullSet.isEmpty()) {
            fullSet.addAll(hrefSet);
        }
        return hrefSet;
    }

    public void addToBase(String path, int statusCode, Document document) {
        StringBuilder content = new StringBuilder();
        try {
            content.append(document.outerHtml());
            connector.makeTransaction(path, statusCode, content.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Set<Set<String>> getHrefsOfHrefs(Set<String> hrefsSet) {
        return new ForkJoinPool().invoke(new NewTask(this, hrefsSet));
    }

    public void findNewHrefs(Set<Set<String>> setsOfSet) {
        Set<Set<String>> setOfBuffers = new HashSet<>(setsOfSet);
        Set<String> newHrefs = new HashSet<>();
        for (Set<String> hrefsSet : setOfBuffers) {
            Set<String> hrefs = new HashSet<>(hrefsSet);
            for (String href : hrefs) {
                if (fullSet.contains(href)) {
                    hrefsSet.remove(href);
                }
            }
            newHrefs.addAll(hrefsSet);
            fullSet.addAll(newHrefs);
        }
        if (!newHrefs.isEmpty()) {
            findNewHrefs(getHrefsOfHrefs(newHrefs));
        }
    }

    public static void main(String[] args) {
        Page page = new Page("https://www.svetlovka.ru/");
        Set<String> mainPage = page.getHrefsOnPage(page.getUrl());
        var test = page.getHrefsOfHrefs(mainPage);
        page.findNewHrefs(test);
    }
}