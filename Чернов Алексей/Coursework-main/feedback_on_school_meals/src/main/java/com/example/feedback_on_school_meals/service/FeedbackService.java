package com.example.feedback_on_school_meals.service;

import com.example.feedback_on_school_meals.model.Feedback;
import com.example.feedback_on_school_meals.util.HibernateSession;
import org.hibernate.Session;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class FeedbackService extends BaseService<Feedback> {
    public FeedbackService() {
        super(Feedback.class);
    }

    public Double getAverageValue(String dishName, LocalDate date) {
        try (Session session = HibernateSession.getSessionFactory().openSession()) {
            String hql = "select avg(feedbackRating) from Feedback where feedbackDate = :date and dish = :dishValue";

            Double result = session.createQuery(hql, Double.class)
                    .setParameter("date", date)
                    .setParameter("dishValue", new DishService().getDishByName(dishName))
                    .uniqueResult();

            return result != null ? result : 0.0;
        }
    }

    //Каждая жалоба автоматическая оценка 1
    public Double getAverageValue(Long dishID, LocalDate dateStart, LocalDate dateEnd) {
        try (Session session = HibernateSession.getSessionFactory().openSession()) {
            String hql = "select feedbackRating from Feedback where feedbackDate >= :dateStart and " +
                    "feedbackDate <= :dateEnd and dish = :dishValue";
            List<Integer> resulList = session.createQuery(hql, Integer.class)
                    .setParameter("dateStart", dateStart)
                    .setParameter("dateEnd", dateEnd)
                    .setParameter("dishValue", new DishService().getOneRow(dishID))
                    .list();

            Double result = 0.0;
            for (Integer el : resulList) {
                result += el;
            }
            int countComplaint = new ComplaintService().getCountComplaintForDish(dishID);
            result += countComplaint;

            return result / (resulList.size() + countComplaint);
        }
    }

    public boolean isFirstFeedbackToday(Feedback feedback) {
        try (Session session = HibernateSession.getSessionFactory().openSession()) {
            if (feedback.getFeedbackDate() == null) {
                feedback.setFeedbackDate(LocalDate.now());
            }
            String hql = "from Feedback where feedbackDate = :date and studentIsClass = :class and dish.dishID = :dishId";
            List<Feedback> resultList = session.createQuery(hql, Feedback.class)
                    .setParameter("date", feedback.getFeedbackDate())
                    .setParameter("class", feedback.getStudentIsClass())
                    .setParameter("dishId", feedback.getDish().getDishID())
                    .list();

            return resultList.isEmpty();
        }
    }

    public List<Feedback> getFeedbacksByDishName(String dishName) {
        String sqlQuery = "from Feedback where dish.dishName = :nameDish";
        try(Session session = HibernateSession.getSessionFactory().openSession()) {
            return session.createQuery(sqlQuery).setParameter("nameDish", dishName).list();
        }
    }

    public List<Feedback> getFeedbacksByFeedbackRating(int rait) {
        String sqlQuery = "from Feedback where feedbackRating = :raitV";
        try(Session session = HibernateSession.getSessionFactory().openSession()) {
            return session.createQuery(sqlQuery).setParameter("raitV", rait).list();
        }
    }

    public List<Feedback> getFeedbacksByFeedbackDate(LocalDate date) {
        String sqlQuery = "from Feedback where feedbackDate = :dateV";
        try(Session session = HibernateSession.getSessionFactory().openSession()) {
            return session.createQuery(sqlQuery).setParameter("dateV", date).list();
        }
    }

    public List<Feedback> getFeedbacksByStudentIsClass(int clas) {
        String sqlQuery = "from Feedback where studentIsClass = :class";
        try(Session session = HibernateSession.getSessionFactory().openSession()) {
            return session.createQuery(sqlQuery).setParameter("class", clas).list();
        }
    }
}