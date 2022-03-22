import org.hibernate.Transaction;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

public class GetPageMap {

    private static final String userAgent = "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.1 Safari/605.1.15";
    private static final String referer = "https://www.google.com/";

    private static final AtomicInteger id = new AtomicInteger(1);

    private final Set<String> setHrefs;
    private final Map<String, String> usedHrefs;
    private final String url;

    public GetPageMap(String url) {
        this.url = url;
        this.usedHrefs = new Hashtable<>();
        this.setHrefs = new TreeSet<>();
    }

    public void getHrefsOfPage(String href) {
        StringBuilder context = new StringBuilder();
        Set<String> buffer = new TreeSet<>();
        int status = 0;
        buffer.add(href);
        try {
            Connection.Response response = Jsoup
                    .connect(url + href)
                    .userAgent(userAgent)
                    .referrer(referer)
                    .execute();
            Elements page = response.parse().getAllElements();
            status = response.statusCode();
            page.forEach(line -> {
                context.append(line);
                String hrefsOfPage = line.select("a[href^=/]").attr("href");
                if (!hrefsOfPage.matches("/\\S+(?:jpg|jpeg|png|pdf|doc|PDF|xlsx|JPG|docx|eps)$") & !hrefsOfPage.contains(" ")) {
                    buffer.add(hrefsOfPage);
                }
            });
            usedHrefs.put(href, context.toString().replaceAll("\n", ""));
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
                    //new DaoPage(hrefInBuffer, status, context.toString());
                    System.out.println(hrefInBuffer);
                    //new ForkJoinPool().invoke(new TestFork(this, hrefInBuffer));
                   Transaction tx =  new Connector().getSession().beginTransaction();
                    DaoPage page = new DaoPage();
                    page.setId(id.getAndIncrement());
                    page.setPath(hrefInBuffer);
                    page.setStatus(status);
                    page.setContext(context.toString());
                    tx.commit();
//                    Connector.session.update(new DaoPage(hrefInBuffer, status, context.toString()));
//                    //Connector.session.flush();
//                    Connector.session.close();
                    getHrefsOfPage(hrefInBuffer);
                }
            }
        }
    }

    public static void main(String[] args) {
        GetPageMap pageMap = new GetPageMap("https://www.svetlovka.ru");
        pageMap.getHrefsOfPage("/");
    }
}
