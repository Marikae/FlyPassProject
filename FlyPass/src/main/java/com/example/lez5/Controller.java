package com.example.lez5;
import static com.example.lez5.Model.getModel;

public abstract class Controller {
    Model model;
    public Controller(){
        model = getModel();

    }
}