import org.hibernate.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class Index {


//    public static String getHeadString(String path) {
//        Document doc = Jsoup.parse(path);
//        StringBuilder head = new StringBuilder();
//        Elements elements = doc.select("head");
//        for(Element element : elements) {
//            head.append(element.text());
//        }
//        return head.toString();
//    }
//    public static String getBodyString(String path) {
//        Document doc = Jsoup.parse(path);
//        StringBuilder body = new StringBuilder();
//        Elements elements = doc.select("body");
//        for(Element element : elements) {
//            body.append(element.text());
//        }
//        return body.toString();
//    }

    public static String getCSSString(String path, String css) {
        Document doc = Jsoup.parse(path);
        StringBuilder body = new StringBuilder();
        Elements elements = doc.select(css);
        for(Element element : elements) {
            body.append(element.text());
        }
        return body.toString();
    }
    public static Map<String, Float> getRankMap(String text, float weight) {
        Map<String, Float> wr = new Hashtable<>();
        Map<String, Integer> tempMap =  Morph.getMorphMap(text);
        for(var line : tempMap.entrySet()) {
            wr.put(line.getKey(), line.getValue() * weight);
        }
        return wr;
    }
}
