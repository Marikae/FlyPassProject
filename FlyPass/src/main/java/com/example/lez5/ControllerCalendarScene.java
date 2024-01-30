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
import java.util.*;
public class ControllerCalendarScene extends Controller implements Initializable {
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
    ArrayList<ZonedDateTime> checkInserimentoPrenotazione = new ArrayList<>();
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

    int currentDate;
    int numberOfWeek;

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
        descriptionLabel.setText(model.getService().getDescription());
        serviceName.setText(model.getService().getName());

        if(model.notification){
            model.putNotification(prenotationImg);
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
        checkInserimentoPrenotazione.clear();
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    private void prenotaEvento (ActionEvent event){
        boolean isInside = false;
        for(ZonedDateTime auxDateCheck : checkInserimentoPrenotazione){
            if (auxDateCheck.toLocalDate().isEqual(EventDatePicker.getValue())){
                isInside = true;
                break;
            }
        }
        if(isInside == false){
            //ErrorePrenotazione.setTextFill(Color.web("#FF0000"));
            //ErrorePrenotazione.setText("La data che hai selezionato\n non è presente nella schermata");
            //return;
            //è triggherante
        }
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
                //ErrorePrenotazione.setTextFill(Color.web("#FF0000"));
                //ErrorePrenotazione.setText("Non è stato possibile rilevare l'appuntamento.\n Cambiare data ed orario e riprovare");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setHeaderText(null);
                alert.setContentText("Non è stato possibile rilevare l'appuntamento. Cambiare data ed orario e riprovare");
                alert.showAndWait();
                return;
            }
            if(!resultSet.getBoolean("Disponibile")){
                // ErrorePrenotazione.setTextFill(Color.web("#FF0000"));
                // ErrorePrenotazione.setText("L'appuntamento da lei selezionato\n" +
                //     " non è al momento disponibile");
               /*
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setHeaderText(null);
                alert.setContentText("L'appuntamento da lei selezionato non è al momento disponibile");
                alert.showAndWait();
                /*
                */
                //TODO Aggiungere una variabile alla classe User inserendo Sede e Tipo servizio e fare il pop up di avviso quando viene inserito
                // un nuovo appuntamento dal personale

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Appuntamento non disponibile");
                alert.setHeaderText(null);
                // Creare una VBox personalizzata con il messaggio e la CheckBox
                VBox vBox = new VBox();
                vBox.setSpacing(10);
                vBox.setPadding(new Insets(10, 10, 10, 10));
                Label messageLabel = new Label("L'appuntamento non è disponibile. Vuoi ricevere una notifica quando sarà disponibile?");
                CheckBox notificationCheckBox = new CheckBox("Ricevi notifica");
                vBox.getChildren().addAll(messageLabel, notificationCheckBox);
                // Impostare la VBox come contenuto del DialogPane
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.setContent(vBox);
                // Aggiungere i pulsanti desiderati
                dialogPane.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
                // Mostrare l'alert e gestire la risposta
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        boolean receiveNotification = notificationCheckBox.isSelected();
                        System.out.println("Risposta: OK, Ricevi notifica: " + receiveNotification);
                        //TODO salvarsi i dati per inviare alla notifica!!!


                    } else if (response == ButtonType.CANCEL) {
                        System.out.println("Risposta: Annulla");
                    }
                });
                return;
            }else if((resultSet.getBoolean("Disponibile") && !resultSet.getBoolean("Prenotato"))){
                if(model.passaportoPrenotato){
                    //ErrorePrenotazione.setTextFill(Color.web("#FF0000"));
                    //ErrorePrenotazione.setText("Sembra che tu abbia già\n" +
                          //  "prenotato un passaporto.\n" +
                          //  "Togli l'altra prenotazione per aggiungerne\n" +
                          //  "una nuova");
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Attention");
                    alert.setHeaderText(null);
                    alert.setContentText("porcodio coglione di merda hai già prenotato ritardato");
                    alert.showAndWait();
                    return;
                }
                try {
                    String query1 = ("UPDATE eventi SET Prenotato = 1, Id_utente_prenotazione = ? WHERE Data = ? AND Inizio = ? AND Sede = ? AND TipoServizio = ?");
                    Connection connection1 = DatabaseConnection.databaseConnection();
                    Statement statement1 = connection1.createStatement();

                    PreparedStatement preparedStatement1 = connection1.prepareStatement(query1);
                    preparedStatement1.setInt(1, model.idUtente);
                    preparedStatement1.setDate(2, Date.valueOf(EventDatePicker.getValue()));
                    preparedStatement1.setObject(3, TimePicker.getValue());
                    preparedStatement1.setString(4, model.evento.sede.name());
                    preparedStatement1.setString(5, model.getService().getName());

                    preparedStatement1.executeUpdate();


                    query1 = "UPDATE citizen SET PassaportoPrenotato = 1 WHERE id = ?";

                    preparedStatement1 = connection1.prepareStatement(query1);
                    preparedStatement1.setInt(1, model.idUtente);

                    preparedStatement1.executeUpdate();

                    connection1.close();
                    statement1.close();
                    preparedStatement1.close();

                    model.passaportoPrenotato = true;

                    calendar.getChildren().clear();
                    drawCalendar();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                //ErrorePrenotazione.setTextFill(Color.web("#FF0000"));
                //ErrorePrenotazione.setText("Prenotazione andata a buon fine\n" +
                    //    "Ri prenota lo stesso evento per annullare la prenotazione");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("ALL GOOD!");
                alert.setHeaderText(null);
                alert.setContentText("Prenotazione andata a buon fine. \n Ri prenota lo stesso evento per annullare la prenotazione");
                alert.showAndWait();

            }else if((resultSet.getBoolean("Disponibile") && resultSet.getBoolean("Prenotato"))){
                if(resultSet.getInt("Id_utente_prenotazione") == model.idUtente){
                    try {
                        String query2 = ("UPDATE eventi SET Prenotato = 0, Id_utente_prenotazione = 0 WHERE Data = ? AND Inizio = ? AND Sede = ? AND TipoServizio = ?");
                        Connection connection2 = DatabaseConnection.databaseConnection();
                        Statement statement2 = connection2.createStatement();

                        PreparedStatement preparedStatement2 = connection2.prepareStatement(query2);
                        preparedStatement2.setDate(1, Date.valueOf(EventDatePicker.getValue()));
                        preparedStatement2.setObject(2, TimePicker.getValue());
                        preparedStatement2.setString(3, model.evento.sede.name());
                        preparedStatement2.setString(4, model.getService().getName());

                        preparedStatement2.executeUpdate();

                        query2 = "UPDATE citizen SET PassaportoPrenotato = 0 WHERE id = ?";

                        preparedStatement2 = connection2.prepareStatement(query2);
                        preparedStatement2.setInt(1, model.idUtente);

                        preparedStatement2.executeUpdate();

                        model.passaportoPrenotato = false;

                        connection2.close();
                        statement2.close();
                        preparedStatement2.close();

                        calendar.getChildren().clear();
                        drawCalendar();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    //ErrorePrenotazione.setTextFill(Color.web("#FF0000"));
                    //ErrorePrenotazione.setText("Errore di prenotazione.\n Evento Già prenotato da un altro utente");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Errore di prenotazione.\n Evento Già prenotato da un altro utente");
                    alert.showAndWait();
                }
            }
            connection.close();
            statement.close();
            preparedStatement.close();
        }catch (SQLException e) {
            System.out.println(e);
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
        checkInserimentoPrenotazione.clear();
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
                    checkInserimentoPrenotazione.add(auxDate);
                    VBox vBoxContainer = new VBox(); // Creare un VBox principale per contenere le VBox
                    vBoxContainer.setAlignment(Pos.CENTER);
                    vBoxContainer.setSpacing(20);


                    for (i = 0; i < 5; i++) {

                        try {
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
                            }


                            VBox calendarActivityBox = new VBox();


                            String time = resultSet.getTime(2).toString();
                            if(!resultSet.getBoolean("Disponibile")){
                                String endTime = resultSet.getTime(2).toString();
                                Text text = new Text(time + "\nNon prenotabile\n\n");
                                calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                                calendarActivityBox.setMaxHeight(rectangleWidth * 0.8);
                                calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                                calendarActivityBox.setMaxHeight(rectangleHeight * 0.05);
                                calendarActivityBox.setStyle("-fx-background-color:#e36363");
                                calendarActivityBox.getChildren().add(text);

                            }else if((resultSet.getBoolean("Disponibile") && !resultSet.getBoolean("Prenotato"))){
                                String endTime = resultSet.getTime(2).toString();
                                Text text = new Text(time + "\nSlot prenotabile!\n\n");
                                calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                                calendarActivityBox.setMaxHeight(rectangleWidth * 0.8);
                                calendarActivityBox.setMaxHeight(rectangleHeight * 0.05);
                                calendarActivityBox.setStyle("-fx-background-color:LIGHTGREEN");
                                calendarActivityBox.getChildren().add(text);


                            }else if((resultSet.getBoolean("Disponibile") && resultSet.getBoolean("Prenotato"))){
                                if(resultSet.getInt("Id_utente_prenotazione") == model.idUtente) {
                                    String endTime = resultSet.getTime(2).toString();
                                    Text text = new Text(time + "\nSlot prenotato   \n da te!\n");
                                    calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                                    calendarActivityBox.setMaxHeight(rectangleWidth * 0.8);
                                    calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                                    calendarActivityBox.setMaxHeight(rectangleHeight * 0.05);
                                    calendarActivityBox.setStyle("-fx-background-color:#F6AE2D");
                                    calendarActivityBox.getChildren().add(text);
                                }
                                else{
                                    String endTime = resultSet.getTime(2).toString();
                                    Text text = new Text(time + "\nSlot già\nprenotato!         \n");
                                    calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                                    calendarActivityBox.setMaxHeight(rectangleHeight * 0.05);
                                    calendarActivityBox.setStyle("-fx-background-color:LIGHTBLUE");
                                    calendarActivityBox.getChildren().add(text);
                                }
                            }

                            vBoxContainer.getChildren().add(calendarActivityBox); // Aggiungere la VBox al contenitore principale

                            localTime = localTime.plusHours(1);

                            connection.close();
                            statement.close();
                            preparedStatement.close();

                        }catch (SQLException e) {
                            System.out.println(e);
                        }
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

                if(today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate){
                    rectangle.setStroke(Color.BLUE);
                }

                calendar.getChildren().add(stackPane);
            }
        }else {
            for (int j = 0; j < 7; j++) {
                VBox vBox = new VBox();
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);


                if (auxDate.getDayOfWeek().getValue() == (j + 1) && auxDate.getMonth() == dateFocus.getMonth()) {
                    Text date = new Text(String.valueOf(auxDate.getDayOfMonth()));
                    double textTranslationY = -(rectangleHeight / 2) * 0.75;
                    date.setTranslateY(textTranslationY);
                    stackPane.getChildren().add(date);


                }


                if(today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate){
                    rectangle.setStroke(Color.BLUE);
                }


                calendar.getChildren().add(stackPane);


                auxDate = auxDate.plusDays(1);
            }

        }
    }


    private void handleRectangleClick(MouseEvent event) {
        System.out.println("Rettangolo cliccato!");
        // Aggiungi qui le azioni che desideri eseguire al cliccare del rettangolo
    }

    private StackPane createClickableRectangle(String labelText, Color color) {
        Rectangle rectangle = new Rectangle(70, 50, color);
        rectangle.setOnMouseClicked(this::handleRectangleClick);
        Text text = new Text(labelText);
        StackPane rectangleWithText = new StackPane(rectangle, text);
        return rectangleWithText;
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
           Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CalendarCitizenScene.fxml")));
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
