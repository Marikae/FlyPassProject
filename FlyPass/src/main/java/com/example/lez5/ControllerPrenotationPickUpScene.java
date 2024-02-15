package com.example.lez5;

import javafx.collections.FXCollections;
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
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.sql.*;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;
public class ControllerPrenotationPickUpScene extends Controller implements Initializable {
    @FXML
    private Button prenotaEvento;
    @FXML
    private Button annullaPrenotaEvento;
    @FXML
    private Label ErrorePrenotazione;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label serviceName;
    private ImageView legendaVerde;
    @FXML
    private ImageView legendaArancione;
    @FXML
    private ImageView legendaAzzurro;
    @FXML
    private ImageView legendaRosso;
    @FXML
    private Label labelVerde;
    @FXML
    private Label labelRosso;
    @FXML
    private Label labelAzzurro;
    @FXML
    private Label labelArancione;
    @FXML
    private Label labelSopraForm;
    @FXML
    private ComboBox<LocalTime> TimePicker;
    @FXML
    private DatePicker EventDatePicker;
    @FXML
    private Text year;
    @FXML
    private Text month;
    @FXML
    private Label giorniDistanza;
    @FXML
    private FlowPane calendar;
    //ArrayList<ZonedDateTime> checkInserimentoPrenotazione = new ArrayList<>();
    private Stage stage;
    private Scene scene;
    public ControllerPrenotationPickUpScene(){
        super();
    }
    @FXML
    private ImageView prenotationImg;
    ZonedDateTime dateFocus;
    ZonedDateTime today;
    ZonedDateTime auxDate;
    ZonedDateTime auxDate2 = dateFocus;

    int currentDate;
    int numberOfWeek;

    String ritiropassaporto = "Prenotazione ritiro passaporto";
    //public Model model;

    LocalTime primoTurno = LocalTime.of(8,0,0);
    LocalTime secondoTurno = LocalTime.of(9,0,0);
    LocalTime terzoTurno = LocalTime.of(10,0,0);
    LocalTime quartoTurno = LocalTime.of(11,0,0);
    LocalTime quintoTurno = LocalTime.of(12,0,0);



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        numberOfWeek = today.getDayOfMonth() / 5;
        TimePicker.setItems(FXCollections.observableArrayList(primoTurno, secondoTurno, terzoTurno, quartoTurno, quintoTurno));
        drawCalendar();
        descriptionLabel.setText("Scegli un giorno per prenotare il ritiro del tuo passaporto.\n" +
                "Ricorda che devi aspettare almeno 30 giorni dalla data di richiesta del passaporto");
        serviceName.setText(ritiropassaporto);

        if(model.notification){
            model.putNotification(prenotationImg);
        }

