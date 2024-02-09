package com.example.lez5;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class ControllerPrenotationScene extends Controller implements Initializable {
    @FXML
    private Stage stage;
    @FXML
    private ImageView prenotationImg;
    private Scene scene;
    @FXML
    private Button deleteRitiro;
    @FXML
    private Button logOutM;
    @FXML
    private Label newNotLabel;
    @FXML
    private Label oldNotLabel;
    @FXML
    private Label preRitiroLabel;
    @FXML
    private Button deletePrenotation;

    @FXML
    private Label prenotationLabel;
    private String newNot;
    private String oldNot;
    private String bookedPrenotation;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(model.isWorker()){ //lavoratore
            try {
                workerPrenotation();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else{ //cittadino
            try {
                citizenPrenotation();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void citizenPrenotation() throws SQLException {

        prenotationLabel.setText("");
        newNotLabel.setText("");
        preRitiroLabel.setText("");


        String preString = model.getCitizenPrenotation();
        if(!preString.equals("Prenotazione ancora da effettuare!\n")){ //prenotazione effettuata
            prenotationLabel.setText(preString);
            newNotLabel.setText("Ricordati di portare:\n" +
                    "1) Il modulo di richiesta compilato\n" +
                    "2) Una marca da bollo\n" +
                    "3) La ricevuta del versamento sul C/C postale\n" +
                    "4) Due fototessere su sfondo bianco\n" +
                    "5) Il passaporto precendete (se ancora in possesso)\n");
            preRitiroLabel.setText(model.getCitizenRitiro());
            if(preRitiroLabel.getText().equals("Prenotazione per il ritiro passaporto\n ancora da effettuare!\n")) {
                deleteRitiro.setVisible(false);
            }else{
                deletePrenotation.setVisible(false);
            }

            //model.activeNotification();
        }else{ //la prenotazione deve ancora avvenire
            //model.notification();
            deletePrenotation.setVisible(false);
            deleteRitiro.setVisible(false);
            newNot = model.getNewNotification();
            newNotLabel.setText(newNot);
            oldNot = model.getOldNotification();
            oldNotLabel.setText(oldNot);
            model.setNotificationSeen();
        }



    }

    private void workerPrenotation() throws SQLException {
        //String notificationAvaibility = model.getNewNotification();
        newNotLabel.setText("");
        bookedPrenotation = model.getBookedReservation();
        newNotLabel.setText(bookedPrenotation);
        deleteRitiro.setVisible(false);
        deletePrenotation.setVisible(false);
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
    @FXML
    void goToInfoScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("InfoScene.fxml")));
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
    void deleteReservation(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attenzione");
        alert.setHeaderText("Sei sicuro di voler cancellare la prenotazione?\n" +
                "Lo slot potrebbe essere cancellato o prenotato da un altro utente");

        // Impostazione dei pulsanti OK e Annulla
        ButtonType buttonTypeOK = new ButtonType("OK");
        ButtonType buttonTypeCancel = new ButtonType("Annulla");
        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);

        // Visualizzazione dell'alert e attesa della risposta dell'utente
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeOK) {
                boolean b = model.annullaPrenotaEventoCittadino();

                if(model.isWorker()){ //lavoratore
                    try {
                        workerPrenotation();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }else{ //cittadino
                    try {
                        citizenPrenotation();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if (buttonType == buttonTypeCancel) {
                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert1.setTitle("Azione annullata");
                alert1.setHeaderText(null);
                alert1.setContentText("Azione annullata, la prenotazione non ha subito variazioni");
                alert1.showAndWait();
            }
        });
    }

    @FXML
    void deleteRitiro(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attenzione");
        alert.setHeaderText("Sei sicuro di cancellare la prenotazione del ritiro?\n" +
                "Lo slot potrebbe essere eliminato o prenotato da un altro utente.");

        // Impostazione dei pulsanti OK e Annulla
        ButtonType buttonTypeOK = new ButtonType("OK");
        ButtonType buttonTypeCancel = new ButtonType("Annulla");
        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);

        // Visualizzazione dell'alert e attesa della risposta dell'utente
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeOK) {
                boolean b = model.annullaPrenotaRitiroPassaportoCittadino();

                if(model.isWorker()){ //lavoratore
                    try {
                        workerPrenotation();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }else{ //cittadino
                    try {
                        citizenPrenotation();
                        deletePrenotation.setVisible(true);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if (buttonType == buttonTypeCancel) {
                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert1.setTitle("Azione annullata");
                alert1.setHeaderText(null);
                alert1.setContentText("Azione annullata, la prenotazione del ritiro non ha subito variazioni");
                alert1.showAndWait();
            }
        });
    }
}
