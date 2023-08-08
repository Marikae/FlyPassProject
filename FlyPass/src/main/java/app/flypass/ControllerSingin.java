package app.flypass;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ControllerSingin {
    @FXML
    private Label birthdayLabel;

    @FXML
    private DatePicker birthdaySinginDP;

    @FXML
    private Label birthplaceLabel;

    @FXML
    private TextField birthplaceSinginTF;

    @FXML
    private Label codfisLabel;

    @FXML
    private TextField codfisSinginTF;

    @FXML
    private Label nameSinginLabel;

    @FXML
    private TextField nameSinginTF;

    @FXML
    private Button registerButton;

    @FXML
    private Label singinLabel;

    @FXML
    private Label surnameSinginLabel;

    @FXML
    private TextField surnameSinginTF;

    @FXML
    private Button undoButton;

    @FXML
    void backToFirstScene(ActionEvent event) {

    }
}