        if(model.isWorker()){
            labelSopraForm.setFont(Font.font(18));
            labelSopraForm.setText("Inserisci data e orario per\n" +
                    "aggiungere o rimuovere lo slot");
            labelRosso.setText("Slot non prenotabile\n" +
                    "da un cittadino");
            labelVerde.setText("Slot prenotabile\n" +
                    "da un cittadino");
            labelAzzurro.setText("Slot già prenotato\n" +
                    "da un cittadino");
            labelArancione.setVisible(false);
            legendaArancione.setVisible(false);

            prenotaEvento.setText("Inserisci slot");
            annullaPrenotaEvento.setText("Rimuovi slot");
        }else {
            giorniDistanza.setFont(Font.font(16));
            giorniDistanza.setText("Ricorda di lasciare 30 giorni \n" +
                    "di distanza tra la data del tuo\n" +
                    "primo appuntamento e la data  \n" +
                    "del ritiro che stai prenotando\n" +
                    "ora");

            labelRosso.setText("Slot non disponibile\n" +
                    "per la prenotazione");
            labelVerde.setText("Slot disponibile\n" +
                    "per la prenotazione");
            labelAzzurro.setText("Slot già prenotato\n" +
                    "da un cittadino");
            labelArancione.setText("Slot prenotato da te");
            if (model.ritiroPrenotato) {
                prenotaEvento.setVisible(false);
            } else {
                annullaPrenotaEvento.setVisible(false);
            }
        }

    }

    @FXML
    private void allertClickOnCalendario(MouseEvent event){
        if(model.isWorker()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inserisci dati");
            alert.setHeaderText(null);
            alert.setContentText("inserisci data e ora nel form sulla destra\n" +
                    "per inserire o rimuovere uno slot");
            alert.getButtonTypes().clear();
            alert.getButtonTypes().add(ButtonType.OK);
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inserisci dati");
            alert.setHeaderText(null);
            alert.setContentText("inserisci data e ora nel form sulla destra\n" +
                    "per prenotare il giorno selezionato");
            alert.getButtonTypes().clear();
            alert.getButtonTypes().add(ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    void backOneWeek(ActionEvent event) {
        auxDate2 = dateFocus;
        dateFocus = dateFocus.minusWeeks(1);

        if(dateFocus.getMonth() != auxDate2.getMonth() && auxDate2.getDayOfMonth() == 1){

            int monthMaxDate = dateFocus.getMonth().maxLength();
            //Check for leap year
            if(dateFocus.getYear() % 4 != 0 && monthMaxDate == 29){
                monthMaxDate = 28;
            }
            dateFocus = ZonedDateTime.of(dateFocus.getYear(),dateFocus.getMonthValue(),monthMaxDate,0,0,0,0,dateFocus.getZone());
        }
        else if(dateFocus.getMonth() != auxDate2.getMonth()){
            dateFocus = ZonedDateTime.of(auxDate2.getYear(),auxDate2.getMonthValue(),1,0,0,0,0,dateFocus.getZone());
        }
        //checkInserimentoPrenotazione.clear();
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    private void annullaPrenotaEvento(ActionEvent event) throws SQLException {
        if (!Model.getModel().isWorker()) {
//------------------------------CALENDARIO CITTADINO------------------------------------------------------
            if (!model.annullaPrenotaRitiroPassaportoCittadino()) {
                ErrorePrenotazione.setText("");
                return;
            }

            prenotaEvento.setVisible(true);
            annullaPrenotaEvento.setVisible(false);

            calendar.getChildren().clear();
            drawCalendar();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Prenotazione annullata");
            alert.setHeaderText(null);
            alert.setContentText("La prenotazione è stata annullata con successo");
            alert.getButtonTypes().clear();
            // Aggiungi solo il tipo di pulsante OK
            alert.getButtonTypes().add(ButtonType.OK);
            alert.showAndWait();
            ErrorePrenotazione.setText("");

            //TODO ANNULLAMENTO PRENOTAZIONE CITTADINO
            //controllo se ci sono notifiche per quella data, setto tutto a definito e seen a 0
            if(model.thereAreNotification(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue(), model.ritiroPassaporto)){
                model.setNotificationDefinito(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue(), model.ritiroPassaporto);
                model.setNotificationNotSeen(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue(), model.ritiroPassaporto);
                //System.out.println("sono entrato nell'if ci sono ntofiche cittaidno \n");
            }

        } else {
//------------------------------CALENDARIO LAVORATORE------------------------------------------------------
            if(EventDatePicker.getValue() == null) {
                ErrorePrenotazione.setTextFill(Color.RED);
                ErrorePrenotazione.setText("Nessuna data selezionata");
                return;
            }

            if(TimePicker.getValue() == null){
                ErrorePrenotazione.setTextFill(Color.RED);
                ErrorePrenotazione.setText("Nessun orario selezionato");
                return;
            }
            if (!model.annullaPrenotaRitiroPassaportoWorker(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue())) {
                ErrorePrenotazione.setText("");
                return;
            } else {
                calendar.getChildren().clear();
                drawCalendar();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Slot cancellato");
                alert.setHeaderText(null);
                alert.setContentText("La cancellazione dell'evento è avvenuta correttamente.");
                alert.getButtonTypes().clear();
                // Aggiungi solo il tipo di pulsante OK
                alert.getButtonTypes().add(ButtonType.OK);
                alert.showAndWait();

                //TODO ANNULLAMENTO PRENOTAZIONE LAVORATORE
                //controllo se ci sono notifiche per quella data, setto tutto a disponibile e seen a 0
                if(model.thereAreNotification(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue(), model.ritiroPassaporto)){
                    model.setNotificationNonDefinito(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue(), model.ritiroPassaporto);
                    //System.out.println("cancellazione da parte del worker\n");
                }
            }
            ErrorePrenotazione.setText("");
        }
    }
    

    @FXML
    private void prenotaEvento (ActionEvent event) throws SQLException {
        if(EventDatePicker.getValue() == null) {
            ErrorePrenotazione.setTextFill(Color.RED);
            ErrorePrenotazione.setText("Nessuna data selezionata");
            return;
        }

        if(TimePicker.getValue() == null){
            ErrorePrenotazione.setTextFill(Color.RED);
            ErrorePrenotazione.setText("Nessun orario selezionato");
            return;
        }

        if (!Model.getModel().isWorker()) {
//------------------------------CALENDARIO CITTADINO------------------------------------------------------
            if(!model.prenotaRitiroPassaportoCittadino(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue())){
                ErrorePrenotazione.setText("");
                return;
            }else{
                calendar.getChildren().clear();
                drawCalendar();

                annullaPrenotaEvento.setVisible(true);
                prenotaEvento.setVisible(false);
                //TODO PRENOTAZIONE APPUNTAMENTO CITTADINO
                //controllo se ci sono notifiche per quella data, setto tutto a occupato
                if(model.thereAreNotification(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue(), model.ritiroPassaporto)){
                    model.setNotificationOccupato(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue(), model.ritiroPassaporto);
                    //model.setNotificationNotSeen(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue());

                }
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Ritiro prenotato");
                alert.setHeaderText(null);
                alert.setContentText("Congratulazioni, il ritiro è stato prenotato correttamente");
                alert.getButtonTypes().clear();
                // Aggiungi solo il tipo di pulsante OK
                alert.getButtonTypes().add(ButtonType.OK);
                alert.showAndWait();
                ErrorePrenotazione.setText("");
            }
            ErrorePrenotazione.setText("");
        } else {
//------------------------------CALENDARIO LAVORATORE------------------------------------------------------
            if(!model.prenotaRitiroPassaportoWorker(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue())){
                ErrorePrenotazione.setText("");
                return;
            }else{
                calendar.getChildren().clear();
                drawCalendar();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Slot inserito");
                alert.setHeaderText(null);
                alert.setContentText("Lo slot è stato inserito correttamente");
                alert.getButtonTypes().clear();
                // Aggiungi solo il tipo di pulsante OK
                alert.getButtonTypes().add(ButtonType.OK);
                alert.showAndWait();
                ErrorePrenotazione.setText("");
            }
            ErrorePrenotazione.setText("");
        }
    }

    @FXML
    void forwardOneWeek(ActionEvent event) {
        auxDate2 = dateFocus;
        dateFocus = dateFocus.plusWeeks(1);
        /*if(numberOfWeek == 4)
            numberOfWeek = 0;
        else
            numberOfWeek += 1;*/
        if(dateFocus.getMonth() != auxDate2.getMonth()){

            int monthMaxDate = dateFocus.getMonth().maxLength();
            //Check for leap year
            if(dateFocus.getYear() % 4 != 0 && monthMaxDate == 29){
                monthMaxDate = 28;
            }
            dateFocus = ZonedDateTime.of(dateFocus.getYear(),dateFocus.getMonthValue(),1,0,0,0,0,dateFocus.getZone());
        }
        //checkInserimentoPrenotazione.clear();
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void drawCalendar(){
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));
        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();
        int monthMaxDate = dateFocus.getMonth().maxLength();
        if(dateFocus.getYear() % 4 != 0 && monthMaxDate == 29){
            monthMaxDate = 28;
        }
        boolean checkDate = false;
        while(!checkDate){
            if(dateFocus.getDayOfMonth() == 1)
                checkDate = true;
            else if(dateFocus.getDayOfWeek().getValue() == 1)
                checkDate = true;

            else
                dateFocus = dateFocus.minusDays(1);
        }
        int dayWrite = 1;
        auxDate = dateFocus;
        if(auxDate.getDayOfMonth() == 1) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();
                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.WHITE);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);
                LocalTime localTime = LocalTime.of(8, 0 , 0);

                if (auxDate.getDayOfWeek().getValue() == (j + 1)) {
                    int i = 0;
                    //checkInserimentoPrenotazione.add(auxDate);
                    VBox vBoxContainer = new VBox(); // Creare un VBox principale per contenere le VBox
                    vBoxContainer.setAlignment(Pos.CENTER);
                    vBoxContainer.setSpacing(20);


                    for (i = 0; i < 5; i++) {

                        Evento evento = model.creazioneCalendarioRitiro(localTime, Date.valueOf(auxDate.toLocalDate()));

                            VBox calendarActivityBox = new VBox();

                            String time = evento.getInizio().toString();
                            if(!evento.isDisponibile()){
                                Text text = new Text(time + "\nNon prenotabile\n\n");
                                calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                                calendarActivityBox.setMaxHeight(rectangleWidth * 0.8);
                                calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                                calendarActivityBox.setMaxHeight(rectangleHeight * 0.05);
                                calendarActivityBox.setStyle("-fx-background-color:#e36363");
                                calendarActivityBox.getChildren().add(text);

                            }else if(evento.isDisponibile() && !evento.isPrenotato()){
                                Text text = new Text(time + "\nSlot prenotabile!\n\n");
                                calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                                calendarActivityBox.setMaxHeight(rectangleWidth * 0.8);
                                calendarActivityBox.setMaxHeight(rectangleHeight * 0.05);
                                calendarActivityBox.setStyle("-fx-background-color:LIGHTGREEN");
                                calendarActivityBox.getChildren().add(text);


                            }else if(evento.isDisponibile() && evento.isPrenotato()){
                                if(evento.getId() == model.idUtente) {
                                    Text text = new Text(time + "\nSlot prenotato   \n da te!\n");
                                    calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                                    calendarActivityBox.setMaxHeight(rectangleWidth * 0.8);
                                    calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                                    calendarActivityBox.setMaxHeight(rectangleHeight * 0.05);
                                    calendarActivityBox.setStyle("-fx-background-color:#F6AE2D");
                                    calendarActivityBox.getChildren().add(text);
                                }
                                else{
                                    Text text = new Text(time + "\nSlot già\nprenotato!         \n");
                                    calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                                    calendarActivityBox.setMaxHeight(rectangleHeight * 0.05);
                                    calendarActivityBox.setStyle("-fx-background-color:LIGHTBLUE");
                                    calendarActivityBox.getChildren().add(text);
                                }
                            }
                            vBoxContainer.getChildren().add(calendarActivityBox); // Aggiungere la VBox al contenitore principale
                            localTime = localTime.plusHours(1);
                    }

                    stackPane.getChildren().add(vBoxContainer);

                    Text date = new Text(String.valueOf(dayWrite));

                    dayWrite += 1;
                    double textTranslationY = -(rectangleHeight / 2) * 0.9;
                    date.setTranslateY(textTranslationY);
                    date.setFill(Color.WHITE);
                    stackPane.getChildren().add(date);
                    auxDate = auxDate.plusDays(1);
                }

                if(today.getYear() == auxDate.getYear() && today.getMonth() == auxDate.getMonth() && today.getDayOfMonth() == currentDate){
                    rectangle.setStroke(Color.RED);
                }

                calendar.getChildren().add(stackPane);
            }
        }else {
            for (int j = 0; j < 7; j++) {
                VBox vBox = new VBox();
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.WHITE);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);
                LocalTime localTime = LocalTime.of(8,0,0);


                if (auxDate.getDayOfWeek().getValue() == (j + 1) && auxDate.getMonth() == dateFocus.getMonth()) {
                    Text date = new Text(String.valueOf(auxDate.getDayOfMonth()));
                    date.setFill(Color.WHITE);
                    double textTranslationY = -(rectangleHeight / 2) * 0.9;
                    date.setTranslateY(textTranslationY);
                    stackPane.getChildren().add(date);

                    int i = 0;
                    //checkInserimentoPrenotazione.add(auxDate);
                    VBox vBoxContainer = new VBox(); // Creare un VBox principale per contenere le VBox
                    vBoxContainer.setAlignment(Pos.CENTER);
                    vBoxContainer.setSpacing(20);


                    for (i = 0; i < 5; i++) {
                        Evento evento = model.creazioneCalendarioRitiro(localTime, Date.valueOf(auxDate.toLocalDate()));

                            VBox calendarActivityBox = new VBox();


                            String time = evento.getInizio().toString();
                            if(!evento.isDisponibile()){
                                Text text = new Text(time + "\nNon prenotabile\n\n");
                                calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                                calendarActivityBox.setMaxHeight(rectangleWidth * 0.8);
                                calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                                calendarActivityBox.setMaxHeight(rectangleHeight * 0.05);
                                calendarActivityBox.setStyle("-fx-background-color:#e36363");
                                calendarActivityBox.getChildren().add(text);

                            }else if(evento.isDisponibile() && !evento.isPrenotato()){
                                Text text = new Text(time + "\nSlot prenotabile!\n\n");
                                calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                                calendarActivityBox.setMaxHeight(rectangleWidth * 0.8);
                                calendarActivityBox.setMaxHeight(rectangleHeight * 0.05);
                                calendarActivityBox.setStyle("-fx-background-color:LIGHTGREEN");
                                calendarActivityBox.getChildren().add(text);

                            }else if(evento.isDisponibile() && evento.isPrenotato()){
                                if(evento.getId() == model.idUtente) {
                                    Text text = new Text(time + "\nSlot prenotato   \n da te!\n");
                                    calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                                    calendarActivityBox.setMaxHeight(rectangleWidth * 0.8);
                                    calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                                    calendarActivityBox.setMaxHeight(rectangleHeight * 0.05);
                                    calendarActivityBox.setStyle("-fx-background-color:#F6AE2D");
                                    calendarActivityBox.getChildren().add(text);
                                }
                                else{
                                    Text text = new Text(time + "\nSlot già\nprenotato!         \n");
                                    calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                                    calendarActivityBox.setMaxHeight(rectangleHeight * 0.05);
                                    calendarActivityBox.setStyle("-fx-background-color:LIGHTBLUE");
                                    calendarActivityBox.getChildren().add(text);
                                }
                            }
                            vBoxContainer.getChildren().add(calendarActivityBox); // Aggiungere la VBox al contenitore principale
                            localTime = localTime.plusHours(1);
                    }
                    stackPane.getChildren().add(vBoxContainer);
                }

                if(today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate){
                    rectangle.setStroke(Color.BLUE);
                }

                calendar.getChildren().add(stackPane);

                auxDate = auxDate.plusDays(1);
            }
        }
    }


    //----------------------------CAMBIO SCENA-------------------------------------------------------
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
    void returnSedeScene(ActionEvent event) throws IOException {
        if(!model.isWorker()) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("SelectOfficeSceneForPrenotation.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
        }else {
            returnMainScene(event);
        }
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

}