package com.example.feedback_on_school_meals.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MenuDishesId implements Serializable {

    @Column(name = "menu_id")
    private Integer menuId;

    @Column(name = "dish_id")
    private Long dishId; // Измените Integer на Long

    public MenuDishesId() {}

    public MenuDishesId(Integer menuId, Long dishId) { // Long вместо Integer
        this.menuId = menuId;
        this.dishId = dishId;
    }

    public Integer getMenuId() { return menuId; }
    public void setMenuId(Integer menuId) { this.menuId = menuId; }

    public Long getDishId() { return dishId; }
    public void setDishId(Long dishId) { this.dishId = dishId; } // Long вместо Integer

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuDishesId that = (MenuDishesId) o;
        return Objects.equals(menuId, that.menuId) &&
                Objects.equals(dishId, that.dishId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, dishId);
    }

    @Override
    public String toString() {
        return String.format("%d", menuId);
    }
}