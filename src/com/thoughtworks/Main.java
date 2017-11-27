package com.thoughtworks;

import com.thoughtworks.entity.Customer;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {
    private static final SessionFactory ourSessionFactory;

    static {
        try {
            ourSessionFactory = new Configuration().
                configure("hibernate.cfg.xml").
                buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public static void main(final String[] args) throws Exception {
        final Session session = getSession();
        try {
            session.beginTransaction();
            Customer customer = new Customer();
            customer.setFirstName("Gary");
            customer.setLastName("Ma");
            session.save(customer);
            session.getTransaction().commit();

            session.beginTransaction();
            Customer customer1 = session.get(Customer.class, 1L);
            System.out.println(customer1.getFirstName());
            System.out.println(customer1.getLastName());
            customer1.setGender("Male");
            session.getTransaction().commit();
            System.out.println("querying all the managed entities...");
        } finally {
            session.close();
        }
    }
}
