package com.example.lez5;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

public class ControllerCalendarScene extends Controller implements Initializable {
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
    @FXML
    private ComboBox<LocalTime> TimePicker;
    @FXML
    private DatePicker EventDatePicker;
    @FXML
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
    private Text year;
    @FXML
    private Text month;
    @FXML
    private Label labelSopraForm;
    @FXML
    private FlowPane calendar;
    private Stage stage;
    private Scene scene;
    public ControllerCalendarScene(){
        super();
    }
    @FXML
    private ImageView prenotationImg;
    ZonedDateTime dateFocus;
    ZonedDateTime today;
    ZonedDateTime auxDate;
    ZonedDateTime auxDate2 = dateFocus;

    Boolean notificationAlredyExist = null;

    int currentDate;
    int numberOfWeek;


    LocalTime primoTurno = LocalTime.of(8,0,0);
    LocalTime secondoTurno = LocalTime.of(9,0,0);
    LocalTime terzoTurno = LocalTime.of(10,0,0);
    LocalTime quartoTurno = LocalTime.of(11,0,0);
    LocalTime quintoTurno = LocalTime.of(12,0,0);


    public void closeConnection(Connection connection, Statement statement, PreparedStatement preparedStatement) throws SQLException {
        //CHIUSURA CONNESSIONI
        connection.close();
        statement.close();
        preparedStatement.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        numberOfWeek = today.getDayOfMonth() / 5;
        TimePicker.setItems(FXCollections.observableArrayList(primoTurno, secondoTurno, terzoTurno, quartoTurno, quintoTurno));
        drawCalendar();
        descriptionLabel.setText(model.getService().getDescription());
        serviceName.setText(model.getService().getName());

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
        } else{
            labelRosso.setText("Slot non disponibile\n" +
                    "per la prenotazione");
            labelVerde.setText("Slot disponibile\n" +
                    "per la prenotazione");
            labelAzzurro.setText("Slot già prenotato\n" +
                    "da un cittadino");
            labelArancione.setText("Slot prenotato da te");
            if (model.passaportoPrenotato) {
                prenotaEvento.setVisible(false);
            } else {
                annullaPrenotaEvento.setVisible(false);
            }
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
            if(!model.annullaPrenotaEventoCittadino()){
                ErrorePrenotazione.setText("");
                return;
            }

            prenotaEvento.setVisible(true); //bottone per prenotare visibile
            annullaPrenotaEvento.setVisible(false); //bottone per annullare invisibile

            calendar.getChildren().clear();
            drawCalendar();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Prenotazione annullata");
            alert.setHeaderText(null);
            alert.setContentText("La prenotazione è stata annullata con successo");
            alert.getButtonTypes().clear();
            alert.getButtonTypes().add(ButtonType.OK);
            alert.showAndWait();

            //TODO ANNULLAMENTO PRENOTAZIONE CITTADINO
            //controllo se ci sono notifiche per quella data, setto tutto a definito e seen a 0
            if(model.thereAreNotification(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue(), model.getService().getName())){
                model.setNotificationDefinito(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue(), model.getService().getName());
                model.setNotificationNotSeen(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue(), model.getService().getName());
                //System.out.println("sono entrato nell'if ci sono ntofiche cittaidno \n");
            }

            ErrorePrenotazione.setText("");

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

            if(!model.annullaPrenotazioneEventoWorker(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue())){
                ErrorePrenotazione.setText("");
                return;
            }else{
                calendar.getChildren().clear();
                drawCalendar();
                //TODO ANNULLAMENTO PRENOTAZIONE LAVORATORE
                //controllo se ci sono notifiche per quella data, setto tutto a disponibile e seen a 0
                if(model.thereAreNotification(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue(), model.getService().getName())){
                    model.setNotificationNonDefinito(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue(), model.getService().getName());
                    //System.out.println("cancellazione da parte del worker\n");
                }
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Slot cancellato");
                alert.setHeaderText(null);
                alert.setContentText("La cancellazione dell'evento è avvenuta correttamente.");
                alert.getButtonTypes().clear();
                // Aggiungi solo il tipo di pulsante OK
                alert.getButtonTypes().add(ButtonType.OK);
                alert.showAndWait();

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
            if(!model.prenotaEventoCittadino(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue())){
                ErrorePrenotazione.setText("");
                return;
            }else{
                calendar.getChildren().clear();

                //TODO PRENOTAZIONE APPUNTAMENTO CITTADINO
                //controllo se ci sono notifiche per quella data, setto tutto a occupato
                if(model.thereAreNotification(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue(), model.getService().getName())){
                    model.setNotificationOccupato(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue(), model.getService().getName());
                    //model.setNotificationNotSeen(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue());

                }
                drawCalendar();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Conferma prenotazione!");
                alert.setHeaderText(null);
                alert.setContentText("Prenotazione andata a buon fine.\n" +
                        "Ricorda di portare:\n" +
                        "1) Il modulo di richiesta compilato\n" +
                        "2) Una marca da bollo\n" +
                        "3) La ricevuta del versamento sul C/C postale\n" +
                        "4) Due fototessere su sfondo bianco\n" +
                        "5) Il passaporto precedente (se ancora in possesso)\n\n\n" +
                        "La lista dei documenti da portare sarà visualizzabile su prenotation alla chiusura di questo avviso\n\n" +
                        "RICORDATI DI PRENOTARE IL RITIRO DEL TUO PASSAPORTO");
                alert.getButtonTypes().clear();
                alert.getButtonTypes().add(ButtonType.OK);
                alert.showAndWait();

                annullaPrenotaEvento.setVisible(true);
                prenotaEvento.setVisible(false);
            }
            ErrorePrenotazione.setText("");
        } else {
//------------------------------CALENDARIO LAVORATORE------------------------------------------------------
            if(!model.prenotaEventoWorker(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue())){
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

        //PRIMA SETTIMANA DEL MESE
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


                        Evento evento = model.creazioneCalendario(localTime, Date.valueOf(auxDate.toLocalDate()));

                        if (evento == null) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Attenzione");
                            alert.setHeaderText(null);
                            alert.setContentText("Impossibile stampare il calendario\n" +
                                    "Per eventuali informazioni scrivere a info@questura.it");
                            alert.getButtonTypes().clear();
                            // Aggiungi solo il tipo di pulsante OK
                            alert.getButtonTypes().add(ButtonType.OK);
                            alert.showAndWait();
                        }

                        VBox calendarActivityBox = new VBox();

                        String time = evento.getInizio().toString();
                        if (!evento.isDisponibile()) {
                            Text text = new Text(time + "\nNon prenotabile\n\n");
                            calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                            calendarActivityBox.setMaxHeight(rectangleWidth * 0.8);
                            calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                            calendarActivityBox.setMaxHeight(rectangleHeight * 0.05);
                            calendarActivityBox.setStyle("-fx-background-color:#e36363");
                            calendarActivityBox.getChildren().add(text);


                        } else if ((evento.isDisponibile() && !evento.isPrenotato())) {
                            Text text = new Text(time + "\nSlot prenotabile!\n\n");
                            calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                            calendarActivityBox.setMaxHeight(rectangleWidth * 0.8);
                            calendarActivityBox.setMaxHeight(rectangleHeight * 0.05);
                            calendarActivityBox.setStyle("-fx-background-color:LIGHTGREEN");
                            calendarActivityBox.getChildren().add(text);
                        } else if ((evento.isDisponibile() && evento.isPrenotato())) {
                            if (evento.getId() == model.idUtente) {
                                Text text = new Text(time + "\nSlot prenotato   \n da te!\n");
                                calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                                calendarActivityBox.setMaxHeight(rectangleWidth * 0.8);
                                calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                                calendarActivityBox.setMaxHeight(rectangleHeight * 0.05);
                                calendarActivityBox.setStyle("-fx-background-color:#F6AE2D");
                                calendarActivityBox.getChildren().add(text);
                            } else {
                                String endTime = evento.getInizio().toString();
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

                        Evento evento = model.creazioneCalendario(localTime, Date.valueOf(auxDate.toLocalDate()));

                        if (evento == null) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Attenzione");
                            alert.setHeaderText(null);
                            alert.setContentText("Impossibile stampare il calendario\n" +
                                    "Per eventuali informazioni scrivere a info@questura.it");
                            alert.getButtonTypes().clear();
                            // Aggiungi solo il tipo di pulsante OK
                            alert.getButtonTypes().add(ButtonType.OK);
                            alert.showAndWait();
                        }

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
           Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("SelectOfficeScene.fxml")));
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
