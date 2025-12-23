package com.example.feedback_on_school_meals.service;

import com.example.feedback_on_school_meals.model.Complaint;
import com.example.feedback_on_school_meals.model.Dish;

import java.time.LocalDate;
import java.util.Objects;

public class ComplaintService extends BaseService<Complaint> {
    //Можно добавить отдельные уникальные методы для этой таблице
    public ComplaintService() {
        super(Complaint.class);
    }

    public int getCountComplaintForDish(Long dishId) {
        int result = 0;
        LocalDate endDate = LocalDate.now().plusDays(1);
        LocalDate startDate = LocalDate.now().minusWeeks(1).minusDays(1);
        for(Complaint complaint : getAllRows()) {
            if(complaint.getDish() != null
                    && Objects.equals(complaint.getDish().getDishID(), dishId)
                    && complaint.getComplaintDate().isAfter(startDate)
                    && complaint.getComplaintDate().isBefore(endDate)) {
                result += 1;
            }
        }
        return result;
    }
}