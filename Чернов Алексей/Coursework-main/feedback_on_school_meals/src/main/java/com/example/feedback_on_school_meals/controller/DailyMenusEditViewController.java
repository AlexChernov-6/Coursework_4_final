package com.example.feedback_on_school_meals.controller;

import com.example.feedback_on_school_meals.model.DailyMenus;
import com.example.feedback_on_school_meals.service.DailyMenusService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;

import static com.example.feedback_on_school_meals.util.HelpFullClass.*;

public class DailyMenusEditViewController {
    public DailyMenus dailyMenus;
    public DailyMenusService modelNameService = new DailyMenusService();
    @FXML
    private DatePicker dailyMenuDateDP;

    @FXML
    private TextField approvedByTF;

    @FXML
    private Button saveBtn, cancelBtn;

    public void initForm(DailyMenus dailyMenus, Stage mainStage) {
        this.dailyMenus = dailyMenus;
        saveBtn.setText((dailyMenus != null) ? "Обновить" : "Добавить");
        if (dailyMenus != null) initInputControllers();
        initComboBoxes();
    }

    @FXML
    private void onSaveBtn() {
        if (validValues()) {
            if (dailyMenus != null) {
                setStateDailyMenus();
                modelNameService.update(dailyMenus);
            } else {
                dailyMenus = new DailyMenus();
                setStateDailyMenus();
                modelNameService.save(dailyMenus);
            }
            saveBtn.getScene().getWindow().hide();
        }
    }

    @FXML
    private void onCancelBtn() {
        cancelBtn.getScene().getWindow().hide();
    }

    private void setStateDailyMenus() {
        dailyMenus.setDailyMenuDate(dailyMenuDateDP.getValue());
        dailyMenus.setApprovedBy(approvedByTF.getText());
    }

    private void initComboBoxes() {
    }

    private void initInputControllers() {
        if (dailyMenus.getDailyMenuDate() != null) {
            dailyMenuDateDP.setValue(dailyMenus.getDailyMenuDate());
        }
        if (dailyMenus.getApprovedBy() != null) {
            approvedByTF.setText(dailyMenus.getApprovedBy());
        }
    }

    private boolean validValues() {
        if (approvedByTF.getText() == null || approvedByTF.getText().trim().isEmpty()) {
            showAlert("Approved by");
            return false;
        }
        return true;
    }
}