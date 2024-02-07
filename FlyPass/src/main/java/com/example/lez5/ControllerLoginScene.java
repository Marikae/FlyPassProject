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
import org.w3c.dom.events.MouseEvent;

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
    @FXML
    private CheckBox showPass;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showPass.setOnAction(event -> {
            if (showPass.isSelected()) {
                passwordF.setPromptText(passwordF.getText());
                passwordF.setText("");
                passwordF.setDisable(true);
            } else {
                passwordF.setText(passwordF.getPromptText());
                passwordF.setPromptText("");
                passwordF.setDisable(false);
            }
        });
        errorLabel.setVisible(false);
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
        if(showPass.isSelected()) {
            passwordF.setText(passwordF.getPromptText());
            passwordF.setPromptText("");
            passwordF.setDisable(false);
        }
        String email = usernameLoginTF.getText();
        String password = passwordF.getText();


        if(email.equals("admin")){
            model.workerLogin(email, password);
            enterMainScene(event);
        }else {
            if (checkCredential(email, password)) {

                if (email.endsWith("@questura.it")) { //si logga il personale della questura
                    if (model.workerLogin(email, password))
                        enterMainScene(event);
                    else {
                        // Nessun risultato trovato, le credenziali non sono valide
                        //System.out.println("Credenziali non valide. Accesso negato.");
                        errorLabel.setVisible(true);
                        errorLabel.setTextFill(Color.web("#FF0000"));
                        errorLabel.setText("Credenziali non valide. Accesso negato\n.");
                        //fromWho = "worker";
                    }
                } else {
                    if (model.citizenLogin(email, password)) {
                        enterMainScene(event);
                    } else {
                        // Nessun risultato trovato, le credenziali non sono valide
                        errorLabel.setVisible(true);
                        errorLabel.setTextFill(Color.web("#FF0000"));
                        errorLabel.setText("Credenziali non valide. Accesso negato.\n");
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
            errorLabel.setVisible(true);
            errorLabel.setTextFill(Color.web("#FF0000"));
            errorLabel.setText("Inserisci le credenziali per accedere.");
            return false;
        }
        if(email.isEmpty()){
            errorLabel.setVisible(true);
            errorLabel.setTextFill(Color.web("#FF0000"));
            errorLabel.setText("Email mancante.");
            return false;
        }
        if(password.isEmpty()){
            errorLabel.setVisible(true);
            errorLabel.setTextFill(Color.web("#FF0000"));
            errorLabel.setText("Password mancante.");
            return false;
        }
        if (!model.checkEmail(email)) {
            errorLabel.setVisible(true);
            errorLabel.setTextFill(Color.web("#FF0000"));
            errorLabel.setText("Email non valida.");
            return false;
        }
        return  true;
    }

    @FXML
    void showPassword(ActionEvent event) { //TODO
        //errorLabel.setVisible(true);
        passwordF.setDisable(false);
        passwordF.setVisible(true);
    }
}