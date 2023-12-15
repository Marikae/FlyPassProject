package com.example.lez5;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;


public class FlyPassApplication extends Application {
    public void start(Stage stage) throws IOException {


        String url = "jdbc:mysql://localhost:3306/passport";
        String username = "root";
        String databasePassword = "";

        String sqlQuery = "SELECT *\n" +
                "FROM user\n" +
                "LIMIT 1;";


        //DATABASE
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url,username,databasePassword);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            resultSet.next();

            System.out.println(resultSet.getString(2));

            connection.close();
            statement.close();
            resultSet.close();


        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(FlyPassApplication.class.getResource("FirstScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {

        launch(args);
    }

}


//prova
