import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Map;
import java.util.TreeMap;

public class CurrentPage {

    public static Map<String, Integer> getTest() {
        Map<String, Integer> result = new TreeMap<>();
        try {
            Document doc = Jsoup.connect("https://www.svetlovka.ru/").get();
            Elements elements = doc.select("body");
            for (Element el : elements) {
                //System.out.println(el.text());
                result = Morph.getMorphMap(el.text());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        getTest().forEach((p,l) -> System.out.println(p + " - " + l));
    }
}
