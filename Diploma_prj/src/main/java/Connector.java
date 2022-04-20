
import daoClasses.DaoField;
import daoClasses.DaoIndex;
import daoClasses.DaoLemma;
import daoClasses.DaoPage;
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


    public void addPage(DaoPage page) {
        Transaction tx = session.beginTransaction();
        DaoPage daoPage = new DaoPage();
        daoPage.setPath(page.getPath());
        daoPage.setCode(page.getCode());
        daoPage.setContent(page.getContent());
        List list = session.createQuery("from DaoPage where path = :path").setParameter("path", page.getPath()).list();
        if (list.isEmpty()) {
            session.saveOrUpdate(daoPage);
        }
        tx.commit();
    }

    public void saveLemma(String lemma) {
        DaoLemma daoLemma = new DaoLemma();
        List list = session.createQuery("select frequency from DaoLemma where lemma = :lemma")
                .setParameter("lemma", lemma)
                .getResultList();
        Transaction tx = session.beginTransaction();
        if (!list.isEmpty()) {
            int fr = (int)list.get(0);
            session.createQuery("update DaoLemma set frequency = :newFrequency where lemma = :lemma")
                    .setParameter("lemma", lemma)
                    .setParameter("newFrequency", ++fr)
                    .executeUpdate();

        } else {
            daoLemma.setFrequency(1);
            daoLemma.setLemma(lemma);
            session.save(daoLemma);
        }
        tx.commit();
    }

    public DaoLemma getLemma(String lemma) {
        List list = session.createQuery("from DaoLemma where lemma = :lemma")
                .setParameter("lemma", lemma).getResultList();
        DaoLemma daoLemma = (DaoLemma) list.get(0);
        return daoLemma;
    }

    public DaoPage getPage(String path) {
        List list = session.createQuery("from DaoPage where path = :path")
                .setParameter("path", path).getResultList();
        DaoPage daoPage = (DaoPage) list.get(0);
        return daoPage;
    }


    public void addIndexes(DaoPage page, DaoLemma lemma, Float rank) {
        DaoIndex daoIndex = new DaoIndex();
        daoIndex.setPage(page);
        daoIndex.setLemma(lemma);
        daoIndex.setRank(rank);
        session.save(daoIndex);
    }

    public List<String> getAllPaths() {
        List daoPageSet;
        daoPageSet = session.createQuery("select path from DaoPage").getResultList();
        return daoPageSet;
    }

    public Map<String, Float> getSelectorFields() {
        Map<String, Float> fieldsMap = new HashMap<>();
        List<DaoField> fields = session.createQuery("from DaoField").getResultList();

        for(DaoField field : fields) {
            fieldsMap.put(field.getSelector(), field.getWeight());
        }
        return fieldsMap;
    }

    public void closeSession() {
        session.close();
        System.out.println("Session closed");
    }



}
