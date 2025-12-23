package com.example.feedback_on_school_meals.controller;

import com.example.feedback_on_school_meals.model.Dish;
import com.example.feedback_on_school_meals.model.Feedback;
import com.example.feedback_on_school_meals.service.DishService;
import com.example.feedback_on_school_meals.service.FeedbackService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import static com.example.feedback_on_school_meals.util.HelpFullClass.showAlert;


public class FeedbackEditViewController {
    public Feedback feedback;

    public FeedbackService feedbackService = new FeedbackService();

    @FXML
    private ComboBox<Dish> dishesCB;

    @FXML
    private ComboBox<Integer> ratingCB, classCB;

    @FXML
    private TextArea commentTA;

    @FXML
    private Button saveBtn, cancelBtn;

    @FXML
    private CheckBox anonymousCB;

    public void initForm(Feedback feedback, Stage mainStage) {
        this.feedback = feedback;
        saveBtn.setText((feedback != null) ? "Обновить" : "Добавить");
        if (feedback != null)
            initInputControllers();
        initComboBoxes();
    }

    @FXML
    private void onSaveBtn() {
        if (validValues()) {
            if (feedback != null) {
                setStateFeedback();
                feedbackService.update(feedback);
            } else {
                feedback = new Feedback();
                setStateFeedback();
                if (feedbackService.isFirstFeedbackToday(feedback))
                    feedbackService.save(feedback);
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText(null);
                    alert.setContentText("Отзыв от класса " + feedback.getStudentIsClass() +
                            " на блюдо '" + feedback.getDish().getDishName() +
                            "' сегодня уже существует!");
                    alert.showAndWait();
                    return;
                }
            }
            saveBtn.getScene().getWindow().hide();
        }
    }

    @FXML
    private void onCancelBtn() {
        cancelBtn.getScene().getWindow().hide();
    }

    private void setStateFeedback() {
        feedback.setDish(dishesCB.getValue());
        feedback.setFeedbackRating(ratingCB.getValue());
        feedback.setFeedbackComment(commentTA.getText());
        feedback.setStudentIsClass(classCB.getValue());
        feedback.setAnonymous(anonymousCB.isSelected());
    }

    private void initComboBoxes() {
        dishesCB.getItems().addAll(new DishService().getAllRows());
        for(int i = 1; i < 12; i++) {
            if (i < 6)
                ratingCB.getItems().add(i);
            classCB.getItems().add(i);
        }
    }

    private void initInputControllers() {
        if (feedback.getDish() != null) {
            dishesCB.setValue(feedback.getDish());
        }
        if (feedback.getFeedbackRating() != null) {
            ratingCB.setValue(feedback.getFeedbackRating());
        }
        if (feedback.getFeedbackComment() != null) {
            commentTA.setText(feedback.getFeedbackComment());
        }
        if (feedback.getStudentIsClass() != null) {
            classCB.setValue(feedback.getStudentIsClass());
        }
        anonymousCB.setSelected(feedback.isAnonymous());
    }

    private boolean validValues() {
        if (dishesCB.getValue() == null) {
            showAlert("блюдо");
            return false;
        }
        if (ratingCB.getValue() == null) {
            showAlert("оценку", "оценка");
            return false;
        }
        if (classCB.getValue() == null) {
            showAlert("класс ученика");
            return false;
        }
        return true;
    }
}