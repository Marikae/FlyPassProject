package com.example.lez5;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;

public class ControllerRegisterScene extends Controller{


    private Stage stage;
    private Scene scene;
    @FXML
    private TextField place_of_birth;

    @FXML
    private TextField cat;

    @FXML
    private TextField num_health_card;

    @FXML
    private DatePicker date_of_birth;

    @FXML
    private TextField tax_code;

    @FXML
    private TextField confirmPassword;

    @FXML
    private TextField email;

    @FXML
    private Label error;

    @FXML
    private TextField name;

    @FXML
    private TextField password;

    @FXML
    private TextField surname;


    //-------------------------------------------------------------------

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

    @FXML
    void registration(ActionEvent event) throws IOException, SQLException {

        String aus1 = name.getText();
        String aus2 = password.getText();
        String aus3 = email.getText();
        String aus4 = num_health_card.getText();
        LocalDate aus5 = date_of_birth.getValue();
        String aus6 = cat.getText();
        String aus7 = tax_code.getText();
        String aus8 = place_of_birth.getText();
        String aus9 = surname.getText();


        boolean check = false;

        if(date_of_birth.getValue() != null){
            User newUser = new User(name.getText(), num_health_card.getText(), cat.getText(), surname.getText(), date_of_birth.getValue().toString(), place_of_birth.getText(), tax_code.getText(), email.getText(), password.getText());

            if(!model.checkEmail(email.getText())){
                error.setText("Invalid mail format");
                return;
            }

            if (!model.isCodiceFiscaleValid(aus7, aus1, aus9)){
                error.setText("Invalid tax code format");
                return;
            }

            if(!model.HealthCardNumberCheck(aus4)){
                error.setText("Invalid health card number format");
                return;
            }
            if(checkEmptyFields(newUser) && checkPasswordConfirm())
                check = true;

        }else
            error.setText("Field empty");


        if(check == true){
            model.databaseInsertion(aus5,aus1,aus2,aus3,aus4,aus6,aus7,aus8,aus9);
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScene.fxml")));
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            // stage.initStyle(StageStyle.TRANSPARENT); // Rimuovi i bordi della finestra
            double screenWidth = Screen.getPrimary().getBounds().getWidth();
            double screenHeight = Screen.getPrimary().getBounds().getHeight();
            double windowWidth = screenWidth + 30; // Larghezza della finestra
            double windowHeight = screenHeight + 1; // Altezza della finestra
            stage.setX((screenWidth - windowWidth) / 2);
            stage.setY((screenHeight - windowHeight) / 2);
            stage.setScene(scene);
            stage.show();
        }
    }

    public boolean checkEmptyFields(User user){
        if(user.getName().isEmpty() || user.getSurname().isEmpty() || user.getBirthday().isEmpty() || user.getBirthPlace().isEmpty() || user.getCodiceFiscale().isEmpty() || user.getEmail().isEmpty() || confirmPassword.getText().isEmpty()){
            error.setText("there is a empty fields");
            return false;
        }
        return true;
    }
    public boolean checkPasswordConfirm(){
        if(!password.getText().equals(confirmPassword.getText())){
            error.setText("Password not equal");
            return false;
        }
        return true;
    }

}