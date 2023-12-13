package com.example.lez5;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ControllerLogOutScene {
    private Stage stage;
    private Scene scene;
    @FXML
    private Button undoLogOut;
    @FXML
    private Button logOut;
    @FXML
    private Button yesLogOut;
    @FXML
    private Button servicesButton;

    @FXML
    void returnFirstScene(ActionEvent event) throws IOException {
        //ritorna alla prima schermata inziale di login e singin
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FirstScene.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void returnMainScene(ActionEvent event) throws IOException {
        //ritorna nella main scene
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

    public void goToLogOutScene(ActionEvent actionEvent) {

    }


}
