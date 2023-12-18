package com.example.lez5;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.*;

public class DatabaseConnection {

    /*public Connection databaseConnection(){
        String url = "jdbc:mysql://localhost:3306/passport";
        String username = "root";
        String databasePassword = "";

        //DATABASE
        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url,username,databasePassword);
            return connection;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }s


    }*/
    static private HikariDataSource ds = null;

    static private String url = "jdbc:mysql://localhost:3306/flypass";

    public static Connection databaseConnection(){

        if (ds == null) {
            ds = new HikariDataSource();
            ds.setJdbcUrl(url);
            ds.setUsername("root");
            ds.setPassword("");
        }
        try{
            return ds.getConnection();
        }catch (SQLException e){
            e.printStackTrace();
            return  null;
        }
    }

}