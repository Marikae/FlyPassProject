package com.example.lez5;

public class User {
    private String name;
    private String surname;
    private String category;
    private String birthday;
    private String birthPlace;
    private String codiceFiscale;
    private String email;
    private String num_health_card;
    private String password;

    public User(String name, String num_health_card, String category, String surname, String birthday, String birthPlace, String codiceFiscale, String email, String password){
        this.name = name;
        this.surname = surname;
        this.category = category;
        this.birthday = birthday;
        this.birthPlace = birthPlace;
        this.codiceFiscale = codiceFiscale;
        this.email = email;
        this.num_health_card = num_health_card;
        this.password = password;
    }

    public String getName(){
        return name;
    }
    public String getCategory(){return category;}
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
    public String getHealCard(){
        return this.num_health_card;
    }
}