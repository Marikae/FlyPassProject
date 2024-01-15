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
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.Objects;
import java.util.ResourceBundle;


import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;
public class ControllerCalendarScene extends Controller implements Initializable {

    @FXML
    private Label serviceType;
    @FXML
    private Button undo;
    private Stage stage;
    private Scene scene;
    private String service;

    public ControllerCalendarScene(){
        super();
    }



    ZonedDateTime dateFocus;
    ZonedDateTime today;

    ZonedDateTime auxDate;

    ZonedDateTime auxDate2 = dateFocus;

    int currentDate;

    int numberOfWeek;

    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    private FlowPane calendar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        numberOfWeek = today.getDayOfMonth() / 5;
        drawCalendar();
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
        calendar.getChildren().clear();
        drawCalendar();
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
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);
                LocalTime localTime = LocalTime.of(8, 0 , 0);



                if (auxDate.getDayOfWeek().getValue() == (j + 1)) {
                    int i = 0;
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
                                Text text = new Text(time + "\nNon prenotabile\nClicca per essere\navvisato");
                                calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                                calendarActivityBox.setMaxHeight(rectangleHeight * 0.05);
                                calendarActivityBox.setStyle("-fx-background-color:RED");
                                calendarActivityBox.getChildren().add(text);

                                calendarActivityBox.setOnMouseClicked(event -> {
                                        System.out.println("DIO LEBBRA");
                                });
                            }else if((resultSet.getBoolean("Disponibile") && !resultSet.getBoolean("Prenotato"))){
                                String endTime = resultSet.getTime(2).toString();
                                Text text = new Text(time + "\nClicca qui\nPer prenotare!");
                                calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                                calendarActivityBox.setMaxHeight(rectangleHeight * 0.05);
                                calendarActivityBox.setStyle("-fx-background-color:LIGHTGREEN");
                                calendarActivityBox.getChildren().add(text);

                                LocalTime finalLocalTime = localTime;
                                calendarActivityBox.setOnMouseClicked(event -> {

                                    try {

                                        String query1 = "UPDATE eventi SET Prenotato = 1 WHERE Data = ? AND Inizio = ? AND Sede = ? AND TipoServizio = ?";
                                        Connection connection1 = DatabaseConnection.databaseConnection();
                                        Statement statement1 = connection1.createStatement();

                                        PreparedStatement preparedStatement1 = connection1.prepareStatement(query1);
                                        preparedStatement1.setDate(1, Date.valueOf(auxDate.toLocalDate()));
                                        preparedStatement1.setObject(2, finalLocalTime);
                                        preparedStatement1.setString(3, model.evento.sede.name());
                                        preparedStatement1.setString(4, model.getService().getName());

                                        preparedStatement1.executeUpdate();


                                        connection1.close();
                                        statement1.close();
                                        preparedStatement1.close();

                                        calendar.getChildren().clear();
                                        drawCalendar();
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }

                                });
                            }else if((resultSet.getBoolean("Disponibile") && resultSet.getBoolean("Prenotato"))){
                                String endTime = resultSet.getTime(2).toString();
                                Text text = new Text(time + "\nSlot già\nPrenotato!");
                                calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
                                calendarActivityBox.setMaxHeight(rectangleHeight * 0.05);
                                calendarActivityBox.setStyle("-fx-background-color:LIGHTBLUE");
                                calendarActivityBox.getChildren().add(text);

                                calendarActivityBox.setOnMouseClicked(event -> {
                                    //TODO
                                });
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

}
