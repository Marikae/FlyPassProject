package com.example.lez5;

import java.io.IOException;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ControllerAddRequestQuesturaScene {

    private Stage stage;
    private Scene scene;

    @FXML
    private Label dateLabel;

    @FXML
    private ComboBox<String> time;

    @FXML
    private Label orarioLabel;
    
    @FXML
    private DatePicker dateOfSlots;

    @FXML
    private ComboBox<Integer> numberOfSlots;

    @FXML
    private void initialize() {
        // Inizializza la ComboBox con valori
        numberOfSlots.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        ObservableList<String> items = FXCollections.observableArrayList(
            "8:00 - 9:00",
            "9:00 - 10:00",
            "10:00 - 11:00",
            "11:00 - 12:00",
            "14:00 - 15:00",
            "15:00 - 16:00",
            "16:00 - 17:00",
            "17:00 - 18:00"
        );
        time.setItems(items);
    }

    @FXML
    private Button insertNewSlot;

    @FXML
    private Label labelSlots;

    @FXML
    private Label addNewSlotLabel;

    
    @FXML
    private Button undoButton;

    @FXML
    void backToFirstScene(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FirstScene.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void insertNewSlot1(ActionEvent event) {
        //
    }

}
