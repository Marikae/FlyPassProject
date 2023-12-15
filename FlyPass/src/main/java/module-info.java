module com.example.lez5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.lez5 to javafx.fxml;
    exports com.example.lez5;
}