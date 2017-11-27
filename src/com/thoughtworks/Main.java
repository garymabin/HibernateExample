package com.thoughtworks;

import com.thoughtworks.entity.Customer;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.Arrays;
import java.util.List;

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

            createSampleData(session);

            Query query = session.createQuery("select customer from Customer as customer");
            List<Customer> customers = query.list();
            for (Customer c : customers) {
                System.out.println(c.getLastName() + " " + c.getFirstName() + " " + c.getGender());
            }

            System.out.println("querying all the managed entities...");
        } finally {
            session.close();
        }
    }

    private static void createSampleData(Session session) {
        session.beginTransaction();
        List<Customer> customers = Arrays.asList(Customer.builder()
            .firstName("Alice")
            .lastName("Jane")
            .gender("Female").build(),
            Customer.builder()
                .firstName("Bob")
                .lastName("Dylan").build());
        customers.forEach(session::save);
        session.getTransaction().commit();
    }
}
