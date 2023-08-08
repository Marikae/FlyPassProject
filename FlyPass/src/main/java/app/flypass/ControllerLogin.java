package app.flypass;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ControllerLogin extends Controller {

    @FXML
    private Label loginLabel;

    @FXML
    private Label passwordLoginLabel;

    @FXML
    private TextField passwordLoginTF;

    @FXML
    private Button undoButton;

    @FXML
    private Label usernameLoginLabel;

    @FXML
    private TextField usernameLoginTF;

    @FXML
    void backToFirstScene(ActionEvent event) {

    }
}
