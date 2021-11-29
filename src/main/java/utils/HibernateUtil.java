package utils;

import entity.Coordinates;
import entity.Location;
import entity.Ticket;
import entity.Person;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "org.postgresql.Driver");
                //settings.put(Environment.URL, "jdbc:postgresql://pg/studs");
                settings.put(Environment.URL, "jdbc:postgresql://localhost:9999/studs");
                settings.put(Environment.USER, "s264447");
                settings.put(Environment.PASS, "mdi832");
                settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");

                settings.put(Environment.SHOW_SQL, "true");

                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

                settings.put(Environment.HBM2DDL_AUTO, "update");
//                settings.put(Environment.VALID)
//                spring.jpa.properties.hibernate.validator.apply_to_ddl=false

                configuration.setProperties(settings);
                configuration.addAnnotatedClass(Ticket.class);
                configuration.addAnnotatedClass(Coordinates.class);
                configuration.addAnnotatedClass(Person.class);
                configuration.addAnnotatedClass(Location.class);


                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                return sessionFactory;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}