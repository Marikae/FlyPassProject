package com.example.lez5;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.Objects;

public class Service {
    private String name;
    private String imgSrc;
    private String description;
    public String getName(){
        return name;
    }
    public String getImgSrc(){
        return imgSrc;
    }
    public String getDescription(){
        return description;
    }
    public void setName(String newName){
        this.name = newName;
    }
    public void setImgSrc(String newImgSrc){
        this.imgSrc = newImgSrc;
    }
    public void setDescription(String newDescription){
        this.description = newDescription;
    }
}
