package com.example.feedback_on_school_meals.controller;

import com.example.feedback_on_school_meals.FeedbackOnSchoolMealsApplication;
import com.example.feedback_on_school_meals.model.*;
import com.example.feedback_on_school_meals.service.DishService;
import javafx.beans.property.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.feedback_on_school_meals.util.HelpFullClass.showModalStage;

public class DishesTableViewController {
    @FXML
    private TableView<Dish> tableViewDishes;
    @FXML
    private Button addBtn;
    @FXML
    private TableColumn<Dish, String> tableColumnDishId;
    @FXML
    private TableColumn<Dish, String> tableColumnDishName, tableColumnDishCategory, tableColumnStandardTempMin
            , tableColumnStandardTempMax;
    private final DishService dishesService = new DishService();
    public Stage stage;

    @FXML
    private void initialize() {
        setCellValueFactories();
        tableViewRefresh();
    }

    private void setCellValueFactories() {
        tableColumnDishId.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%d", cellData.getValue().getDishID())));
        tableColumnDishName.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s", cellData.getValue().getDishName())));
        tableColumnDishCategory.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s", cellData.getValue().getDishCategory())));
        tableColumnStandardTempMin.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%d", cellData.getValue().getStandardTempMin())));
        tableColumnStandardTempMax.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%d", cellData.getValue().getStandardTempMax())));
    }

    private void tableViewRefresh() {
        tableViewDishes.getItems().clear();
        tableViewDishes.getItems().addAll(dishesService.getAllRows());
    }

    @FXML
    private void onAddBtn() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FeedbackOnSchoolMealsApplication.class.getResource("dishes-edit-view.fxml"));
        Parent parent = fxmlLoader.load();
        DishesEditViewController controller = fxmlLoader.getController();
        controller.initForm(null, stage);
        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onUpdateBtn() throws IOException {
        Dish dishes = tableViewDishes.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(FeedbackOnSchoolMealsApplication.class.getResource("dishes-edit-view.fxml"));
        Parent parent = fxmlLoader.load();
        DishesEditViewController controller = fxmlLoader.getController();
        controller.initForm(dishes, stage);
        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onDeleteBtn() {
        Dish dishes = tableViewDishes.getSelectionModel().getSelectedItem();
        if (dishes != null) dishesService.delete(dishes);
        tableViewRefresh();
    }

    @FXML
    public void MenuItemDailyMenusAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(FeedbackOnSchoolMealsApplication.class.getResource("daily_menus-table-view.fxml"));
        Parent parent = loader.load();
        DailyMenusTableViewController controller = loader.getController();
        controller.stage = stage;
        addBtn.getScene().setRoot(parent);
    }

    @FXML
    public void MenuItemDishesAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(FeedbackOnSchoolMealsApplication.class.getResource("dishes-table-view.fxml"));
        Parent parent = loader.load();
        DishesTableViewController controller = loader.getController();
        controller.stage = stage;
        addBtn.getScene().setRoot(parent);
    }

    @FXML
    public void MenuItemMenuDishesAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(FeedbackOnSchoolMealsApplication.class.getResource("menu_dishes-table-view.fxml"));
        Parent parent = loader.load();
        MenuDishesTableViewController controller = loader.getController();
        controller.stage = stage;
        addBtn.getScene().setRoot(parent);
    }

    @FXML
    public void MenuItemFeedbackAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(FeedbackOnSchoolMealsApplication.class.getResource("feedback-table-view.fxml"));
        Parent parent = loader.load();
        FeedbackTableViewController controller = loader.getController();
        controller.stage = stage;
        addBtn.getScene().setRoot(parent);
    }

    @FXML
    public void MenuItemComplaintsAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(FeedbackOnSchoolMealsApplication.class.getResource("complaints-table-view.fxml"));
        Parent parent = loader.load();
        ComplaintsTableViewController controller = loader.getController();
        controller.stage = stage;
        addBtn.getScene().setRoot(parent);
    }
}