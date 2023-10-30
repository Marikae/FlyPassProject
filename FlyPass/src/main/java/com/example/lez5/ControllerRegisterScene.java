package com.example.lez5;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class ControllerRegisterScene {
    private Stage stage;
    private Scene scene;
    @FXML
    private TextField birthPlace;

    @FXML
    private DatePicker birthday;

    @FXML
    private Label birthdayLabel;

    @FXML
    private Label birthplaceLabel;

    @FXML
    private Label codfisLabel;

    @FXML
    private TextField codiceFiscale;

    @FXML
    private TextField confirmPassword;

    @FXML
    private TextField email;

    @FXML
    private Label error;

    @FXML
    private Hyperlink loginHL;

    @FXML
    private TextField name;

    @FXML
    private Label nameSinginLabel;

    @FXML
    private TextField password;

    @FXML
    private Button registerButton;

    @FXML
    private Label singinLabel;

    @FXML
    private TextField surname;

    @FXML
    private Label surnameSinginLabel;

    @FXML
    private Button undoButton;

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
    void registration(ActionEvent event) throws IOException {
        //TODO controllare consistenza dei dati
        //TODO creare metodo di scrittura utenti su db in model
        boolean check = false;

        if(birthday.getValue() != null){
            User newUser = new User(name.getText(), surname.getText(), birthday.getValue().toString(), birthPlace.getText(), codiceFiscale.getText(), email.getText(), password.getText());
            if(checkEmptyFields(newUser) == true && checkPasswordConfirm() == true)
                check = true;
            // checkDataConsistence(newUser); //TODO
            }else
               error.setText("Field empty");



        if(check == true){
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScene.fxml")));
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }



    }

    public boolean checkEmptyFields(User user){
        String name = user.getName();
        String surname = user.getSurname();
        String birthday = user.getBirthday();
        String birthPlace = user.getBirthPlace();
        String codiceFiscale = user.getCodiceFiscale();
        String email = user.getEmail();
        String password = user.getPassword();

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

    public boolean checkDataConsistence(User user){
        //TODO
        return true;
    }
}

