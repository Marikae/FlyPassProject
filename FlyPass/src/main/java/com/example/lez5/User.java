package com.example.lez5;

public class User {
    public String name;
    public String surname;
    public String birthday;
    public String birthPlace;
    public String codiceFiscale;
    public String email;
    public String phone;
    public String password;

    public User(String name, String surname, String birthday, String birthPlace, String codiceFiscale, String email, String phone){
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.birthPlace = birthPlace;
        this.codiceFiscale = codiceFiscale;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public String getName(){
        return name;
    }

    public String getSurname(){
        return surname;
    }
    public String getBirthday(){
        return birthday;
    }
    public String getBirthPlace(){
        return birthPlace;
    }
    public String getCodiceFiscale(){
        return codiceFiscale;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
}