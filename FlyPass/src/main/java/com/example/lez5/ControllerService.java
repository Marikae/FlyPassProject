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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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
        Rectangle clip = new Rectangle();
        double arcSize = 20; // Imposta la dimensione dell'arrotondamento degli angoli
        clip.setArcWidth(arcSize);
        clip.setArcHeight(arcSize);

        // Imposta le dimensioni del rettangolo come quelle dell'immagine
        clip.widthProperty().bind(img.fitWidthProperty());
        clip.heightProperty().bind(img.fitHeightProperty());

        // Applica la maschera di arrotondamento all'ImageView
        img.setClip(clip);
        //Image image = new Image(getClass().getResourceAsStream(service.getImgSrc()));
        //ImageView imageView = new ImageView(image);
        img.setPreserveRatio(false); // Imposta il rapporto d'aspetto su false
        //imageView.fitWidthProperty().bind(containerPane.widthProperty()); // Fai in modo che l'immagine si adatti alla larghezza del contenitore
        //imageView.fitHeightProperty().bind(containerPane.heightProperty()); // Fai in modo che l'immagine si adatti all'altezza del contenitore
        img.setSmooth(true); // Opzionale: rende l'immagine più nitida
        img.setCache(true); // Opzionale: abilita la cache per migliorare le prestazioni

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
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("SelectOfficeScene.fxml")));
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
