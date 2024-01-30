package com.example.lez5;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ControllerLogOutScene extends Controller implements Initializable{
    private Stage stage;
    private Scene scene;
    @FXML
    private ImageView prenotationImg;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(model.notification){
            model.putNotification(prenotationImg);
        }
    }

    public ControllerLogOutScene(){
        super();
    }
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
    @FXML
    void goToProfileScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ProfileScene.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void goToInfoScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("InfoScene.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void goToPrenotationScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("PrenotationScene.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goToLogOutScene(ActionEvent actionEvent) {
    }
}
