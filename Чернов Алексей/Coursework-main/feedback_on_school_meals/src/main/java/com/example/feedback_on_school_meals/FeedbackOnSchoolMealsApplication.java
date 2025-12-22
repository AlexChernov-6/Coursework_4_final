package com.example.feedback_on_school_meals;

import com.example.feedback_on_school_meals.controller.FeedbackTableViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FeedbackOnSchoolMealsApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FeedbackOnSchoolMealsApplication.class.getResource("feedback-table-view.fxml"));
        Parent parent = fxmlLoader.load();
        FeedbackTableViewController controller = fxmlLoader.getController();
        controller.stage = stage;
        Scene scene = new Scene(parent, 1000, 600);
        stage.setScene(scene);
        stage.setTitle("Обратная связь по школьному питанию");
        stage.show();
    }
}