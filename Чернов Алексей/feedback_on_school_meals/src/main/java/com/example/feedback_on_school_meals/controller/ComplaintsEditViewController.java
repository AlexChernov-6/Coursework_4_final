package com.example.feedback_on_school_meals.controller;

import com.example.feedback_on_school_meals.model.Complaint;
import com.example.feedback_on_school_meals.model.Dish;
import com.example.feedback_on_school_meals.service.ComplaintService;
import com.example.feedback_on_school_meals.service.DishService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;

import static com.example.feedback_on_school_meals.util.HelpFullClass.*;

public class ComplaintsEditViewController {
    public Complaint complaint;

    public ComplaintService complaintService = new ComplaintService();

    @FXML
    private ComboBox<Integer> classCB;

    @FXML
    private TextArea complaintTextTA;

    @FXML
    private ImageView imageView;

    @FXML
    private Button saveBtn, cancelBtn, loadImageBtn;

    @FXML
    private ComboBox<Dish> dishCB;

    public void initForm(Complaint complaint, Stage mainStage) {
        this.complaint = complaint;
        saveBtn.setText((complaint != null) ? "Обновить" : "Добавить");
        if (complaint != null)
            initInputControllers();
        initComboBoxes();
    }

    @FXML
    private void onSaveBtn() {
        if (validValues()) {
            if (complaint != null) {
                setStateComplaint();
                complaintService.update(complaint);
            } else {
                complaint = new Complaint();
                setStateComplaint();
                complaintService.save(complaint);
            }
            saveBtn.getScene().getWindow().hide();
        }
    }

    @FXML
    private void onCancelBtn() {
        cancelBtn.getScene().getWindow().hide();
    }

    private void setStateComplaint() {
        complaint.setStudentIsClass(classCB.getValue());
        complaint.setComplaintText(complaintTextTA.getText());
        complaint.setComplaintPhoto(imageViewToByteArray(imageView));
        complaint.setComplaintStatus("новый");
    }

    private void initComboBoxes() {
        for (int i = 1; i < 12; i++) {
            classCB.getItems().add(i);
        }
        dishCB.getItems().addAll(new DishService().getAllRows());
    }

    private void initInputControllers() {
        if (complaint.getStudentIsClass() != null) {
            classCB.setValue(complaint.getStudentIsClass());
        }
        if (complaint.getComplaintText() != null) {
            complaintTextTA.setText(complaint.getComplaintText());
        }
        if (complaint.getComplaintPhoto() != null && complaint.getComplaintPhoto().length != 0) {
            imageView.setImage(byteArrayToImageView(complaint.getComplaintPhoto(), 180, 380, true).getImage());
        }
        if (complaint.getDish() != null) {
            dishCB.setValue(complaint.getDish());
        }
    }

    private boolean validValues() {
        if (classCB.getValue() == null) {
            showAlert("класс ученика");
            return false;
        }
        return true;
    }

    @FXML
    private void onLoadImageBtn() throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg")
        );
        Stage stage = (Stage) loadImageBtn.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            String imageUrl = file.toURI().toURL().toExternalForm();
            imageView.setImage(new Image(imageUrl));
        }
    }
}
