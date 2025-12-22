package com.example.feedback_on_school_meals.controller;

import com.example.feedback_on_school_meals.model.*;
import com.example.feedback_on_school_meals.service.DishService;
import com.example.feedback_on_school_meals.service.MenuDishesService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MenuDishesEditViewController {
    public MenuDishes menuDishes;
    public MenuDishesService menuDishesService = new MenuDishesService();
    
    @FXML
    private TextField menuIdTF;
    
    @FXML
    private ComboBox<Dish> dishCB;
    
    @FXML
    private Button saveBtn, cancelBtn;
    
    // Для загрузки данных в ComboBox
    private DishService dishService = new DishService();

    public void initForm(MenuDishes menuDishes, Stage mainStage) {
        this.menuDishes = menuDishes;
        saveBtn.setText((menuDishes != null) ? "Обновить" : "Добавить");
        
        initComboBoxes();
        
        if (menuDishes != null) {
            initInputControllers();
        }
    }

    @FXML
    private void onSaveBtn() {
        if (validValues()) {
            if (menuDishes != null) {
                // Для обновления существующей записи
                setStateMenuDishes();
                menuDishesService.update(menuDishes);
            } else {
                // Для создания новой записи
                menuDishes = new MenuDishes();
                setStateMenuDishes();
                menuDishesService.save(menuDishes);
            }
            saveBtn.getScene().getWindow().hide();
        }
    }

    @FXML
    private void onCancelBtn() {
        cancelBtn.getScene().getWindow().hide();
    }

    private void setStateMenuDishes() {
        if (validValues()) {
            menuDishes.setMenuId(Integer.parseInt(menuIdTF.getText()));
            menuDishes.setDish(dishCB.getValue());
        }
    }

    private void initComboBoxes() {
        dishCB.getItems().clear();
        dishCB.getItems().addAll(dishService.getAllRows());
    }

    private void initInputControllers() {
        if (menuDishes != null) {
            menuIdTF.setText(String.format("%d", menuDishes.getMenuId()));
            
            // Устанавливаем блюдо в ComboBox
            if (menuDishes.getDish() != null) {
                dishCB.setValue(menuDishes.getDish());
            }
        }
    }

    private boolean validValues() {
        // Проверка, что все поля заполнены
        if (menuIdTF.getText() == null || menuIdTF.getText().trim().isEmpty()) {
            showAlert("Укажите меню!");
            return false;
        }
        
        if (dishCB.getValue() == null) {
            showAlert("Выберите блюдо!");
            return false;
        }
        
        return true;
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Внимание");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}