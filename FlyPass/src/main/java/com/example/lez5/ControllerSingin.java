package com.example.lez5;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ControllerSingin {
    private Stage stage;
    private Scene scene;
    @FXML
    private Label birthdayLabel;

    @FXML
    private DatePicker birthdaySinginDP;

    @FXML
    private Label birthplaceLabel;

    @FXML
    private TextField birthplaceSinginTF;

    @FXML
    private Label codfisLabel;

    @FXML
    private TextField codfisSinginTF;

    @FXML
    private Label nameSinginLabel;

    @FXML
    private TextField nameSinginTF;

    @FXML
    private Button registerButton;

    @FXML
    private Label singinLabel;

    @FXML
    private Label surnameSinginLabel;

    @FXML
    private TextField surnameSinginTF;
    @FXML
    private Hyperlink loginHL;
    @FXML
    private Button undoButton;

    @FXML
    void backToFirstScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FirstScene.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void EnterLoginScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LoginScene.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
