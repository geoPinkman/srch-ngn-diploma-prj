import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ForkJoinPool;

public class Page {

    private static final String userAgent = "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.1 Safari/605.1.15";
    private static final String referer = "https://www.google.com/";

    private Set<String> fullSet;
    private String url;

    public Page(String url) {
        this.fullSet = new TreeSet<>();
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

    public Set<String> getHrefsOnPage(String url){
        Set<String> hrefSet = new HashSet<>();
        try {
            Document document = Jsoup
                    .connect(url)
                    .userAgent(userAgent)
                    .referrer(referer)
                    .get();
            Elements elements = document.select("a[href^=/]");

            for (Element href : elements) {
                String trueHref = href.attr("abs:href");
                if (!trueHref.matches("\\S+(?:jpg|jpeg|png|pdf|doc|PDF|xlsx|JPG|docx|eps)$") & !trueHref.contains(" ")) {
                    hrefSet.add(trueHref);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return hrefSet;
    }

    public void writeHrefsToFile(Set<String> stringSet) {
        try {
            FileWriter fileWriter = new FileWriter("hrefs.txt");
            for (String path : stringSet) {
                fileWriter.write(path + "\n");
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<String> getHrefsOfHrefs(Set<String> hrefsSet) {
        return new ForkJoinPool().invoke(new NewTask(this, hrefsSet));
    }

    public void findNewHrefs(Set<String> setsOfSet) {
        Set<String> setOfHrefs = new HashSet<>(setsOfSet);
        Set<String> newHrefs = new HashSet<>();
            for (String href : setOfHrefs) {
                if (fullSet.contains(href)) {
                    setsOfSet.remove(href);
                }
            }
            newHrefs.addAll(setsOfSet);
            fullSet.addAll(newHrefs);
            writeHrefsToFile(fullSet);
        if (!newHrefs.isEmpty()) {
            findNewHrefs(getHrefsOfHrefs(newHrefs));
        }
    }

    public static void main(String[] args) {
        Page page = new Page("https://www.svetlovka.ru/");
        Set<String> stringSet = new HashSet<>();
        try {
            FileReader fileReader = new FileReader("hrefs.txt");
            Scanner sc = new Scanner(fileReader);
            while (sc.hasNextLine()) {
                stringSet.add(sc.nextLine());
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (stringSet.isEmpty()) {
            stringSet.add(page.getUrl());
        }
        page.fullSet.addAll(stringSet);
        page.findNewHrefs(page.getHrefsOfHrefs(page.getHrefsOnPage(page.getUrl())));
    }
}