package com.example.lez5;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ControllerMainScene extends Controller implements Initializable {
    @FXML
    private Stage stage;
    private Scene scene;
    @FXML
    private GridPane serviceGrid;
    @FXML
    private Button prenotationPickUpButton;
    private List<Service> services;
    @FXML
    private Button prenotationButton;

    @FXML
    private ImageView prenotationImg;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(model.isWorker()){
            //se non è un lavoratore
            prenotationPickUpButton.setText("Inserisci disponibilità ritiro passaporto");
        }else{
            try {
                if(model.notificationSeen() == false)
                    model.activeNotification();
                else{
                    model.disativateNotification();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if(model.notification){
                model.putNotification(prenotationImg);
            }
        }
        services = new ArrayList<>(model.getServices());

        int column = 0;
        int row = 1;
        try {
            for (Service service : services) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("service.fxml"));

                Pane pane = fxmlLoader.load();
                ControllerService controllerService = fxmlLoader.getController();
                controllerService.setData(service);

                if(column == 3){
                    column = 0;
                    ++row;
                }
                serviceGrid.add(pane, column++, row);
                GridPane.setMargin(pane, new Insets(20));
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    public ControllerMainScene(){
        super();
    }
    @FXML
    void goToPrenotationPickUpScene(ActionEvent event) throws SQLException, IOException {
        if(!model.isWorker()) {
            if (!model.hasPrenotation()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Attenzione");
                alert.setHeaderText(null);
                alert.setContentText("Devi prima prenotare il passaporto prima di chiedere l'appuntamento per il ritiro!");
                alert.showAndWait();
            } else {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("PrenotationPickUpScene.fxml")));
                stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        }else{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("PrenotationPickUpScene.fxml")));
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    void goToLogOutScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LogOut.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
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
    void goToPrenotationScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("PrenotationScene.fxml")));
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
}
