package com.example.feedback_on_school_meals.controller;

import com.example.feedback_on_school_meals.FeedbackOnSchoolMealsApplication;
import com.example.feedback_on_school_meals.model.Complaint;
import com.example.feedback_on_school_meals.model.Dish;
import com.example.feedback_on_school_meals.service.ComplaintService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.feedback_on_school_meals.util.HelpFullClass.byteArrayToImageView;
import static com.example.feedback_on_school_meals.util.HelpFullClass.showModalStage;

public class ComplaintsTableViewController {
    @FXML
    private TableView<Complaint> tableViewComplaints;

    @FXML
    private TableColumn<Complaint, Long> tableColumnID;

    @FXML
    private TableColumn<Complaint, Integer> tableColumnClass;

    @FXML
    private TableColumn<Complaint, ImageView> tableColumnImage;

    @FXML
    private TableColumn<Complaint, String> tableColumnText, tableColumnStatus, tableColumnDate, tableColumnResponse, tableColumnResponseAt, tableColumnDish;

    private final ComplaintService complaintService = new ComplaintService();

    public Stage stage;

    @FXML
    private void initialize() {
        setCellValueFactories();
        tableViewRefresh();
    }

    @FXML
    private Button feedbackBtn;

    private void setCellValueFactories() {
        tableColumnID.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getComplaintID()).asObject());
        tableColumnDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getComplaintDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        tableColumnClass.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getStudentIsClass()).asObject());
        tableColumnText.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getComplaintText()));
        tableColumnImage.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(byteArrayToImageView
                        (cellData.getValue().getComplaintPhoto(), 114, 114, true)));
        tableColumnStatus.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getComplaintStatus()));
        tableColumnResponse.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getResponse()));
        tableColumnResponseAt.setCellValueFactory(cellData -> {
            LocalDateTime respondedAt = cellData.getValue().getRespondedAt();
            String formattedDate = (respondedAt != null)
                    ? respondedAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                    : "Не обработано";
            return new SimpleStringProperty(formattedDate);
        });

        tableColumnDish.setCellValueFactory(cellData -> {
            Dish dish = cellData.getValue().getDish();
            return new SimpleStringProperty(dish != null ? dish.toString() : "Блюдо не указано");
        });
    }

    private void tableViewRefresh() {
        tableViewComplaints.getItems().clear();
        tableViewComplaints.getItems().addAll(complaintService.getAllRows());
    }

    @FXML
    private void onAddBtn() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                FeedbackOnSchoolMealsApplication.class.getResource("complaints-edit-view.fxml"));
        Parent parent = fxmlLoader.load();

        ComplaintsEditViewController controller = fxmlLoader.getController();
        controller.initForm(null, stage);

        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onUpdateBtn() throws IOException {
        Complaint complaint = tableViewComplaints.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(
                FeedbackOnSchoolMealsApplication.class.getResource("complaints-edit-view.fxml"));
        Parent parent = fxmlLoader.load();

        ComplaintsEditViewController controller = fxmlLoader.getController();
        controller.initForm(complaint, stage);

        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onDeleteBtn() {
        Complaint complaint = tableViewComplaints.getSelectionModel().getSelectedItem();
        if (complaint != null)
            complaintService.delete(complaint);
        tableViewRefresh();
    }

    @FXML
    public void MenuItemDailyMenusAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(FeedbackOnSchoolMealsApplication.class.getResource("daily_menus-table-view.fxml"));
        Parent parent = loader.load();
        DailyMenusTableViewController controller = loader.getController();
        controller.stage = stage;
        tableViewComplaints.getScene().setRoot(parent);
    }

    @FXML
    public void MenuItemDishesAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(FeedbackOnSchoolMealsApplication.class.getResource("dishes-table-view.fxml"));
        Parent parent = loader.load();
        DishesTableViewController controller = loader.getController();
        controller.stage = stage;
        tableViewComplaints.getScene().setRoot(parent);
    }

    @FXML
    public void MenuItemMenuDishesAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(FeedbackOnSchoolMealsApplication.class.getResource("menu_dishes-table-view.fxml"));
        Parent parent = loader.load();
        MenuDishesTableViewController controller = loader.getController();
        controller.stage = stage;
        tableViewComplaints.getScene().setRoot(parent);
    }

    @FXML
    public void MenuItemFeedbackAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(FeedbackOnSchoolMealsApplication.class.getResource("feedback-table-view.fxml"));
        Parent parent = loader.load();
        FeedbackTableViewController controller = loader.getController();
        controller.stage = stage;
        tableViewComplaints.getScene().setRoot(parent);
    }

    @FXML
    public void MenuItemComplaintsAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(FeedbackOnSchoolMealsApplication.class.getResource("complaints-table-view.fxml"));
        Parent parent = loader.load();
        ComplaintsTableViewController controller = loader.getController();
        controller.stage = stage;
        tableViewComplaints.getScene().setRoot(parent);
    }
}
