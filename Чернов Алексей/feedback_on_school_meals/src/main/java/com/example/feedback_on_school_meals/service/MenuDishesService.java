package com.example.feedback_on_school_meals.service;

import com.example.feedback_on_school_meals.model.MenuDishes;
import com.example.feedback_on_school_meals.util.HibernateSession;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class MenuDishesService extends BaseService<MenuDishes> {
    public MenuDishesService() {
        super(MenuDishes.class);
    }

    @Override
    public void save(MenuDishes newRow) {
        Session session = HibernateSession.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.merge(newRow);
        transaction.commit();
    }
}