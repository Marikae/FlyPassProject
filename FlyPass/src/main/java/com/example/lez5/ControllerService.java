package com.example.lez5;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ControllerService extends Controller implements Initializable {
    @FXML
    private Label description;
    private Stage stage;
    private Scene scene;
    @FXML
    private ImageView img;
    @FXML
    private Label name;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public ControllerService(){
        super();
    }

    public void setData(Service service){
        Image image = new Image(getClass().getResourceAsStream(service.getImgSrc()));
        //this.myListener = myListener;
        img.setImage(image);
        name.setText(service.getName());
        description.setText(service.getDescription());
    }
    @FXML
    void printfMsg(MouseEvent event) {
    }
    public String getServiceName(){
        return name.getText();
    }

    @FXML
    void click(MouseEvent event) throws IOException {
        model.setService(getServiceName());
        if(!model.isWorker()) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CalendarCitizenScene.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }else{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("newCalendarScene.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }



}
