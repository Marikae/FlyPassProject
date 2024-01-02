package com.example.lez5;

import javafx.fxml.Initializable;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Model implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    private static Model modelInstance; // statico e protetto da accesso esterno
    private String loginPassword;
    private String loginUserName;
    private User user;
    private String nameService;
    private boolean worker;

    private Model() {
    }
    public static Model getModel() { // metodo aperto, invocazione tramite codice
        if (modelInstance == null) { // solamente quando non esiste alcuna istanza, ne crea una nuova
            modelInstance = new Model();
        }
        return modelInstance;
    }
    public void closeResource(Connection connection, PreparedStatement preparedStatement ) throws SQLException {
        connection.close();
        preparedStatement.close();
    }
    //-----------------------------SERVICE DATA SET-----------------------------------
    public void setNameService(String nameService){
        this.nameService = nameService;
    }
    public String getNameService(){
        return  this.nameService;
    }

    //-------------------------LOGIN----------------------------------
    public Boolean workerLogin(String username, String password) throws IOException {
        try {
            this.loginPassword = password;
            this.loginUserName = username;
            Connection connection = DatabaseConnection.databaseConnection();
            String query = "SELECT * FROM worker WHERE email = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Imposta i valori dei parametri per email e password
            preparedStatement.setString(1, loginUserName);
            preparedStatement.setString(2, loginPassword);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Se esiste almeno una riga nel risultato, l'accesso è stato effettuato con successo
               // System.out.println("Accesso consentito per l'utente con email: " + resultSet.getString("email"));
                worker = true;
                return true;
            } else {
               return false;
            }

            // Chiudi le risorse

        } catch (SQLException e) {

            System.out.println(e);
            //return false;
        }
        return false;
    }
    public boolean citizenLogin(String username, String password){
        try {
            this.loginPassword = password;
            this.loginUserName = username;
            Connection connection = DatabaseConnection.databaseConnection();
            String query = "SELECT * FROM citizen WHERE email = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Imposta i valori dei parametri per email e password
            preparedStatement.setString(1, loginUserName);
            preparedStatement.setString(2, loginPassword);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Se esiste almeno una riga nel risultato, l'accesso è stato effettuato con successo
                // System.out.println("Accesso consentito per l'utente con email: " + resultSet.getString("email"));
                worker = false;
                return true;
            } else {
                return false;
            }

            // Chiudi le risorse

        } catch (SQLException e) {

            System.out.println(e);
            //return false;
        }
        // Chiudi le risorse

        return false;
    }

    //-----------------------USER DATA SET------------------------------
    public User getUser(){
        return this.user;
    }
    private void setUser(){
        //chiamata a database che ritorna tutti i dati dell'user e lo salva in una classe
        //user = new User(loginUserName...);
        //TODO
    }

}
