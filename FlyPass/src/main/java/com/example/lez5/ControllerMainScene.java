package com.example.lez5;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;

import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ControllerMainScene implements Initializable {
    @FXML
    private Stage stage;
    private Scene scene;
    @FXML
    private GridPane serviceGrid;
    private List<Service> services;
    private Button logOutM;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        services = new ArrayList<>(getServices());
        int column = 0;
        int row = 1;
        try {
            for (Service service : services) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("service.fxml"));

                Pane pane = fxmlLoader.load();
                ControllerService controllerService = fxmlLoader.getController();
                controllerService.setData(service);

                if(column == 3){
                    column = 0;
                    ++row;
                }
                serviceGrid.add(pane, column++, row);
                GridPane.setMargin(pane, new Insets(20));
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private List<Service> getServices(){
        List<Service> ls = new ArrayList<Service>();
        //Rilascio prima volta
        Service service = new Service();
        service.setName("Rilascio prima volta");
        service.setDescription("Rilascio del passaporto per la prima volta");
        service.setImgSrc("/img/firstTime.jpg");
        ls.add(service);

        //Rilascio scadenza
        service = new Service();
        service.setName("Rinnovo per scadenza");
        service.setDescription("Rilascio del passaporto per scadenza");
        service.setImgSrc("/img/scadenza.jpg");
        ls.add(service);

        //Furto o smarrimento
        service = new Service();
        service.setName("Furto o smarrimento");
        service.setDescription("Rilascio del passaporto per furto o smarrimento");
        service.setImgSrc("/img/furto.jpg");
        ls.add(service);

        //Rilascio detoriamento
        service = new Service();
        service.setName("Rinnovo per detoriamento");
        service.setDescription("Rilascio del passaporto per detoriamento");
        service.setImgSrc("/img/detoriamento.jpg");
        ls.add(service);

        //passaporto urgente
        service = new Service();
        service.setName("Passaporto urgente");
        service.setDescription("Rilascio del passaporto urgentemente");
        service.setImgSrc("/img/urgente.jpg");
        ls.add(service);

        //prolungamento validità passsaporto
        service = new Service();
        service.setName("Prolungamento validità passaporto");
        service.setDescription("prolungamento della validità del passaporto");
        service.setImgSrc("/img/scadenza.jpg");
        ls.add(service);

        //cambio info personali
        service = new Service();
        service.setName("Cambio info personali");
        service.setDescription("Iter per il cambio delle informazioni personali");
        service.setImgSrc("/img/cambio.jpg");
        ls.add(service);

        //passaporto per minori
        service = new Service();
        service.setName("Passaporto per minori");
        service.setDescription("Rilascio passaporto per minori");
        service.setImgSrc("/img/minori.jpg");
        ls.add(service);



        return ls;
    }
    @FXML
    void goToLogOutScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LogOut.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}
