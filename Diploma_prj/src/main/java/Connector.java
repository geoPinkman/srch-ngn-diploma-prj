import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

public class Connector {

    private StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
    private Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
    private SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
    private Session session;

    public Session getSession() {
        this.session = sessionFactory.openSession();
        return session;
    }
    public void makeTransactionInPageTable(String path, int code, String content) {
        getSession();
        //Transaction tx = session.beginTransaction();
        DaoPage daoPage = new DaoPage();
        daoPage.setPath(path);
        daoPage.setCode(code);
        daoPage.setContent(content);
        List list = session.createQuery("from DaoPage where path = :path").setParameter("path", path).list();
        if (list.isEmpty()) {
            session.saveOrUpdate(daoPage);
            //tx.commit();
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

    public void addFields(String name, String selector, float weight) {
        Session localSession = getSession();
        Transaction tx = localSession.beginTransaction();
        DaoField field = new DaoField();
        field.setName(name);
        field.setSelector(selector);
        field.setWeight(weight);
        localSession.save(field);
        tx.commit();
        localSession.close();
    }
    public Map<String, Float> getSelectorFields() {
        Map<String, Float> fieldsMap = new HashMap<>();
        List<DaoField> fields = session.createQuery("from DaoField ").getResultList();
        for(DaoField field : fields) {
            fieldsMap.put(field.getSelector(), field.getWeight());
        }
        return fieldsMap;
    }

    public void closeSession() {
        session.close();
        System.out.println("Session closed");
    }
    public static void main(String[] args) {
        Connector connector = new Connector();
        connector.getSession();
        Map<String, Float> fieldsMap = connector.getSelectorFields();
        List<String> paths = connector.getAllPaths();


        for (String path : paths) {
            //System.out.println(Index.getCSSString(connector.getContent(path), "head"));
            Index.getRankMap(Index.getCSSString(connector.getContent(path), "body"), 0.8f).forEach((l,p) -> System.out.println(l + " - " + p));
        }
        connector.closeSession();
    }
}
