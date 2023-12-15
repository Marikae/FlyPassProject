module com.example.lez5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.zaxxer.hikari;

    opens com.example.lez5 to javafx.fxml;
    exports com.example.lez5;
}