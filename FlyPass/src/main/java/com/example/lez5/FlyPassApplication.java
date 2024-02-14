package com.example.lez5;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;


public class FlyPassApplication extends Application {
    private static final String APPLICATION_ICON = "/img/logo.png";
    public void start(Stage stage) throws IOException, SQLException {
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(FlyPassApplication.class.getResource("FirstScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);

        stage.getIcons().add(new Image(getClass().getResourceAsStream(APPLICATION_ICON)));

        stage.show();
    }

}
