package app.flypass;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class Model extends Application {

    private static Model modelInstance; // statico e protetto da accesso esterno

    private Model() {
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Model.class.getResource("FirstEnter.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args){
        launch(args);
    }
    //singelton
    public static Model getModel() { // metodo aperto, invocazione tramite codice
        if (modelInstance == null) { // solamente quando non esiste alcuna istanza, ne crea una nuova
            modelInstance = new Model();
        }
        return modelInstance;
    }
}

