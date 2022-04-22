import daoClasses.Field;
import daoClasses.Index;
import daoClasses.Lemma;
import daoClasses.Page;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.*;

public class Connector {

    private StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
    private Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
    private SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
    private Session session = sessionFactory.openSession();


    public void addPage(Page page) {
        //Transaction tx = session.beginTransaction();
        Page daoPage = new Page();
        daoPage.setPath(page.getPath());
        daoPage.setCode(page.getCode());
        daoPage.setContent(page.getContent());
        List list = session.createQuery("from Page where path = :path").setParameter("path", page.getPath()).list();
        if (list.isEmpty()) {
            session.saveOrUpdate(daoPage);
        }
        //tx.commit();
    }

    public void saveLemma(String lemma) {
        Lemma daoLemma = new Lemma();
        List list = session.createQuery("select frequency from Lemma where lemma = :lemma")
                .setParameter("lemma", lemma)
                .getResultList();

        if (!list.isEmpty()) {
            Transaction tx = session.beginTransaction();
            int fr = (int)list.get(0);
            session.createQuery("update Lemma set frequency = :newFrequency where lemma = :lemma")
                    .setParameter("lemma", lemma)
                    .setParameter("newFrequency", ++fr)
                    .executeUpdate();
            tx.commit();
        } else {
            daoLemma.setFrequency(1);
            daoLemma.setLemma(lemma);
            session.save(daoLemma);
        }

    }

    public Lemma getLemma(String lemma) {
        List list = session.createQuery("from Lemma where lemma = :lemma")
                .setParameter("lemma", lemma).getResultList();
        Lemma daoLemma = (Lemma) list.get(0);
        return daoLemma;
    }

    public Page getPage(String path) {
        List list = session.createQuery("from Page where path = :path")
                .setParameter("path", path).getResultList();
        Page page = (Page) list.get(0);
        return page;
    }

    public void addIndexes(Page page, Lemma lemma, Float rank) {
        Index index = new Index();
        index.setPage(page);
        index.setLemma(lemma);
        index.setRank(rank);
        session.save(index);
    }

    public List<String> getAllPaths() {
        List daoPageSet;
        daoPageSet = session.createQuery("select path from Page").getResultList();
        return daoPageSet;
    }

    public Map<String, Float> getSelectorFields() {
        Map<String, Float> fieldsMap = new HashMap<>();
        List<Field> fields = session.createQuery("from Field").getResultList();

        for(Field field : fields) {
            fieldsMap.put(field.getSelector(), field.getWeight());
        }
        return fieldsMap;
    }

    public void closeSession() {
        session.close();
        System.out.println("Session closed");
    }



}
