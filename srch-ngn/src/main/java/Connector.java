import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Connector {

       private Session session;

       public Session getSession() {
              this.session = null;

              StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                      .configure("hibernate.cfg.xml")
                      .build();
              Metadata metadata = new MetadataSources(registry)
                      .getMetadataBuilder()
                      .build();
              SessionFactory factory = metadata
                      .getSessionFactoryBuilder()
                      .build();

              session = factory.openSession();
              return session;
       }



}
