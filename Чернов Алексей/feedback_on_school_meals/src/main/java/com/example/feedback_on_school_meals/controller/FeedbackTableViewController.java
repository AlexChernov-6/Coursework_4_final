package com.example.feedback_on_school_meals.controller;

import com.example.feedback_on_school_meals.FeedbackOnSchoolMealsApplication;
import com.example.feedback_on_school_meals.model.Feedback;
import com.example.feedback_on_school_meals.service.FeedbackService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.example.feedback_on_school_meals.util.HelpFullClass.showModalStage;


public class FeedbackTableViewController {
    @FXML
    private TableView<Feedback> tableViewFeedback;

    @FXML
    private TableColumn<Feedback, Long> tableColumnID;

    @FXML
    private TableColumn<Feedback, Integer> tableColumnRating, tableColumnClass;

    @FXML
    private TableColumn<Feedback, String> tableColumnDish, tableColumnComment, tableColumnDate;

    @FXML
    private Label countRows;

    private final FeedbackService feedbackService = new FeedbackService();

    public List<Feedback> allFeedback = feedbackService.getAllRows();

    public Stage stage;

    @FXML
    private Button reportsBtn;

    @FXML
    private TextField serchTF;

    @FXML
    private void initialize() {
        setCellValueFactories();
        tableViewRefresh();
    }

    @FXML
    private Button complaintsBtn;

    private void setCellValueFactories() {
        tableColumnID.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getFeedbackID()).asObject());
        tableColumnDish.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDish().getDishName()));
        tableColumnRating.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getFeedbackRating()).asObject());
        tableColumnComment.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFeedbackComment()));
        tableColumnDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFeedbackDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        tableColumnClass.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getStudentIsClass()).asObject());
    }

    private void tableViewRefresh() {
        tableViewFeedback.getItems().clear();
        tableViewFeedback.getItems().addAll(allFeedback);
        countRows.setText(String.format("Показано записей %d/%d", tableViewFeedback.getItems().size(), allFeedback.size()));
    }

    @FXML
    private void onAddBtn() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                FeedbackOnSchoolMealsApplication.class.getResource("feedback-edit-view.fxml"));
        Parent parent = fxmlLoader.load();

        FeedbackEditViewController controller = fxmlLoader.getController();
        controller.initForm(null, stage);

        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onUpdateBtn() throws IOException {
        Feedback feedback = tableViewFeedback.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(
                FeedbackOnSchoolMealsApplication.class.getResource("feedback-edit-view.fxml"));
        Parent parent = fxmlLoader.load();

        FeedbackEditViewController controller = fxmlLoader.getController();
        controller.initForm(feedback, stage);

        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onDeleteBtn() {
        Feedback feedback = tableViewFeedback.getSelectionModel().getSelectedItem();
        if (feedback != null)
            feedbackService.delete(feedback);
        tableViewRefresh();
    }

    @FXML
    private void onReportsBtn() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                FeedbackOnSchoolMealsApplication.class.getResource("dish-rating-table-view.fxml"));
        Parent parent = loader.load();
        reportsBtn.getScene().setRoot(parent);
    }

    @FXML
    public void MenuItemDailyMenusAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(FeedbackOnSchoolMealsApplication.class.getResource("daily_menus-table-view.fxml"));
        Parent parent = loader.load();
        DailyMenusTableViewController controller = loader.getController();
        controller.stage = stage;
        tableViewFeedback.getScene().setRoot(parent);
    }

    @FXML
    public void MenuItemDishesAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(FeedbackOnSchoolMealsApplication.class.getResource("dishes-table-view.fxml"));
        Parent parent = loader.load();
        DishesTableViewController controller = loader.getController();
        controller.stage = stage;
        tableViewFeedback.getScene().setRoot(parent);
    }

    @FXML
    public void MenuItemMenuDishesAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(FeedbackOnSchoolMealsApplication.class.getResource("menu_dishes-table-view.fxml"));
        Parent parent = loader.load();
        MenuDishesTableViewController controller = loader.getController();
        controller.stage = stage;
        tableViewFeedback.getScene().setRoot(parent);
    }

    @FXML
    public void MenuItemFeedbackAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(FeedbackOnSchoolMealsApplication.class.getResource("feedback-table-view.fxml"));
        Parent parent = loader.load();
        FeedbackTableViewController controller = loader.getController();
        controller.stage = stage;
        tableViewFeedback.getScene().setRoot(parent);
    }

    @FXML
    public void MenuItemComplaintsAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(FeedbackOnSchoolMealsApplication.class.getResource("complaints-table-view.fxml"));
        Parent parent = loader.load();
        ComplaintsTableViewController controller = loader.getController();
        controller.stage = stage;
        tableViewFeedback.getScene().setRoot(parent);
    }

    @FXML
    public void filterData() {
        tableViewFeedback.getItems().clear();
        String serchTFContent = serchTF.getText();
        if (serchTFContent.trim().isEmpty()) {
            tableViewRefresh();
        }
        Feedback feedback = null;
        List<Feedback> feedbacksByDishName = new ArrayList<>();
        List<Feedback> feedbacksByRait = new ArrayList<>();
        List<Feedback> feedbacksByDate = new ArrayList<>();
        List<Feedback> feedbacksByClass = new ArrayList<>();
        boolean isRating = serchTFContent.toLowerCase().contains("оценка");
        boolean isClass = serchTFContent.toLowerCase().contains("класс");
        try {
            feedback = feedbackService.getOneRow(Long.parseLong(serchTFContent));
        } catch (NumberFormatException e) {
            feedbacksByDishName.addAll(feedbackService.getFeedbacksByDishName(serchTFContent));
            String[] date = serchTFContent.split("\\.");
            if(date.length > 1)
                feedbacksByDate = feedbackService.getFeedbacksByFeedbackDate(LocalDate.of(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0])));
            if (!serchTFContent.trim().isEmpty() && !serchTFContent.replaceAll("[^0-9]", "").isEmpty()) {
                feedbacksByRait = feedbackService.getFeedbacksByFeedbackRating(Integer.parseInt(serchTFContent.replaceAll("[^0-9]", "")));
                feedbacksByClass = feedbackService.getFeedbacksByStudentIsClass(Integer.parseInt(serchTFContent.replaceAll("[^0-9]", "")));
            }
        }
        if(feedback != null) {
            tableViewFeedback.getItems().add(feedback);
        } else if (!feedbacksByDishName.isEmpty()) {
            tableViewFeedback.getItems().addAll(feedbacksByDishName);
        } else if (!feedbacksByRait.isEmpty() && isRating) {
            tableViewFeedback.getItems().addAll(feedbacksByRait);
        } else if(!feedbacksByDate.isEmpty()) {
            tableViewFeedback.getItems().addAll(feedbacksByDate);
        } else if(!feedbacksByClass.isEmpty() && isClass) {
            tableViewFeedback.getItems().addAll(feedbacksByClass);
        }
        countRows.setText(String.format("Показано записей %d/%d", tableViewFeedback.getItems().size(), allFeedback.size()));
    }
}