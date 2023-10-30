package com.example.lez5;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ControllerService {
    @FXML
    private Label description;

    @FXML
    private ImageView img;

    @FXML
    private Label name;
    public void setData(Service service){
        Image image = new Image(getClass().getResourceAsStream(service.getImgSrc()));
        img.setImage(image);
        name.setText(service.getName());
        description.setText(service.getDescription());
    }
    @FXML
    void printfMsg(MouseEvent event) {

    }

}
