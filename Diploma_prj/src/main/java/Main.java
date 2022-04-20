import daoClasses.DaoIndex;
import daoClasses.DaoLemma;
import daoClasses.DaoPage;
import org.hibernate.Session;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileReader;
import java.util.*;

public class Main {

    private static final String userAgent = "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.1 Safari/605.1.15";
    private static final String referer = "https://www.google.com/";

    public static DaoPage getContent(String path) {
        DaoPage daoPage = new DaoPage();
        try {
            Connection.Response response = Jsoup
                    .connect(path)
                    .userAgent(userAgent)
                    .referrer(referer)
                    .execute();
            Elements page = response.parse().getAllElements();
            int statusCode = response.statusCode();
            String content = page.html();
            daoPage.setPath(path);
            daoPage.setCode(statusCode);
            daoPage.setContent(content);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return daoPage;
    }

    private static Map<String, Float> getRank(String text, float weight) {
        Map<String, Float> wr = new Hashtable<>();
        Map<String, Integer> tempMap = Morph.getMorphMap(text);
        for(var line : tempMap.entrySet()) {
            wr.put(line.getKey(), line.getValue() * weight);
        }
        return wr;
    }

    public static Map<String, Float> rankMap(String content, Map<String, Float> indexes) {
        Elements page = Jsoup.parse(content).getAllElements();
        Map<String, Float> indexMap = new Hashtable<>(indexes);
        Map<String, Float> rankMap = new TreeMap<>();
        indexMap.forEach((selector,weight) -> {
            String text = page.select(selector).text();
            Map<String, Float> ranks = Main.getRank(text, weight);
            for (Map.Entry<String, Float> lineOfMap : ranks.entrySet()) {
                String word = lineOfMap.getKey();
                Float rank = lineOfMap.getValue();
                if (!rankMap.containsKey(word)) {
                    rankMap.put(word, rank);
                } else {
                    Float tempRank = rankMap.remove(word);
                    rankMap.put(word, rank + tempRank);
                }
            }
        });
        return rankMap;
    }

    public static Set<String> getLemmasSet(Map<String, Float> rankMap) {
        return new TreeSet<>(rankMap.keySet());
    }

    public static void main(String[] args) {

        Connector connector = new Connector();
        Map<String, Float> selectors = connector.getSelectorFields();

        try {
            FileReader reader = new FileReader("hrefs.txt");
            Scanner scanner = new Scanner(reader);
            while (scanner.hasNextLine()) {
                String path = scanner.nextLine();
                DaoPage page = getContent(path);
                connector.addPage(page);
                Map<String, Float> rankMapOfLemmasOnPage = rankMap(page.getContent(), selectors);
                for (Map.Entry<String, Float> lemmas : rankMapOfLemmasOnPage.entrySet()) {
                    connector.saveLemma(lemmas.getKey());
                    DaoLemma lemma = connector.getLemma(lemmas.getKey());
                    DaoPage daoPage = connector.getPage(page.getPath());
                    connector.addIndexes(daoPage, lemma, lemmas.getValue());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        connector.closeSession();

    }

}
