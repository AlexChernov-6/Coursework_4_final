package com.example.feedback_on_school_meals.controller;

import com.example.feedback_on_school_meals.FeedbackOnSchoolMealsApplication;
import com.example.feedback_on_school_meals.model.*;
import com.example.feedback_on_school_meals.service.MenuDishesService;
import javafx.beans.property.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.feedback_on_school_meals.util.HelpFullClass.showModalStage;

public class MenuDishesTableViewController {
    @FXML
    private TableView<MenuDishes> tableViewMenuDishes;
    @FXML
    private Button addBtn;
    @FXML
    private TableColumn<MenuDishes, String> tableColumnDishId;
    @FXML
    private TableColumn<MenuDishes, String> tableColumnMenuId;
    @FXML
    private TableColumn<MenuDishes, String> tableColumnMenuDishId;
    private final MenuDishesService menu_dishesService = new MenuDishesService();
    public Stage stage;

    @FXML
    private void initialize() {
        setCellValueFactories();
        tableViewRefresh();
    }

    private void setCellValueFactories() {
        tableColumnMenuDishId.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%d", cellData.getValue().getMenuDishId())));
        tableColumnMenuId.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%d", cellData.getValue().getMenuId())));
        tableColumnDishId.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s", cellData.getValue().getDish())));
    }

    private void tableViewRefresh() {
        tableViewMenuDishes.getItems().clear();
        tableViewMenuDishes.getItems().addAll(menu_dishesService.getAllRows());
    }

    @FXML
    private void onAddBtn() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FeedbackOnSchoolMealsApplication.class.getResource("menu_dishes-edit-view.fxml"));
        Parent parent = fxmlLoader.load();
        MenuDishesEditViewController controller = fxmlLoader.getController();
        controller.initForm(null, stage);
        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onUpdateBtn() throws IOException {
        MenuDishes menu_dishes = tableViewMenuDishes.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(FeedbackOnSchoolMealsApplication.class.getResource("menu_dishes-edit-view.fxml"));
        Parent parent = fxmlLoader.load();
        MenuDishesEditViewController controller = fxmlLoader.getController();
        controller.initForm(menu_dishes, stage);
        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onDeleteBtn() {
        MenuDishes menu_dishes = tableViewMenuDishes.getSelectionModel().getSelectedItem();
        if (menu_dishes != null) menu_dishesService.delete(menu_dishes);
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