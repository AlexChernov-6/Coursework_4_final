package com.example.feedback_on_school_meals.controller;

import com.example.feedback_on_school_meals.model.Dish;
import com.example.feedback_on_school_meals.service.DishService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import static com.example.feedback_on_school_meals.util.HelpFullClass.*;

public class DishesEditViewController {
    public Dish dishes;
    public DishService modelNameService = new DishService();
    @FXML
    private TextField dishNameTF;

    @FXML
    private TextField standardTempMinTF;

    @FXML
    private TextField standardTempMaxTF;

    @FXML
    private ComboBox<String> dishCategoryCB;

    @FXML
    private Button saveBtn, cancelBtn;

    public void initForm(Dish dishes, Stage mainStage) {
        this.dishes = dishes;
        saveBtn.setText((dishes != null) ? "Обновить" : "Добавить");
        if (dishes != null) initInputControllers();
        initComboBoxes();
    }

    @FXML
    private void onSaveBtn() {
        if (validValues()) {
            if (dishes != null) {
                setStateDishes();
                modelNameService.update(dishes);
            } else {
                dishes = new Dish();
                setStateDishes();
                modelNameService.save(dishes);
            }
            saveBtn.getScene().getWindow().hide();
        }
    }

    @FXML
    private void onCancelBtn() {
        cancelBtn.getScene().getWindow().hide();
    }

    private void setStateDishes() {
        dishes.setDishName(dishNameTF.getText());
        dishes.setDishCategory(dishCategoryCB.getValue());
        dishes.setStandardTempMin(Integer.parseInt(standardTempMinTF.getText()));
        dishes.setStandardTempMax(Integer.parseInt(standardTempMaxTF.getText()));
    }

    private void initComboBoxes() {
        dishCategoryCB.getItems().addAll("первое", "второе", "десерт");
    }

    private void initInputControllers() {
        if (dishes.getDishName() != null) {
            dishNameTF.setText(dishes.getDishName());
        }
        if (dishes.getStandardTempMin() != null) {
            standardTempMinTF.setText(String.format("%d", dishes.getStandardTempMin()));
        }
        if (dishes.getStandardTempMax() != null) {
            standardTempMaxTF.setText(String.format("%d", dishes.getStandardTempMax()));
        }
    }

    private boolean validValues() {
        if (dishNameTF.getText() == null || dishNameTF.getText().trim().isEmpty()) {
            showAlert("Dish name");
            return false;
        }
        if (dishCategoryCB.getValue() == null) {
            showAlert("Dish category");
            return false;
        }
        if (standardTempMinTF.getText() == null || standardTempMinTF.getText().trim().isEmpty()) {
            showAlert("Standard temp min");
            return false;
        }
        if (standardTempMaxTF.getText() == null || standardTempMaxTF.getText().trim().isEmpty()) {
            showAlert("Standard temp max");
            return false;
        }
        return true;
    }
}