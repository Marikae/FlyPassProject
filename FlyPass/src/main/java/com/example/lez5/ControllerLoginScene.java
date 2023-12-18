package com.example.lez5;

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Objects;

public class ControllerLoginScene {
    private Stage stage;
    private Scene scene;
    //private Parent root;
    @FXML
    private Label errorLabel;
    @FXML
    private Label loginLabel;
    @FXML
    private Label passwordLoginLabel;
    @FXML
    private PasswordField passwordF;
    @FXML
    private Button undoButton;
    @FXML
    private Label usernameLoginLabel;
    @FXML
    private TextField usernameLoginTF;
    @FXML
    private CheckBox showPass;


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
    void login(ActionEvent event) throws IOException {

        String email = usernameLoginTF.getText();
        String password = passwordF.getText();
        if(checkCredential(email, password) == true){
            //String fromWho = "";
            if (email.endsWith("@questura.it")) { //si logga il personale della questura
                workerLogin(email, password, event);
                //fromWho = "worker";
            }
            else{
                citizenLogin(email, password, event);
                //fromWho = "citizien";
            }
        }

    }

    void enterMainScene(ActionEvent event) throws IOException {
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

    void citizenLogin(String email, String password, ActionEvent event){
        try {
            Connection connection = DatabaseConnection.databaseConnection();
            String query = "SELECT * FROM citizen WHERE email = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Imposta i valori dei parametri per email e password
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Se esiste almeno una riga nel risultato, l'accesso è stato effettuato con successo
                System.out.println("Accesso consentito per l'utente con email: " + resultSet.getString("email"));
                enterMainScene(event);
            } else {
                // Nessun risultato trovato, le credenziali non sono valide
                //System.out.println("Credenziali non valide. Accesso negato.");
                errorLabel.setTextFill(Color.web("#FF0000"));
                errorLabel.setText("Invalid credentials. Access denied.");
            }

            // Chiudi le risorse
            preparedStatement.close();
            connection.close();
        } catch (SQLException | IOException e) {
            System.out.println(e);
        }
    }
    void workerLogin(String email, String password, ActionEvent event){
        try {
            Connection connection = DatabaseConnection.databaseConnection();
            String query = "SELECT * FROM worker WHERE email = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Imposta i valori dei parametri per email e password
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Se esiste almeno una riga nel risultato, l'accesso è stato effettuato con successo
                //ystem.out.println("Accesso consentito per l'utente con email: " + resultSet.getString("email"));
                enterMainScene(event);
            } else {
                // Nessun risultato trovato, le credenziali non sono valide
                errorLabel.setTextFill(Color.web("#FF0000"));
                errorLabel.setText("Invalid credentials. Access denied.");

                //System.out.println("Credenziali non valide. Accesso negato.");
            }

            // Chiudi le risorse
            preparedStatement.close();
            connection.close();
        } catch (SQLException | IOException e) {
            System.out.println(e);
        }
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
        if (isValidEmail(email) == false) {
            errorLabel.setTextFill(Color.web("#FF0000"));
            errorLabel.setText("Email not valid");
            return false;
        }
        return  true;
    }
    public static boolean isValidEmail(String email) {
        // Pattern per verificare il formato dell'email
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
    @FXML
    void showPassword(ActionEvent event) { //TODO
        passwordF.setDisable(false);
        passwordF.setVisible(true);
    }
}