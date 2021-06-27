/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neu.edu.dao;

import java.io.FileInputStream;
import java.io.InputStream;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.Properties;

/**
 *
 * @author aelinadas
 */
public class ConnectionDAO {
    private Session session = null;
    private static final SessionFactory sf = buildSessionFactory();
    private static SessionFactory buildSessionFactory() {
        try {
            Properties properties = new Properties();
            InputStream inputStream;
            inputStream = new FileInputStream("/home/ubuntu/applicationConfig.properties");
            properties.load(inputStream);
            inputStream.close();
            final String hostName = properties.getProperty("hostName").trim();
            final String dbName = properties.getProperty("dbName").trim();
            final String userName = properties.getProperty("userName").trim();
            final String password = properties.getProperty("password").trim();
            
            SessionFactory sessionFactory = new Configuration()
                    .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect")
                    .setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
                    .setProperty("hibernate.connection.url", "jdbc:mysql://" + hostName + "/" + dbName + "?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC")
                    .setProperty("hibernate.connection.username", userName)
                    .setProperty("hibernate.connection.password", password)
                    .setProperty("hibernate.hbm2ddl.auto", "update")
                    .setProperty("hibernate.enable_lazy_load_no_trans", "true")
                    .setProperty("show_sql", "true")
                    .setProperty("hibernate.connection.requireSSL", "true")
                    .setProperty("hibernate.connection.useSSL", "true")
                    .setProperty("hibernate.c3p0.min_size", "500")
                    .setProperty("hibernate.c3p0.max_size", "1000")
                    .setProperty("hibernate.c3p0.timeout", "120")
                    .setProperty("hibernate.c3p0.idle_test_period", "5")
                    .addResource("book.hbm.xml")
                    .addResource("buyer.hbm.xml")
                    .addResource("cart.hbm.xml")
                    .addResource("seller.hbm.xml").buildSessionFactory();
            return sessionFactory;

        } catch (Throwable e) {
            System.err.println("Initial SessionFactory creation failed." + e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sf;
    }

    public Session getSession() {
        if (session == null || !session.isOpen()) {
            session = getSessionFactory().openSession();
        }
        return session;
    }

    public void beginTransaction() {
        getSession().beginTransaction();
    }

    public void commit() {
        getSession().getTransaction().commit();;
    }

    public void close() {
        getSession().close();
    }

    public void rollbackTransaction() {
        getSession().getTransaction().rollback();
    }
}
