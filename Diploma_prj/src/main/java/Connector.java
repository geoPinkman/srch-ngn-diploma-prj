import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Connector {

    private StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
    private Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
    private SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
    private Session session;

    public Session getSession() {
        this.session = sessionFactory.openSession();
        return session;
    }
    public void makeTransaction(String path, int code, String content) {
        Transaction tx = session.beginTransaction();
        DaoPage daoPage = new DaoPage();
        daoPage.setPath(path);
        daoPage.setCode(code);
        daoPage.setContent(content);
        List<DaoPage> list = session.createQuery("from DaoPage where path = :path").setParameter("path", path).getResultList();
        if (list.isEmpty()) {
            session.saveOrUpdate(daoPage);
            tx.commit();
        }
        session.close();
    }

    public String getContent(String url) {
        List listOfPages = session.createQuery("from DaoPage where path = :path").setParameter("path", url).getResultList();
        DaoPage page;
        page = (DaoPage) listOfPages.get(0);
        return page.getContent();
    }

    public List<String> getAllPaths() {
        List daoPageSet;
        daoPageSet = session.createQuery("select path from DaoPage").getResultList();
        return daoPageSet;
    }

    public static void main(String[] args) {
        Connector connector = new Connector();
        connector.getSession();
        //System.out.println(connector.getAllPaths().size());
        //System.out.println(connector.getContent("https://www.svetlovka.ru/what-see/vr-360/"));
        String head = Index.getCSSString(connector.getContent("https://www.svetlovka.ru/"), "head");
        String body = Index.getCSSString(connector.getContent("https://www.svetlovka.ru/"), "body");
//        Morph.getMorphMap(head).forEach((l,p) -> System.out.println(l + " - " + p));
//        System.out.println("***");
//        Morph.getMorphMap(body).forEach((l,p) -> System.out.println(l + " - " + p));
        Index.getRankMap(body,0.8f).forEach((l,p) -> System.out.println(l + " - " + p));

    }
}
