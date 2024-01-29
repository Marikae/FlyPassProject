package com.example.lez5;

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Objects;

public class ControllerLoginScene extends Controller implements Initializable {
    private Stage stage;
    private Scene scene;

    @FXML
    private Label errorLabel;
    @FXML
    private PasswordField passwordF;
    @FXML
    private TextField usernameLoginTF;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public ControllerLoginScene() {
        super();
    }

    @FXML
    void backToFirstScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FirstScene.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void enterRegisterScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("RegisterScene.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
     private void login(ActionEvent event) throws IOException {

        String email = usernameLoginTF.getText();
        String password = passwordF.getText();
        if(email.equals("admin")){
            model.workerLogin(email, password);
            enterMainScene(event);
        }else {
            if (checkCredential(email, password)) {
                //String fromWho = "";
                //model.login(email, password);
                if (email.endsWith("@questura.it")) { //si logga il personale della questura
                    if (model.workerLogin(email, password))
                        enterMainScene(event);
                    else {
                        // Nessun risultato trovato, le credenziali non sono valide
                        //System.out.println("Credenziali non valide. Accesso negato.");
                        errorLabel.setTextFill(Color.web("#FF0000"));
                        errorLabel.setText("Invalid credentials. Access denied.");
                        //fromWho = "worker";
                    }
                } else {
                    if (model.citizenLogin(email, password)) {
                        enterMainScene(event);
                    } else {
                        // Nessun risultato trovato, le credenziali non sono valide
                        errorLabel.setTextFill(Color.web("#FF0000"));
                        errorLabel.setText("Invalid credentials. Access denied.");
                    }
                }
            }
        }
    }


    public void enterMainScene(ActionEvent event) throws IOException {
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
        //stage.setFullScreen(true);
        stage.show();
    }

    boolean checkCredential(String email, String password){
        if(email.isEmpty() && password.isEmpty()){
            errorLabel.setTextFill(Color.web("#FF0000"));
            errorLabel.setText("Empty fields.");
            return false;
        }
        if(email.isEmpty()){
            errorLabel.setTextFill(Color.web("#FF0000"));
            errorLabel.setText("Email is missing.");
            return false;
        }
        if(password.isEmpty()){
            errorLabel.setTextFill(Color.web("#FF0000"));
            errorLabel.setText("Password is missing.");
            return false;
        }
        if (!model.checkEmail(email)) {
            errorLabel.setTextFill(Color.web("#FF0000"));
            errorLabel.setText("Email not valid");
            return false;
        }
        return  true;
    }

    @FXML
    void showPassword(ActionEvent event) { //TODO
        passwordF.setDisable(false);
        passwordF.setVisible(true);
    }
}