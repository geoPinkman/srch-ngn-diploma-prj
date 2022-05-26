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
import java.util.stream.Collectors;

public class Connector {

    private StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
    private Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
    private SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
    private Session session = sessionFactory.openSession();
    public static Set<String> resultSet = new HashSet<>();

    public Connector () {

    }

    public void closeSession() {
        session.close();
        System.out.println("Session closed");
    }

    public Session getSession() {
        return session;
    }

    public Map<String, Float> getSelectorFields() {
        Map<String, Float> fieldsMap = new HashMap<>();
        List<Field> fields = session.createQuery("from Field").getResultList();
        for(Field field : fields) {
            fieldsMap.put(field.getSelector(), field.getWeight());
        }
        return fieldsMap;
    }

    public void addPage(Page page) {
        Page daoPage = new Page();
        daoPage.setPath(page.getPath());
        daoPage.setCode(page.getCode());
        daoPage.setContent(page.getContent());
        List list = session.createQuery("from Page where path = :path").setParameter("path", page.getPath()).list();
        if (list.isEmpty()) {
            session.saveOrUpdate(daoPage);
        }
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
        return (Page) list.get(0);
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

    public long getPathCount() {
        return (Long) session.createQuery("select count(id) from Page as pageCount").uniqueResult();
    }

    public Map<String, Integer> getMorphsFromDB(String queryString) {
        Map<String, Integer> result = new HashMap<>();
        Set<String> uniqMorphs = Indexes.getMorphMap(queryString).keySet();
        int pageCount = (int) getPathCount();
        for (String morph : uniqMorphs) {
            Integer freq = (Integer) session.createQuery("select frequency from Lemma where lemma = :morph")
                    .setParameter("morph", morph)
                    .uniqueResult();
            if (freq != null) {
                double frq = freq / (pageCount * 1.0);
                if (frq <= 0.7) {
                    result.put(morph, freq);
                }
            }
        }
        Map<String, Integer> sortedResultByValue = result
                .entrySet()
                .stream()
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> { throw new AssertionError(); },
                        LinkedHashMap::new
                ));
        return sortedResultByValue;
    }

    public List<String> getPathListByLemma(String lemma) {
        List<String> list = session.createQuery("select p.path from Page p inner join Index i on p.id = i.page.id inner join Lemma l on i.lemma.id = l.id where l.lemma = :lemma")
                .setParameter("lemma", lemma).getResultList();
        return list;
    }

    public void testM(List<String> list) {
        
    }

    public static void main(String[] args) {
        Connector connector = new Connector();
        //System.out.println(connector.getPathCount());
        String qString = "гости пришли";
        Indexes.getMorphMap(qString).forEach((l,p)-> System.out.println(l));
        connector.getMorphsFromDB(qString).forEach((l,p) -> System.out.println(l + " - " + p));
        connector.getMorphsFromDB(qString).keySet().forEach(l-> connector.getPathListByLemma(l).forEach(System.out::println));
        System.out.println(qString);
//        connector
//                .getMorphsFromDB(qString)
//                .forEach((l,p) -> {
//                    System.out.println(l);
//                    connector.testM(connector.getPathListByLemma(l));
//                    System.out.println(p);
//                });

        //resultSet.forEach(System.out::println);

    }


}

