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
    private Text year;
    @FXML
    private Text month;
    @FXML
    private FlowPane calendar;
    //ArrayList<ZonedDateTime> checkInserimentoPrenotazione = new ArrayList<>();
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
            prenotaEvento.setText("Inserisci slot");
            annullaPrenotaEvento.setText("Rimuovi slot");
        } else{
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

            //ANNULLAMENTO PRENOTAZIONE CITTADINO
            //controllo se ci sono notifiche per quella data, setto tutto a disponibile e seen a 0
            if(model.thereAreNotification(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue())){
                model.setNotificationDefinito(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue());
                model.setNotificationNotSeen(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue());
                System.out.println("sono entrato nell'if ci sono ntofiche cittaidno \n");
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
                //TODO ANNULLAMENTO PRENOTAZIONE CITTADINO
                //controllo se ci sono notifiche per quella data, setto tutto a disponibile e seen a 0
                if(model.thereAreNotification(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue())){
                    model.setNotificationNonDefinito(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue());
                    System.out.println("cancellazione da parte del worker\n");
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
        /*if (!Model.getModel().isWorker()) {
//------------------------------CALENDARIO CITTADINO------------------------------------------------------
            if(!model.passaportoPrenotato){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attenzione");
                alert.setHeaderText(null);
                alert.setContentText("Non è stato possibile rilevare nessun passaporto da lei prenotato.");
                alert.showAndWait();
                return;
            }else {
                try {
                    Connection connection = DatabaseConnection.databaseConnection();
                    Statement statement = connection.createStatement();

                    String query = ("UPDATE eventi SET Id_utente_prenotazione = 0, Prenotato = 0 WHERE Id_utente_prenotazione = ?");
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, model.idUtente);

                    preparedStatement.executeUpdate();

                    String query2 = "UPDATE citizen SET PassaportoPrenotato = 0 WHERE id = ?";

                    Connection connection2 = DatabaseConnection.databaseConnection();
                    Statement statement2 = connection2.createStatement();

                    PreparedStatement preparedStatement2 = connection2.prepareStatement(query2);
                    preparedStatement2.setInt(1, model.idUtente);

                    preparedStatement2.executeUpdate();

                    model.passaportoPrenotato = false;

                    //CHIUSURA CONNESSIONE
                    closeConnection(connection2, statement2, preparedStatement2);

                    //CHIUSURA CONNESSIONE
                    closeConnection(connection, statement, preparedStatement);

                    prenotaEvento.setVisible(true);
                    annullaPrenotaEvento.setVisible(false);

                    calendar.getChildren().clear();
                    drawCalendar();

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Prenotazione annullata");
                    alert.setHeaderText(null);
                    alert.setContentText("La prenotazione è stata annullata con successo");
                    alert.showAndWait();


                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        } else {
//------------------------------CALENDARIO LAVORATORE------------------------------------------------------
            try {
                Connection connection = DatabaseConnection.databaseConnection();
                Statement statement = connection.createStatement();
                String query = ("SELECT * FROM eventi " +
                        "WHERE Data = ? " +
                        "AND Inizio = ? " +
                        "AND Sede = ? " +
                        "AND TipoServizio = ? ");
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setDate(1, Date.valueOf(EventDatePicker.getValue()));
                preparedStatement.setObject(2, TimePicker.getValue());
                preparedStatement.setString(3, model.evento.sede.name());
                preparedStatement.setString(4, model.getService().getName());
                ResultSet resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Attention");
                    alert.setHeaderText(null);
                    alert.setContentText("Non è stato possibile rilevare l'appuntamento. Cambiare data ed orario e riprovare");
                    alert.showAndWait();

                    //CHIUSURA CONNESSIONI
                    closeConnection(connection, statement, preparedStatement);
                    resultSet.close();
                    return;
                }

                if (!resultSet.getBoolean("Disponibile")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Errore");
                    alert.setHeaderText(null);
                    alert.setContentText("La cancellazione dell'evento non può essere effettuata poichè non esiste\n" +
                            "alcun evento programmato per questo slot temporale");
                    alert.showAndWait();
                    //--------------------------CANCELLAZIONE APPUNTAMENTO----------------------------------
                } else if ((resultSet.getBoolean("Disponibile") && !resultSet.getBoolean("Prenotato"))) {
                    // IL WORKER DISABILITA LA DISPONIBILITA' DELL'EVENTO
                    if(model.getLoginUserName().equals(resultSet.getString("Worker"))){
                        try {
                            String query1 = ("UPDATE eventi SET Disponibile = 0, Worker = NULL WHERE Data = ? AND Inizio = ?" +
                                    " AND Sede = ? AND TipoServizio = ? ");

                            Connection connection1 = DatabaseConnection.databaseConnection();
                            Statement statement1 = connection1.createStatement();

                            PreparedStatement preparedStatement1 = connection1.prepareStatement(query1);
                            preparedStatement1.setDate(1, Date.valueOf(EventDatePicker.getValue()));
                            preparedStatement1.setObject(2, TimePicker.getValue());
                            preparedStatement1.setString(3, model.evento.sede.name());
                            preparedStatement1.setString(4, model.getService().getName());
                            preparedStatement1.executeUpdate();
                            //CHIUSURA CONNESSIONE
                            closeConnection(connection1, statement1, preparedStatement1);

                            calendar.getChildren().clear();
                            drawCalendar();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Slot cancellato");
                        alert.setHeaderText(null);
                        alert.setContentText("La cancellazione dell'evento è avvenuta correttamente.");
                        alert.showAndWait();

                    //Il worker sta cercando di cancellare lo slot di un altro worker
                    }else{
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Slot non cancellabile");
                        alert.setHeaderText(null);
                        alert.setContentText("La cancellazione dell'evento non può essere cancellata poichè\n" +
                                "l'evento non è stato creato da lei");
                        alert.showAndWait();
                    }

                    //----------------------SLOT GIA' PRENOTATO-----------------------------------------
                } else if ((resultSet.getBoolean("Disponibile") && resultSet.getBoolean("Prenotato"))) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Slot prenotato");
                    alert.setHeaderText(null);
                    alert.setContentText("Lo slot non può essere cancellato poichè\nè già stato prenotato");
                    alert.showAndWait();
                }
                //CHIUSURA CONNESSIONE
                closeConnection(connection, statement, preparedStatement);
                resultSet.close();
            } catch (SQLException e) {
                System.out.println(e);
            }

        }*/
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
                if(model.thereAreNotification(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue())){
                    model.setNotificationOccupato(Date.valueOf(EventDatePicker.getValue()), TimePicker.getValue());
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
                        "La lista dei documenti da portare sarà visualizzabile su prenotation alla chiusura di questo avviso");
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
                        /*try {
                            Connection connection = DatabaseConnection.databaseConnection();
                            Statement statement = connection.createStatement();

                            String query = ("SELECT * FROM eventi " +
                                    "WHERE Data = ? " +
                                    "AND Inizio = ? " +
                                    "AND Sede = ? " +
                                    "AND TipoServizio = ? ");
                            PreparedStatement preparedStatement = connection.prepareStatement(query);
                            preparedStatement.setDate(1, Date.valueOf(auxDate.toLocalDate()));
                            preparedStatement.setObject(2, localTime);
                            preparedStatement.setString(3, model.evento.sede.name());
                            preparedStatement.setString(4, model.getService().getName());
                            ResultSet resultSet = preparedStatement.executeQuery();
                            if (!resultSet.next()) {
                                query = "INSERT INTO `eventi` (`Data`, `Inizio`, `Fine`, `Disponibile`, `Prenotato`, `Sede`, `TipoServizio`) VALUES (?, ?, ?, 0, 0, ?, ?)";
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.setDate(1, Date.valueOf(auxDate.toLocalDate()));
                                preparedStatement.setObject(2, localTime);
                                preparedStatement.setObject(3, localTime.plusHours(1));
                                preparedStatement.setString(4, model.evento.sede.name());
                                preparedStatement.setString(5, model.getService().getName());

                                preparedStatement.executeUpdate();


                                query = ("SELECT * FROM eventi " +
                                        "WHERE Data = ? " +
                                        "AND Inizio = ? " +
                                        "AND Sede = ? " +
                                        "AND TipoServizio = ? ");

                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.setDate(1, Date.valueOf(auxDate.toLocalDate()));
                                preparedStatement.setObject(2, localTime);
                                preparedStatement.setString(3, model.evento.sede.name());
                                preparedStatement.setString(4, model.getService().getName());

                                resultSet = preparedStatement.executeQuery();
                                resultSet.next();

                                //CHIUSURA CONNESSIONE
                                //closeConnection(connection, statement, preparedStatement);
                            }*/

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
