package com.example.feedback_on_school_meals.model;

import jakarta.persistence.*;

@Entity
@Table(name = "dishes", schema = "public")
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dish_id")
    private Long dishID;
    @Column(name = "dish_name")
    private String dishName;
    @Column(name = "dish_category")
    private String dishCategory;
    @Column(name = "standard_temp_min")
    private Integer standardTempMin;
    @Column(name = "standard_temp_max")
    private Integer standardTempMax;


    public Long getDishID() {
        return dishID;
    }

    public void setDishID(Long dishID) {
        this.dishID = dishID;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    @Override
    public String toString() {
        return dishName;
    }

    public String getDishCategory() {
        return dishCategory;
    }

    public void setDishCategory(String dishCategory) {
        this.dishCategory = dishCategory;
    }

    public Integer getStandardTempMin() {
        return standardTempMin;
    }

    public void setStandardTempMin(Integer standardTempMin) {
        this.standardTempMin = standardTempMin;
    }

    public Integer getStandardTempMax() {
        return standardTempMax;
    }

    public void setStandardTempMax(Integer standardTempMax) {
        this.standardTempMax = standardTempMax;
    }
}