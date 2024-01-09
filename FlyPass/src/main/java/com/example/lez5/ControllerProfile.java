package com.example.lez5;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ControllerProfile extends Controller implements Initializable {
    private Stage stage;
    private Scene scene;
    @FXML
    private Button logOutM;

    @FXML
    private Button serviceButton;
    @FXML
    private Label nameLabel;
    @FXML
    private Label surnameLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //model.getUser();
        //nameLabel.setText(model.getNameService());
        //settare tutte le label con gli elementi dell'user
        //TODO
    }
    public ControllerProfile(){
        super();
    }
    @FXML
    void goToMainScene(ActionEvent event) throws IOException {
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

    @FXML
    void goToLogOutScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LogOut.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}