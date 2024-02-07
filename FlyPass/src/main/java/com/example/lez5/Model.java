package com.example.lez5;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
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

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }

    //Serve per capire se l'utente sta provando a prenotare o ad inserire disponibilità in un "ritiro passaporto"
    private boolean prenotaPassaportoClicked;

    public boolean isPrenotaPassaportoClicked() {
        return prenotaPassaportoClicked;
    }

    public void setPrenotaPassaportoClicked(boolean prenotaPassaportoClicked) {
        this.prenotaPassaportoClicked = prenotaPassaportoClicked;
    }

    private User user;
    private Service service;
    @FXML
    private ImageView prenotationImg;
    private boolean worker;
    public int idUtente;
    public boolean passaportoPrenotato;

    public boolean ritiroPrenotato;
    public boolean notification = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
    public static Model getModel() { // metodo aperto, invocazione tramite codice
        if (modelInstance == null) { // solamente quando non esiste alcuna istanza, ne crea una nuova
            modelInstance = new Model();
        }
        return modelInstance;
    }

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


        List<Service> ls = new ArrayList<Service>();
        //Rilascio prima volta
        Service service = new Service();
        service.setName("Rilascio prima volta");
        service.setDescription("Rilascio del passaporto per la prima volta.");
        service.setImgSrc("/img/firstTime.jpg");
        ls.add(service);

        //Rilascio scadenza
        service = new Service();
        service.setName( "Rinnovo per scadenza");
        service.setDescription("Rilascio del passaporto per scadenza.");
        service.setImgSrc("/img/scadenza.jpg");
        ls.add(service);

        //Furto o smarrimento
        service = new Service();
        service.setName("Furto o smarrimento");
        service.setDescription("Rilascio del passaporto per furto o smarrimento.");
        service.setImgSrc("/img/furto.jpg");
        ls.add(service);

        //Rilascio detoriamento
        service = new Service();
        service.setName("Rinnovo per detoriamento");
        service.setDescription("Rilascio del passaporto per detoriamento.");
        service.setImgSrc("/img/detoriamento.jpg");
        ls.add(service);

        //passaporto urgente
        service = new Service();
        service.setName("Passaporto urgente");
        service.setDescription("Rilascio del passaporto urgentemente.");
        service.setImgSrc("/img/urgente.jpg");
        ls.add(service);

        //prolungamento validità passsaporto
        service = new Service();
        service.setName("Prolungamento validità passaporto");
        service.setDescription("prolungamento della validità del passaporto.");
        service.setImgSrc("/img/scadenza.jpg");
        ls.add(service);

        //cambio info personali
        service = new Service();
        service.setName("Cambio info personali");
        service.setDescription("Iter per il cambio delle informazioni personali.");
        service.setImgSrc("/img/cambio.jpg");
        ls.add(service);

        //passaporto per minori
        service = new Service();
        service.setName("Passaporto per minori");
        service.setDescription("Rilascio passaporto per minori.");
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
                idUtente = resultSet.getInt("id");

                connection.close();
                preparedStatement.close();
                resultSet.close();
                return true;
            } else {
                connection.close();
                preparedStatement.close();
                resultSet.close();
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
                ritiroPrenotato = resultSet.getBoolean("PrenotazioneRitiro");
                connection.close();
                preparedStatement.close();
                resultSet.close();
                return true;
            } else {
                connection.close();
                preparedStatement.close();
                resultSet.close();
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
    public boolean checkAnagrafica(LocalDate aus5,String aus1, String aus2, String aus3, String aus4,
                                String aus6, String aus7, String aus8, String aus9){
        try {
            Connection connection = DatabaseConnection.databaseConnection();
            Statement statement = connection.createStatement();

            String query = ("SELECT * FROM anagrafica " +
                    "WHERE name = ? " +
                    "AND surname = ? " +
                    "AND date_of_birth = ? " +
                    "AND place_of_birth = ? " +
                    "AND num_health_card = ?" +
                    "AND tax_code = ?");

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, aus1);
            preparedStatement.setString(2, aus9);
            preparedStatement.setObject(3, aus5);// Considera che date_of_birth è un LocalDate
            preparedStatement.setString(4, aus8);
            preparedStatement.setString(5, aus4);
            preparedStatement.setString(6, aus7);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                connection.close();
                preparedStatement.close();
                resultSet.close();
                return false;
            }
            connection.close();
            preparedStatement.close();
            resultSet.close();
        }catch (SQLException e) {
            System.out.println(e);
        }

        return true;
    }

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

            connection.close();
            preparedStatement.close();
        }catch (SQLException e) {
            System.out.println(e);
        }
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
        connection.close();
        preparedStatement.close();
        resultSet.close();
    }
    public boolean hasPrenotation() throws SQLException {
        Connection connection = DatabaseConnection.databaseConnection();
        String query = "SELECT PassaportoPrenotato FROM citizen WHERE email = ? AND PassaportoPrenotato = 1";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        // Imposta i valori dei parametri per email
        preparedStatement.setString(1, user.getEmail());

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            connection.close();
            preparedStatement.close();
            resultSet.close();
            return true;
        }
        connection.close();
        preparedStatement.close();
        resultSet.close();
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

        connection.close();
        preparedStatement.close();
        resultSet.close();
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
           Statement statement = connection.createStatement();
           PreparedStatement preparedStatement = connection.prepareStatement(query);
           preparedStatement.setString(1, String.valueOf(idUtente) );
           ResultSet resultSet = preparedStatement.executeQuery();
           if (resultSet.next()) {
                prenotation = "Prenotazione prenotata per: \n " + preparedStatement.getResultSet().getString("Data") + "\n Orario: " + preparedStatement.getResultSet().getString("Inizio") + "\n Sede: " + preparedStatement.getResultSet().getString("Sede");
           }else{
               prenotation = "Prenotazione ancora da effettuare!\n";
           }

           connection.close();
           preparedStatement.close();
           statement.close();
           resultSet.close();

       }catch (SQLException e) {
        System.out.println(e);
        }
        return prenotation;
    }

    public String getCitizenRitiro() throws SQLException { //ritorna la prenotazione del ritiro passaporto se c'è
        String prenotation = "";
        try {
            Connection connection = DatabaseConnection.databaseConnection();
            String query = ("SELECT Data, Inizio, Fine, Sede FROM eventi WHERE Id_utente_prenotazione = ? AND tipoServizio = ?");
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(idUtente) );
            preparedStatement.setString(2, "Prenotazione ritiro passaporto");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                prenotation = "Prenotazione ritiro passaporto prenotata per: \n " + preparedStatement.getResultSet().getString("Data") + "\n Orario: " + preparedStatement.getResultSet().getString("Inizio") + "\n Sede: " + preparedStatement.getResultSet().getString("Sede");
            }else{
                prenotation = "Prenotazione per il ritiro passaporto\n ancora da effettuare!\n";
            }

            connection.close();
            preparedStatement.close();
            statement.close();
            resultSet.close();

        }catch (SQLException e) {
            System.out.println(e);
        }
        return prenotation;
    }


    /*public void notification() throws SQLException {
        //se ci sono notifiche non viste allora appare il pallino verde
        //altrimenti niente pallino verde ma le notifiche vengono scritte comunque
        if(notificationSeen() == false){
            activeNotification();
        }else{
            setNotificationSeen(); //setta la notifica a "vista"
            disativateNotification();
        }

    }*/
    public void disativateNotification(){
        this.notification = false;
    }
    public void activeNotification(){
        this.notification = true;
    }
    public void putNotification(ImageView image) { //Cambiare l'immagine di prenotazioni con l'immagine con notifica
        Image initialImage = new Image(getClass().getResource("/icon/bookingNotification.png").toExternalForm());
        image.setImage(initialImage);
    }

    public String getIdCitizen() throws SQLException { //ritorna l'id del cittadino
        Connection connection2 = DatabaseConnection.databaseConnection();
        String query = "SELECT id FROM citizen WHERE email = ?";
        Statement statement1 = connection2.createStatement();
        PreparedStatement preparedStatement = connection2.prepareStatement(query);
        preparedStatement.setString(1, user.getEmail());

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            String var = resultSet.getString(1);
            connection2.close();
            preparedStatement.close();
            statement1.close();
            resultSet.close();
            return var;
        }
        connection2.close();
        preparedStatement.close();
        statement1.close();
        resultSet.close();
        return null;
    }
    public void setNotificationSeen(){ //setta la notifica a già vista
        try {
            Connection connection1 = DatabaseConnection.databaseConnection();
            String query1 = ("UPDATE notification SET seen = 1 WHERE utente_id = ?");
            Statement statement1 = connection1.createStatement();
            PreparedStatement preparedStatement1 = connection1.prepareStatement(query1);
            preparedStatement1.setString(1, getIdCitizen());
            preparedStatement1.executeUpdate();

            connection1.close();
            statement1.close();
            preparedStatement1.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        disativateNotification();
    }
    public boolean notificationSeen() throws SQLException { //controlla se ci sono notifiche da vedere
        Connection connection = DatabaseConnection.databaseConnection();
        String query = "SELECT * FROM notification WHERE utente_id = ? AND stato = 'definito' AND seen = 0";
        Statement statement = connection.createStatement();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, getIdCitizen());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {//ci sono notifiche da vedere quindi ritorna false
            connection.close();
            statement.close();
            preparedStatement.close();

            return false;
        } else { //ritorna true se non ci sono notifiche da vedere
            connection.close();
            statement.close();
            preparedStatement.close();

            return true;
        }
    }

    public String getNewNotification() throws SQLException { //ritorna le nuove prenotazioni ancora da vedere
        Connection connection = DatabaseConnection.databaseConnection();
        String query = "SELECT * FROM notification WHERE utente_id = ? AND stato = 'definito' AND seen = 0";
        Statement statement = connection.createStatement();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, getIdCitizen());
        ResultSet resultSet = preparedStatement.executeQuery();
        StringBuilder resultString = new StringBuilder();
        //String removeStrg = "Prenota per:\n";

        while (resultSet.next()) {
            String sede = resultSet.getString("sede");
            String data = resultSet.getString("data");
            String ora = resultSet.getString("ora");
            String tipo = resultSet.getString("tipo");
            //String tipo = tipoOriginal.substring(removeStrg.length());
            String notificationString = String.format("Nuova disponibilità per %s nella sede di %s, il giorno %s, alle ore %s\n",tipo, sede, data, ora);
            resultString.append(notificationString);
        }
        if (resultString.isEmpty()) {
            connection.close();
            statement.close();
            preparedStatement.close();
            return "Nessuna nuova notifica";
        } else {
            //notification = true;
            connection.close();
            statement.close();
            preparedStatement.close();
            return resultString.toString();
        }
    }
    public String getOldNotification() throws SQLException { //ritorna le vecchie prenotazioni già viste
        Connection connection = DatabaseConnection.databaseConnection();
        String query = "SELECT * FROM notification WHERE utente_id = ? AND stato = 'definito' AND seen = 1";
        Statement statement = connection.createStatement();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, getIdCitizen());
        ResultSet resultSet = preparedStatement.executeQuery();
        StringBuilder resultString = new StringBuilder();
       // String removeStrg = "Prenota per:\n";
        while (resultSet.next()) {
            String sede = resultSet.getString("sede");
            String data = resultSet.getString("data");
            String ora = resultSet.getString("ora");
            String tipo = resultSet.getString("tipo");
            //String tipo = tipoOriginal.substring(removeStrg.length());
            String notificationString = String.format("Disponibilità per %s nella sede di %s, il giorno %s, alle ore %s\n",tipo, sede, data, ora);
            resultString.append(notificationString);
        }
        if (resultString.isEmpty()) {
            connection.close();
            statement.close();
            preparedStatement.close();
            return "";
        } else {
            connection.close();
            statement.close();
            preparedStatement.close();
            return resultString.toString();
        }
    }

    public void updateNotification(Date date, Time time){
        try {
            String query1 = ("UPDATE notification SET stato = 'definito' WHERE data = ? AND ora = ? AND tipo = ? AND sede = ?");
            Connection connection1 = DatabaseConnection.databaseConnection();
            Statement statement1 = connection1.createStatement();
            PreparedStatement preparedStatement1 = connection1.prepareStatement(query1);
            preparedStatement1.setDate(1, date);
            preparedStatement1.setObject(2, time);
            preparedStatement1.setString(3, getService().getName());
            preparedStatement1.setString(4, evento.sede.name());
            preparedStatement1.executeUpdate();

            connection1.close();
            statement1.close();
            preparedStatement1.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getBookedReservation() throws SQLException {
        Connection connection = DatabaseConnection.databaseConnection();
        String query = "SELECT * FROM eventi WHERE Worker = ? AND Prenotato = 1";
        Statement statement = connection.createStatement();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, getLoginUserName());
        ResultSet resultSet = preparedStatement.executeQuery();
        StringBuilder resultString = new StringBuilder();
        while (resultSet.next()) {
            String office = resultSet.getString("Sede");
            String date = resultSet.getString("Data");
            String hour = resultSet.getString("Inizio");
            String type = resultSet.getString("TipoServizio");
            String citizen = getCitizenWhoBooked(resultSet.getString("Id_utente_prenotazione"));

            String notificationString = String.format("Prenotazione per %s, sede di %s, il giorno %s, alle ore %s effettuata da %s", type, office, date, hour, citizen);
            resultString.append(notificationString);
        }
        if (resultString.isEmpty()) {
            connection.close();
            statement.close();
            preparedStatement.close();
            return "Lista prenotazioni vuota.";
        } else {
            connection.close();
            statement.close();
            preparedStatement.close();
            return resultString.toString();
        }
    }

    public String getCitizenWhoBooked(String idCode) throws SQLException {
        Connection connection2 = DatabaseConnection.databaseConnection();
        String query2 = "SELECT * FROM citizen WHERE id = ?";
        Statement statement2 = connection2.createStatement();
        PreparedStatement preparedStatement2 = connection2.prepareStatement(query2);
        preparedStatement2.setString(1, idCode);
        ResultSet resultSet2 = preparedStatement2.executeQuery();

        StringBuilder resultString = new StringBuilder();
        String citizenWhoBooked = "";
        while (resultSet2.next()) {
            String name = resultSet2.getString("name");
            String surname = resultSet2.getString("surname");
            String taxCode = resultSet2.getString("tax_code");

            citizenWhoBooked = String.format("%s %s cod. fisc. %s\n", name, surname, taxCode);
            resultString.append(citizenWhoBooked);
        }
        connection2.close();
        statement2.close();
        preparedStatement2.close();

        return citizenWhoBooked;
    }
    /*public boolean notificationAlredyExist(Date dataIn, Time hourIn, String office) throws SQLException {
        Connection connection = DatabaseConnection.databaseConnection();
        String query = "SELECT * FROM notification WHERE utente_id = ? AND data = dataIn AND ora = hourIn AND tipo = ? AND sede = office AND stato = 'definito'";
        Statement statement = connection.createStatement();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, getIdCitizen());
        preparedStatement.setString(2, getService().getName());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {//ci sono notifiche già inserite torna true
            connection.close();
            statement.close();
            preparedStatement.close();

            return true;
        } else { //ritorna false se non esistono già dei record
            connection.close();
            statement.close();
            preparedStatement.close();

            return false;
        }
    }*/
}
