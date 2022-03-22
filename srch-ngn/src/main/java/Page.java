import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Page {

    public static Set<String> fullSet = new HashSet<>();

    //public static String url = "";
    public static String userAgent = "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.1 Safari/605.1.15";
    public static String referer = "https://www.google.com/";

    public static Set<String> getHrefsOnPage(String url) {
        Set<String> hrefSet = new TreeSet<>();
        try {
            Connection.Response response = Jsoup
                    .connect(url)
                    .userAgent(userAgent)
                    .referrer(referer)
                    .execute();
            Document doc = response.parse();
            Elements elements = doc.select("a[href^=/]");
            for (Element href : elements) {
                String trueHref = href.attr("abs:href");
                if (!trueHref.matches("\\S+(?:jpg|jpeg|png|pdf|doc|PDF|xlsx|JPG|docx|eps)$") & !trueHref.contains(" ")) {
                    hrefSet.add(trueHref);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        //fullSet.addAll(hrefSet);
        return hrefSet;
    }

    public static Set<Set<String>> getHrefsOfHrefs(Set<String> hrefsSet) {
        Set<Set<String>> setOfEachHref = new HashSet<>();
        for (String href : hrefsSet) {
                setOfEachHref.add(getHrefsOnPage(href));
        }
        return setOfEachHref;
    }

    public static void findNewHrefs(Set<Set<String>> setsOfSet) {
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
        newHrefs.forEach(System.out::println);
        if (!newHrefs.isEmpty()) {
            findNewHrefs(getHrefsOfHrefs(newHrefs));
        }
    }

    public static void main(String[] args) {
        fullSet.addAll(getHrefsOnPage("https://www.svetlovka.ru"));
        findNewHrefs(getHrefsOfHrefs(fullSet));
        //fullSet.forEach(System.out::println);
        System.out.println(fullSet.size());
    }
}
