package com.example.lez5;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;
import java.time.format.DateTimeFormatter;

public class ControllerRegisterScene {
    public static boolean isCodiceFiscaleValid(String codiceFiscale, String nome, String cognome, LocalDate dataDiNascita) {
        // Verifica della lunghezza del codice fiscale
        if (codiceFiscale.length() != 16) {
            return false;
        }

        // Estrazione delle parti dal codice fiscale
        String parteCognome = codiceFiscale.substring(0, 3);
        String parteNome = codiceFiscale.substring(3, 6);
        String dataDiNascitaCodice = codiceFiscale.substring(6, 12);

        // Verifica delle corrispondenze con nome, cognome e data di nascita
        if (!parteCognome.equals(generaParteNomeCognome(cognome)) ||
                !parteNome.equals(generaParteNomeCognome(nome)) ||
                !dataDiNascitaCodice.equals(generaDataDiNascitaCodice(dataDiNascita))) {
            return false;
        }

        // Aggiungi ulteriori verifiche, ad esempio i caratteri di controllo

        return true;
    }

    private static String generaParteNomeCognome(String input) {
        String result = input.toUpperCase().replaceAll("[^A-Z]", "");

        if (result.length() >= 3) {
            return result.substring(0, 3);
        } else {
            return result + "XXX".substring(0, 3 - result.length());
        }
    }

    private static String generaDataDiNascitaCodice(LocalDate dataDiNascita) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        return dataDiNascita.format(formatter);
    }
    private Stage stage;
    private Scene scene;
    @FXML
    private TextField place_of_birth;

    @FXML
    private TextField cat;

    @FXML
    private TextField num_health_card;

    @FXML
    private DatePicker date_of_birth;


    @FXML
    private Label birthdayLabel;

    @FXML
    private Label birthplaceLabel;

    @FXML
    private Label codfisLabel;

    @FXML
    private TextField tax_code;

    @FXML
    private TextField confirmPassword;

    @FXML
    private TextField email;

    @FXML
    private Label error;

    @FXML
    private Hyperlink loginHL;

    @FXML
    private TextField name;

    @FXML
    private Label nameSinginLabel;

    @FXML
    private TextField password;

    @FXML
    private Button registerButton;

    @FXML
    private Label singinLabel;

    @FXML
    private TextField surname;

    @FXML
    private Label surnameSinginLabel;

    @FXML
    private Button undoButton;

    //-------------------------------------------------------------------

    @FXML
    void backToFirstScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FirstScene.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void EnterLoginScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LoginScene.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void registration(ActionEvent event) throws IOException, SQLException {

        String aus1 = name.getText();
        String aus2 = password.getText();
        String aus3 = email.getText();
        String aus4 = num_health_card.getText();
        LocalDate aus5 = date_of_birth.getValue();
        String aus6 = cat.getText();
        String aus7 = tax_code.getText();
        String aus8 = place_of_birth.getText();
        String aus9 = surname.getText();
        //TODO controllare consistenza dei dati
        //TODO creare metodo di scrittura utenti su db in model
        boolean check = false;

        if(date_of_birth.getValue() != null){
            User newUser = new User(name.getText(),num_health_card.getText() ,cat.getText(), surname.getText(), date_of_birth.getValue().toString(), place_of_birth.getText(), tax_code.getText(), email.getText(), password.getText());
            if(checkEmptyFields(newUser) == true && checkPasswordConfirm() == true )
                check = true;
            // checkDataConsistence(newUser); //TODO
        }else
             error.setText("Field empty");
        if(isCodiceFiscaleValid(aus7 ,name.getText(),surname.getText(), aus5)){
            check = true;
        }else{
            error.setText("Invalid tax code");
        }






        if(check == true){

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

                //ResultSet resultSet = statement.executeQuery("INSERT INTO `citizen` (`id`, `name`, `surname`, `tax_code`, `num_health_card`, `place_of_birth`, `date_of_birth`, `cat`, `email`, `password`) " +
                 //       "VALUES (NULL, name, surname, '1234567890', '1234567890', 'New York', '1975-12-15', 'cittadino con passaporto diplomatico', email, password);");


                //System.out.println(resultSet.getString(2));
            }catch (SQLException e) {
                System.out.println(e);
            }


            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScene.fxml")));
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            // stage.initStyle(StageStyle.TRANSPARENT); // Rimuovi i bordi della finestra
            double screenWidth = Screen.getPrimary().getBounds().getWidth();
            double screenHeight = Screen.getPrimary().getBounds().getHeight();
            double windowWidth = screenWidth + 30; // Larghezza della finestra
            double windowHeight = screenHeight + 1; // Altezza della finestra
            stage.setX((screenWidth - windowWidth) / 2);
            stage.setY((screenHeight - windowHeight) / 2);            stage.setScene(scene);
            stage.show();
        }



    }

    public boolean checkEmptyFields(User user){
        String name = user.getName();
        String surname = user.getSurname();
        String birthday = user.getBirthday();
        String birthPlace = user.getBirthPlace();
        String codiceFiscale = user.getCodiceFiscale();
        String email = user.getEmail();
        String password = user.getPassword();

        if(user.getName().isEmpty() || user.getSurname().isEmpty() || user.getBirthday().isEmpty() || user.getBirthPlace().isEmpty() || user.getCodiceFiscale().isEmpty() || user.getEmail().isEmpty() || confirmPassword.getText().isEmpty()){
            error.setText("there is a empty fields");



            return false;
        }


        return true;

    }
    public boolean checkPasswordConfirm(){
        if(!password.getText().equals(confirmPassword.getText())){
            error.setText("Password not equal");
            return false;
        }
        return true;
    }

    public boolean checkDataConsistence(User user){
        //TODO
        return true;
    }
}

