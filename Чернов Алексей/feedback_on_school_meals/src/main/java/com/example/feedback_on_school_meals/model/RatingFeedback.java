package com.example.feedback_on_school_meals.model;

public class RatingFeedback {
    private String dishName;
    private Double dishRating;
    private String ratingDate;

    public RatingFeedback(String dishName, Double dishRating, String ratingDate) {
        this.dishName = dishName;
        this.dishRating = dishRating;
        this.ratingDate = ratingDate;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public Double getDishRating() {
        return dishRating;
    }

    public void setDishRating(Double dishRating) {
        this.dishRating = dishRating;
    }

    public String getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(String ratingDate) {
        this.ratingDate = ratingDate;
    }
}