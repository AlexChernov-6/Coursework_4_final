package com.example.feedback_on_school_meals.model;

import jakarta.persistence.*;

@Entity
@Table(name = "menu_dishes", schema = "public")
public class MenuDishes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_dish_id")
    private int menuDishId;
    @Column(name = "menu_id")
    private int menuId;
    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;

    public int getMenuDishId() {
        return menuDishId;
    }

    public void setMenuDishId(int menuDishId) {
        this.menuDishId = menuDishId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }
}