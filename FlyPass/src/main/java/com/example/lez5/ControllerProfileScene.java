package com.example.lez5;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ControllerProfileScene extends Controller implements Initializable {
    public Button logOutM;
    private Stage stage;
    private Scene scene;
    @FXML
    private Label birthdayLabel;
    @FXML
    private Button prenotationButton;
    @FXML
    private Label emailLabel;
    @FXML
    private Label healtCardLabel;
    @FXML
    private Label surnameLabel;
    @FXML
    private Label taxCodeLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label placeOfBirthLabel;
    @FXML
    private Label categoryLabel;
    @FXML
    private Label finalCat;

    @FXML
    private Label finalCodFisc;

    @FXML
    private Label finalDateBirth;

    @FXML
    private Label finalHealCard;

    @FXML
    private Label finalPlaceBirth;
    @FXML
    private ImageView prenotationImg;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(model.isWorker()) { //lavoratore
            //labelSopraForm.setFont(Font.font(18));
            Worker user = (Worker) model.getUser();
            nameLabel.setText("    " + user.getName());
            surnameLabel.setText("    " + user.getSurname());
            emailLabel.setText("    " + user.getEmail());
            finalCodFisc.setText("Ufficio:      ");
            taxCodeLabel.setText(user.getOffice().toString());
            //nascondo le altre label
            placeOfBirthLabel.setVisible(false);
            birthdayLabel.setVisible(false);
            healtCardLabel.setVisible(false);
            categoryLabel.setVisible(false);
            finalCat.setVisible(false);
            finalHealCard.setVisible(false);
            finalDateBirth.setVisible(false);
            finalPlaceBirth.setVisible(false);
            //controllo della notifica

        }else{ //cittadino
            Citizen user = (Citizen) model.getUser();
            nameLabel.setText("    " + user.getName());
            surnameLabel.setText("    " + user.getSurname());
            taxCodeLabel.setText("    " + user.getCodiceFiscale());
            placeOfBirthLabel.setText("    " + user.getBirthPlace());
            emailLabel.setText("    " + user.getEmail());
            birthdayLabel.setText("    " + user.getBirthday());
            healtCardLabel.setText("    " + user.getHealCard());
            categoryLabel.setText("    " + user.getCategory());

            //controllo notifica
            if(model.notification){
                model.putNotification(prenotationImg);
            }
        }
    }
    public ControllerProfileScene(){
        super();
    }
    @FXML
    void goToMainScene(ActionEvent event) throws IOException {
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