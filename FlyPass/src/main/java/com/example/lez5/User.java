package com.example.lez5;

public abstract class User {
    private String name;
    private String surname;
    private String email;

    private String password;

    public User(String name, String surname, String email, String password){
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }


    public String getName(){
        return name;
    }
    public String getSurname(){
        return surname;
    }
    public String getEmail(){
        return email;
    }
}