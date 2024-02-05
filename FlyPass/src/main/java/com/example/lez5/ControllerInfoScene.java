package com.example.lez5;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ControllerInfoScene extends Controller implements Initializable {
    @FXML
    private Stage stage;
    private Scene scene;
    @FXML
    private ImageView prenotationImg;
    @FXML
    private Label infoLabel;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(model.isWorker()){ //lavoratore

        }else{ // cittadino
            if(model.notification){
                model.putNotification(prenotationImg);
            }
        }
        String info = "Benvenuti nel sistema di prenotazione online per il rilascio del passaporto.  \n" +
                "Questa guida vi accompagnerà attraverso il processo di registrazione, prenotazione dei servizi e tutte le fasi necessarie per ottenere il vostro passaporto in modo efficiente.\n\n" +
                "1. Registrazione:\n  " +
                "- Accedete al sito ufficiale del sistema di prenotazione.   \n" +
                "- Cliccate su \"Registrazione\" e fornite i vostri dati personali: nome, cognome, data e luogo di nascita, e codice fiscale.  \n" +
                "- Verificate attentamente i dati inseriti e procedete con la registrazione.\n\n" +
                "2. Verifica Anagrafica: \n" +
                "- Il sistema verifica i vostri dati rispetto all'anagrafica disponibile.\n" +
                "- In caso di anomalie, il sistema vi indicherà l'anomalia e fornirà un'email per domande di chiarimento.\n\n" +
                "3. Accesso al Sistema:\n" +
                "- Una volta registrati, effettuate il login con le credenziali fornite durante la registrazione.\n\n" +
                "4. Scelta del Servizio:\n" +
                "- Nella dashboard, selezionate il tipo di servizio desiderato (ritiro, rilascio per varie ragioni).\n" +
                "- Visualizzerete gli orari e le sedi disponibili per il servizio scelto.\n\n" +
                "5. Prenotazione:\n" +
                "- Scegliete il giorno, l'orario e la sede che preferite.\n" +
                "- Assicuratevi che la prenotazione sia consistente con le regole del sistema (es. ritiro dopo almeno un mese dalla richiesta di rilascio).\n\n" +
                "6. Conferma e Documenti Necessari:\n" +
                "- Confermate la prenotazione e il sistema vi mostrerà i documenti e le ricevute necessari al momento della richiesta.\n" +
                "- Assicuratevi di avere tutti i documenti richiesti prima della visita.\n\n" +
                "7. Visualizzazione Temporale:\n" +
                "- Utilizzate la visualizzazione temporale per distinguere slot occupati, liberi e non ancora gestiti.\n" +
                "- Se avete richiesto di essere avvisati quando nuovi slot saranno disponibili, il sistema vi informerà.\n\n" +
                "9. Fine della Procedura:\n" +
                "- Una volta completata la prenotazione e preparati i documenti, presentatevi alla sede indicata al momento programmato.\n" +
                "Seguendo questa guida passo dopo passo, sarete in grado di utilizzare il sistema di prenotazione in modo efficace e ottenere il vostro passaporto senza intoppi.\n" +
                "Per ulteriori domande o chiarimenti, potete sempre contattare il supporto clienti tramite l'email fornita dal sistema: aiuto@questura.it";

        infoLabel.setText(info);
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
    void returnMainScene(ActionEvent event) throws IOException {
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
    void goToPrenotationScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("PrenotationScene.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
