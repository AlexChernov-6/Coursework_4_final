package com.example.feedback_on_school_meals.controller;

import com.example.feedback_on_school_meals.FeedbackOnSchoolMealsApplication;
import com.example.feedback_on_school_meals.model.*;
import com.example.feedback_on_school_meals.service.DailyMenusService;
import javafx.beans.property.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.feedback_on_school_meals.util.HelpFullClass.showModalStage;

public class DailyMenusTableViewController {
    @FXML
    private TableView<DailyMenus> tableViewDailyMenus;
    @FXML
    private Button addBtn;
    @FXML
    private TableColumn<DailyMenus, String> tableColumnDailyMenuDate;
    @FXML
    private TableColumn<DailyMenus, String> tableColumnDailyMenuId;
    @FXML
    private TableColumn<DailyMenus, String> tableColumnApprovedBy;
    private final DailyMenusService daily_menusService = new DailyMenusService();
    public Stage stage;

    @FXML
    private void initialize() {
        setCellValueFactories();
        tableViewRefresh();
    }

    private void setCellValueFactories() {
        tableColumnDailyMenuDate.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s", cellData.getValue().getDailyMenuDate())));
        tableColumnDailyMenuId.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%d", cellData.getValue().getDailyMenuId())));
        tableColumnApprovedBy.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s", cellData.getValue().getApprovedBy())));
    }

    private void tableViewRefresh() {
        tableViewDailyMenus.getItems().clear();
        tableViewDailyMenus.getItems().addAll(daily_menusService.getAllRows());
    }

    @FXML
    private void onAddBtn() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FeedbackOnSchoolMealsApplication.class.getResource("daily_menus-edit-view.fxml"));
        Parent parent = fxmlLoader.load();
        DailyMenusEditViewController controller = fxmlLoader.getController();
        controller.initForm(null, stage);
        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onUpdateBtn() throws IOException {
        DailyMenus daily_menus = tableViewDailyMenus.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(FeedbackOnSchoolMealsApplication.class.getResource("daily_menus-edit-view.fxml"));
        Parent parent = fxmlLoader.load();
        DailyMenusEditViewController controller = fxmlLoader.getController();
        controller.initForm(daily_menus, stage);
        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onDeleteBtn() {
        DailyMenus daily_menus = tableViewDailyMenus.getSelectionModel().getSelectedItem();
        if (daily_menus != null) daily_menusService.delete(daily_menus);
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