package com.example.lez5;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
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
    @FXML
    private Label ricordaRitiroLabel;
    @FXML
    private Label statoPrenotazioneERitiro;
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
    @FXML
    private ScrollPane scroll;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Ottieni le dimensioni dello schermo
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        // Crea un oggetto DoubleProperty per la larghezza dello schermo
        DoubleProperty screenWidthProperty = new SimpleDoubleProperty(screenWidth);

        // Lega la larghezza dello ScrollPane alla larghezza dello schermo
        serviceGrid.prefWidthProperty().bind(screenWidthProperty);

        ricordaRitiroLabel.setFont(Font.font(13));
        statoPrenotazioneERitiro.setFont(Font.font(20));

        if(model.isWorker()){
            //se non è un lavoratore
            prenotationPickUpButton.setText("Inserisci disponibilità ritiro passaporto");
        }else{
            if(!model.passaportoPrenotato){
                statoPrenotazioneERitiro.setText("SERVIZIO: NON PRENOTATO\t\tRITIRO PASSAPORTO: NON PRENOTATO");
                ricordaRitiroLabel.setText("Prenota l'appuntamento per il servizio desiderato.\n" +
                        "Successivamente prenota il ritiro del tuo passaporto.");
            }else if(model.passaportoPrenotato && !model.ritiroPrenotato){
                statoPrenotazioneERitiro.setText("SERVIZIO: PRENOTATO\t\tRITIRO PASSAPORTO: NON PRENOTATO");
                ricordaRitiroLabel.setFont(Font.font(20));
                ricordaRitiroLabel.setText("Ricordati di prenotare il ritiro\n" +
                        "del tuo passaporto!");
            }
            if(model.ritiroPrenotato){
                statoPrenotazioneERitiro.setText("SERVIZIO: PRENOTATO\t\tRITIRO PASSAPORTO: PRENOTATO");
                ricordaRitiroLabel.setText("Hai prenotato sia il servizio\n" +
                        "sia il ritiro.\n" +
                        "Vai alla schermata \"Prenotazioni\" con il pulsante in alto\n" +
                        "al centro per vedere le informazioni sulle tue prenotazioni");
            }

            try {
                if(!model.notificationSeen()) //se ci sono notifiche da vedere allora attiva
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
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("SelectOfficeSceneForPrenotation.fxml")));
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
