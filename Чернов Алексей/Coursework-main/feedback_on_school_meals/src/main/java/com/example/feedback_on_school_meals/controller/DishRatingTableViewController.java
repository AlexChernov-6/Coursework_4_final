package com.example.feedback_on_school_meals.controller;

import com.example.feedback_on_school_meals.FeedbackOnSchoolMealsApplication;
import com.example.feedback_on_school_meals.model.Dish;
import com.example.feedback_on_school_meals.model.RatingFeedback;
import com.example.feedback_on_school_meals.service.ComplaintService;
import com.example.feedback_on_school_meals.service.DishService;
import com.example.feedback_on_school_meals.service.FeedbackService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.example.feedback_on_school_meals.util.GeneratedXLSX.createXLSX;

public class DishRatingTableViewController {
    @FXML
    private Button backBtn;

    @FXML
    private TableView<Dish> dishRatingTableView, dishHeatTableView;

    @FXML
    private TableColumn<Dish, String> tableColumnDishRating, tableColumnRatingDate, tableColumnDishName, tableColumnDishNameHeat,
            tableColumnMonday, tableColumnTuesday, tableColumnWednesday, tableColumnThursday, tableColumnFriday;

    @FXML
    private TableColumn<Dish, Integer> tableColumnCountComplaint;

    @FXML
    private DatePicker selectedDateDP;

    private FeedbackService feedbackService = new FeedbackService();
    private DishService dishService = new DishService();

    private List<Dish> dishes = dishService.getAllRows();

