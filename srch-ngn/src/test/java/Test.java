import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Set;
import java.util.TreeSet;

public class Test {

    public static String userAgent= "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.1 Safari/605.1.15";
    public static String referer = "https://www.google.com/";
    public static String url = "https://www.svetlovka.ru";
    public static Set<String> hrefsSet = new TreeSet<>();

//    public static Set<String> getSetHrefs (String url) {
//        try {
//            Document doc = Jsoup.connect(url)
//                    .userAgent(userAgent)
//                    .referrer(referer)
//                    .get();
//            Elements hrefs = doc.select("a[href^=/]");
//            for (Element href : hrefs) {
//                String line = href.attr("href");
//                hrefsSet.add(line);
//            }
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        String finalUrl = url;
//        hrefsSet.forEach(l -> getSetHrefs(finalUrl + l));
//        url = "";
//        return hrefsSet;
//
//    }

    public static void getContext(String href) {
        try {
            Connection.Response response = Jsoup.connect(url + href).userAgent(userAgent).referrer(referer).execute();
            System.out.println(response.statusCode());
            Elements test = response.parse().select("a[href^=/]");

            test.forEach(line -> {
                String text = line.attr("href");
                if (!text.matches("\\.jpg|\\.png|\\.PNG") | !text.endsWith(".png")) {
                    hrefsSet.add(line.attr("href"));
                }

            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void print(){

    }

    public static void main(String[] args) {
        String text = "/upload/img_books/Книжный лабиринт для подростков (12-17 лет).pdf";
        System.out.println(text.matches("/\\S(?:jpg|jpeg|png|pdf)$"));
        System.out.println(text.matches(" "));
//        getContext("/");
//        hrefsSet.forEach(System.out::println);
    }
}
