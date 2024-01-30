package com.example.lez5;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.sql.*;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Model implements Initializable {
    Evento evento = new Evento();
    private static Model modelInstance; // statico e protetto da accesso esterno
    private String loginPassword;
    private String loginUserName;
    private User user;
    private Service service;
    @FXML
    private ImageView prenotationImg;
    private boolean worker;
    public int idUtente;
    public boolean passaportoPrenotato;
    public boolean notification = true;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
    public static Model getModel() { // metodo aperto, invocazione tramite codice
        if (modelInstance == null) { // solamente quando non esiste alcuna istanza, ne crea una nuova
            modelInstance = new Model();
        }
        return modelInstance;
    }
  /*  public void closeResource(Connection connection, PreparedStatement preparedStatement ) throws SQLException {
        connection.close();
        preparedStatement.close();
    }*/


    //----------------------------- SERVICE -----------------------------------
    public void setService(String nameNewService){
        for (Service s : getServices() ) {
            if(s.getName().equals(nameNewService))
                this.service = s;
        }
    }
    public Service getService(){
        return service;
    }
    public List<Service> getServices(){
        String pre = "";
        if(isWorker()){
            pre = "Aggiungi prenotazioni per: \n";
        }else{
            pre = "Prenota per:\n";
        }

        List<Service> ls = new ArrayList<Service>();
        //Rilascio prima volta
        Service service = new Service();
        service.setName(pre + "Rilascio prima volta");
        service.setDescription("Rilascio del passaporto per la prima volta");
        service.setImgSrc("/img/firstTime.jpg");
        ls.add(service);

        //Rilascio scadenza
        service = new Service();
        service.setName(pre + "Rinnovo per scadenza");
        service.setDescription("Rilascio del passaporto per scadenza");
        service.setImgSrc("/img/scadenza.jpg");
        ls.add(service);

        //Furto o smarrimento
        service = new Service();
        service.setName(pre + "Furto o smarrimento");
        service.setDescription("Rilascio del passaporto per furto o smarrimento");
        service.setImgSrc("/img/furto.jpg");
        ls.add(service);

        //Rilascio detoriamento
        service = new Service();
        service.setName(pre + "Rinnovo per detoriamento");
        service.setDescription("Rilascio del passaporto per detoriamento");
        service.setImgSrc("/img/detoriamento.jpg");
        ls.add(service);

        //passaporto urgente
        service = new Service();
        service.setName(pre + "Passaporto urgente");
        service.setDescription("Rilascio del passaporto urgentemente");
        service.setImgSrc("/img/urgente.jpg");
        ls.add(service);

        //prolungamento validità passsaporto
        service = new Service();
        service.setName(pre + "Prolungamento validità passaporto");
        service.setDescription("prolungamento della validità del passaporto");
        service.setImgSrc("/img/scadenza.jpg");
        ls.add(service);

        //cambio info personali
        service = new Service();
        service.setName(pre + "Cambio info personali");
        service.setDescription("Iter per il cambio delle informazioni personali");
        service.setImgSrc("/img/cambio.jpg");
        ls.add(service);

        //passaporto per minori
        service = new Service();
        service.setName(pre + "Passaporto per minori");
        service.setDescription("Rilascio passaporto per minori");
        service.setImgSrc("/img/minori.jpg");
        ls.add(service);

        return ls;
    }

    //------------------------------ LOGIN ------------------------------------
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
                setWorker(loginUserName);
                evento.setSede(((Worker) user).getOffice());
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

                setCitizen(loginUserName);

                idUtente = resultSet.getInt("id");
                passaportoPrenotato = resultSet.getBoolean("PassaportoPrenotato");
                return true;
            } else {
                return false;
            }
            // Chiudi le risorse
        } catch (SQLException e) {
            System.out.println(e);
        }
        // Chiudi le risorse

        return false;
    }

    //----------------------------------------REGISTER----------------------------------------------------
    public boolean HealthCardNumberCheck(String heathCardNumber) {
        // Verifica se la lunghezza della stringa è esattamente 20
        if (heathCardNumber.length() != 20) {
            return false;
        }
        // Verifica se ogni carattere della stringa è una cifra da '0' a '9'
        for (char carattere : heathCardNumber.toCharArray()) {
            if (carattere < '0' || carattere > '9') {
                return false; // La stringa contiene un carattere non valido
            }
        }
        // La stringa è valida se ha lunghezza 20 e contiene solo cifre da '0' a '9'
        return true;
    }

    public boolean checkEmail(String email) {
        String regexPattern = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isCodiceFiscaleValid(String codiceFiscale, String nome, String cognome) {
        // Verifica della lunghezza del codice fiscale
        if (codiceFiscale.length() != 16) {
            return false;
        }
        // Estrazione delle parti dal codice fiscale
        String parteNomeCognome = codiceFiscale.substring(0, 6);
        if(isSoloMaiuscole(parteNomeCognome))
            return  true;
        // Aggiungi ulteriori verifiche, ad esempio i caratteri di controllo
        return  false;
    }

    public boolean isSoloMaiuscole(String input) {
        return input.matches("^[A-Z]+$");
    }

    //------------------------------------- DATABESE INSERIMENTO ---------------------------------
    public void databaseInsertion (LocalDate aus5,String aus1, String aus2, String aus3, String aus4,
                                   String aus6, String aus7, String aus8, String aus9){
        try {
            Connection connection = DatabaseConnection.databaseConnection();
            Statement statement = connection.createStatement();

            String query = ("INSERT INTO `citizen` (`id`, `name`, `surname`, `tax_code`, `num_health_card`, `place_of_birth`, `date_of_birth`, `cat`, `email`, `password`) " +
                    "VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, aus1);
            preparedStatement.setString(2, aus9);
            preparedStatement.setString(3, aus7);
            preparedStatement.setString(4, aus4);
            preparedStatement.setString(5, aus8);
            preparedStatement.setObject(6, aus5);  // Considera che date_of_birth è un LocalDate
            preparedStatement.setString(7, aus6);
            preparedStatement.setString(8, aus3);
            preparedStatement.setString(9, aus2);
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e);
        }
        /*try{
            //TODO da settare l'id prima di far entrare il nuovo utente
        } catch (SQLException e) {
            System.out.println(e);
        }*/
    }

    //-----------------------USER DATA SET------------------------------
    public User getUser(){
        return this.user;
    }
    public void setWorker(String loginUserName)throws SQLException {
        Connection connection = DatabaseConnection.databaseConnection();
        String query = "SELECT * FROM worker WHERE email = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        // Imposta i valori dei parametri per email
        preparedStatement.setString(1, loginUserName);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Evento.Sede office = Evento.Sede.valueOf(resultSet.getString("office"));
            user = new Worker(resultSet.getString("name"), resultSet.getString("surname"),  resultSet.getString("email"), resultSet.getString("password"),  office);
        }
    }
    public boolean hasPrenotation() throws SQLException {
        Connection connection = DatabaseConnection.databaseConnection();
        String query = "SELECT PassaportoPrenotato FROM citizen WHERE email = ? AND PassaportoPrenotato = 1";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        // Imposta i valori dei parametri per email
        preparedStatement.setString(1, user.getEmail());

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return true;
        }
        return false;
    }
    private void setCitizen(String userEmail) throws SQLException {
        Connection connection = DatabaseConnection.databaseConnection();
        String query = "SELECT * FROM citizen WHERE email = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        // Imposta i valori dei parametri per email
        preparedStatement.setString(1, userEmail);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            user = new Citizen(resultSet.getString("name"), resultSet.getString("num_health_card"), resultSet.getString("cat"), resultSet.getString("surname"), resultSet.getString("date_of_birth"), resultSet.getString("place_of_birth"), resultSet.getString("tax_code"), resultSet.getString("email"), resultSet.getString("password"));
        }
        //User(String name, String num_health_card, String category, String surname,
        //String birthday, String birthPlace, String codiceFiscale, String email, String phone)
    }


    //------------------------------ControllerCitizenScene--------------------------------------------
    public void setPadovaAsSede(){
        evento.setSede(Evento.Sede.Padova);
    }

    public void setTrevisoAsSede(){
        evento.setSede(Evento.Sede.Treviso);
    }
    public void setVeneziaAsSede(){
        evento.setSede(Evento.Sede.Venezia);
    }
    public void setVeronaAsSede(){
        evento.setSede(Evento.Sede.Verona);
    }
    public boolean isWorker(){
        return worker;
    }

    public String getCitizenPrenotation() throws SQLException {
        String prenotation = "";
       try {
           Connection connection = DatabaseConnection.databaseConnection();
           String query = ("SELECT Data, Inizio, Fine, Sede FROM eventi WHERE Id_utente_prenotazione = ?");

           PreparedStatement preparedStatement = connection.prepareStatement(query);
           preparedStatement.setString(1, String.valueOf(idUtente) );
           ResultSet resultSet = preparedStatement.executeQuery();
           if (resultSet.next()) {
                prenotation = "data: " + preparedStatement.getResultSet().getString("Data") + "\n Orario: " + preparedStatement.getResultSet().getString("Inizio") + "\n Sede: " + preparedStatement.getResultSet().getString("Sede");
           }else{
               prenotation = "no prenotation found\n";
           }

       }catch (SQLException e) {
        System.out.println(e);
    }
        return prenotation;
    }


    public void notification(){

    }
    public void disativateNotification(){
        this.notification = false;
    }
    public void activeNotification(){
        this.notification = true;
    }
    public void putNotification(ImageView image) {
        // Cambiare l'immagine del bottone con l'immagine con notifica
        Image initialImage = new Image(getClass().getResource("/icon/bookingNotification.png").toExternalForm());
        image.setImage(initialImage);
    }

}