    @FXML
    private void onBackBtn() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                FeedbackOnSchoolMealsApplication.class.getResource("feedback-table-view.fxml"));
        Parent parent = loader.load();
        backBtn.getScene().setRoot(parent);
    }

    @FXML
    private void initialize() {
        selectedDateDP.setValue(LocalDate.now());
        setCellValueFactories();
        setCellHeatTableValueFactories();
        refreshTableViewGetAll(dishRatingTableView);
        refreshTableViewGetAll(dishHeatTableView);
    }

    private void setCellValueFactories() {
        tableColumnDishName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDishName()));

        tableColumnDishRating.setCellValueFactory(cellData -> {
            String dishName = cellData.getValue().getDishName();
            Double rating = feedbackService.getAverageValue(dishName, selectedDateDP.getValue());
            String ratingText = rating > 0 ? String.format("%.2f", rating).replace(',', '.') : "Нет оценок";
            return new SimpleStringProperty(ratingText);
        });

        tableColumnRatingDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(selectedDateDP.getValue().format(DateTimeFormatter.ofPattern("dd_MM_yyyy"))));
    }

    private void setCellNewValueFactories() {
        tableColumnDishName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDishName()));

        LocalDate MondayDate = selectedDateDP.getValue().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        tableColumnDishRating.setCellValueFactory(cellData -> {
            Long dishID = cellData.getValue().getDishID();
            Double rating = feedbackService
                    .getAverageValue(dishID, MondayDate, MondayDate.plusWeeks(1));
            String ratingText = rating > 0 ? String.format("%.2f", rating).replace(',', '.') : "Нет оценок";
            return new SimpleStringProperty(ratingText);
        });

        tableColumnRatingDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(MondayDate
                        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " - "
                        + MondayDate.plusWeeks(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));

        tableColumnCountComplaint.setCellValueFactory(cellData ->
            new SimpleIntegerProperty(
                    new ComplaintService().getCountComplaintForDish(cellData.getValue().getDishID())).asObject());
    }

    private void setCellHeatTableValueFactories() {

        LocalDate MondayDate = selectedDateDP.getValue().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        tableColumnDishNameHeat.setCellValueFactory(cellDate ->
                new SimpleStringProperty(cellDate.getValue().getDishName()));

        setupHeatMapColumn(tableColumnMonday, MondayDate);

        setupHeatMapColumn(tableColumnTuesday, MondayDate.plusDays(1));

        setupHeatMapColumn(tableColumnWednesday, MondayDate.plusDays(2));

        setupHeatMapColumn(tableColumnThursday, MondayDate.plusDays(3));

        setupHeatMapColumn(tableColumnFriday, MondayDate.plusDays(4));
    }

    @FXML
    private void datePickerAction() {
        setCellNewValueFactories();
        dishRatingTableView.refresh();
        setCellHeatTableValueFactories();
        dishHeatTableView.refresh();
    }

    private void refreshTableViewGetAll(TableView<Dish> tableView) {
        tableView.getItems().clear();
        tableView.getItems().addAll(dishes);
    }

    private void refreshTableViewGetTree() {
        dishRatingTableView.getItems().clear();

        dishRatingTableView.getItems().addAll(dishes.stream().sorted(
                Comparator.comparing(dish -> {
                    double rating = feedbackService.getAverageValue
                            (dish.getDishID(), LocalDate.now().minusWeeks(1), LocalDate.now());
                    return rating > 0 ? rating : 5.0;//Если нету оценок то ставим оценку 5
                }))
                .limit(3)
                .toList());
    }

    @FXML
    private void onProblematicDishBtn() {
        if (tableColumnCountComplaint.isVisible() && dishRatingTableView.isVisible()) {
            tableColumnCountComplaint.setVisible(false);
            setCellValueFactories();
            refreshTableViewGetAll(dishRatingTableView);
        } else {
            tableColumnCountComplaint.setVisible(true);
            setCellNewValueFactories();
            refreshTableViewGetTree();
        }

        if (!dishRatingTableView.isVisible())
            onVisibleHeatTableView();
    }

    @FXML
    private void onVisibleHeatTableView() {
        if (dishHeatTableView.isVisible()) {
            dishHeatTableView.setVisible(false);
            dishRatingTableView.setVisible(true);
        } else {
            dishRatingTableView.setVisible(false);
            dishHeatTableView.setVisible(true);
        }
    }

    @FXML
    private void onGetFeedbackFromXLSX() throws IOException {
        createXLSX(parseToRatingFeedback());
    }

    private void setupHeatMapColumn(TableColumn<Dish, String> column, LocalDate date) {
        column.setCellValueFactory(cellData -> {
            String dishName = cellData.getValue().getDishName();
            Double rating = feedbackService.getAverageValue(dishName, date);
            String ratingText = rating > 0 ? String.format("%.2f", rating).replace(',', '.') : "Нет оценок";
            return new SimpleStringProperty(ratingText);
        });

        column.setCellFactory(tc -> new TableCell<Dish, String>() {
            @Override
            protected void updateItem(String ratingText, boolean empty) {
                super.updateItem(ratingText, empty);

                if (empty || ratingText == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(ratingText);

                    if ("Нет оценок".equals(ratingText)) {
                        setStyle("-fx-background-color: #F5F5F5; -fx-text-fill: #999; -fx-alignment: CENTER;");
                        return;
                    }

                    try {
                        double rating = Double.parseDouble(ratingText);
                        applyRatingColor(this, rating);
                    } catch (NumberFormatException e) {
                        setStyle("");
                    }
                }
            }
        });
    }

    private void applyRatingColor(TableCell<Dish, String> tC, double rating) {
        if (rating >= 4.0) {
            tC.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-alignment: CENTER;"); // Отлично
        } else if (rating >= 3.5) {
            tC.setStyle("-fx-background-color: #8BC34A; -fx-text-fill: white; -fx-alignment: CENTER;");
        } else if (rating >= 3.0) {
            tC.setStyle("-fx-background-color: #CDDC39; -fx-alignment: CENTER;");
        } else if (rating >= 2.5) {
            tC.setStyle("-fx-background-color: #FFEB3B; -fx-alignment: CENTER;");
        } else if (rating >= 2.0) {
            tC.setStyle("-fx-background-color: #FFC107; -fx-alignment: CENTER;");
        } else {
            tC.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-weight: bold; -fx-alignment: CENTER;"); // Очень плохо
        }
    }

    private List<RatingFeedback> parseToRatingFeedback() {
        List<RatingFeedback> list = new ArrayList<>();

        for (int i = 0; i < dishRatingTableView.getItems().size(); i++) {
            Dish dish = dishRatingTableView.getItems().get(i);

            //Получаем данные из всех колонок таблицы
            String dishName = dish.getDishName();

            //Получаем рейтинг из соответствующей колонки
            TableColumn<Dish, String> ratingColumn = tableColumnDishRating;
            String ratingText = ratingColumn.getCellObservableValue(dish).getValue();
            double rating = "Нет оценок".equals(ratingText) ? 0 : Double.parseDouble(ratingText);

            //Получаем дату из колонки даты
            String dateText = tableColumnRatingDate.getCellObservableValue(dish).getValue();

            //Создаем объект RatingFeedback
            RatingFeedback rF = new RatingFeedback(dishName, rating, dateText);
            list.add(rF);
        }
        return list;
    }
}