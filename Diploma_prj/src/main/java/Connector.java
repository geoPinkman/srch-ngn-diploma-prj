import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Connector {

    private StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
    private Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
    private SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

    private Session getSession() {
        Session session = sessionFactory.openSession();
        return session;
    }
    public void makeTransaction(String path, int code, String content) {
        Session ss = getSession();
        Transaction tx = ss.beginTransaction();
        DaoPage daoPage = new DaoPage();
        daoPage.setPath(path);
        daoPage.setCode(code);
        daoPage.setContent(content);
        try {
            ss.saveOrUpdate(daoPage);
            tx.commit();
        } catch (Exception q) {
            q.printStackTrace();
        }
        ss.close();
    }

}
