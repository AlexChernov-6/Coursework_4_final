package com.example.feedback_on_school_meals.service;

import com.example.feedback_on_school_meals.model.Dish;

public class DishService extends BaseService<Dish> {
    public DishService() {
        super(Dish.class);
    }

    public Dish getDishByName(String s) {
        for (Dish dish : getAllRows()) {
            if (dish.getDishName().equals(s))
                return dish;
        }
        return null;
    }
}