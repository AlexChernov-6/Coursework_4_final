package com.example.feedback_on_school_meals.util;

import com.example.feedback_on_school_meals.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class HibernateSession {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) try (FileInputStream fileInputStream = new FileInputStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(fileInputStream);
            sessionFactory = new Configuration().setProperties(properties)
                    .addAnnotatedClass(DailyMenus.class)
                    .addAnnotatedClass(Dish.class)
                    .addAnnotatedClass(MenuDishes.class)
                    .addAnnotatedClass(Feedback.class)
                    .addAnnotatedClass(Complaint.class)
                    .buildSessionFactory();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return sessionFactory;
    }
}