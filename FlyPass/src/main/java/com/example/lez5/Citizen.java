package com.example.lez5;
public class Citizen extends User{
        private String name;
        private String surname;
        private String category;
        private String birthday;
        private String birthPlace;
        private String codiceFiscale;
        private String email;
        private String num_health_card;
        private String password;
        public Citizen(String name, String num_health_card, String category, String surname, String birthday, String birthPlace, String codiceFiscale, String email, String password){
            super(name,  surname, email, password);
            this.category = category;
            this.birthday = birthday;
            this.birthPlace = birthPlace;
            this.codiceFiscale = codiceFiscale;
            this.num_health_card = num_health_card;
        }
        public String getCategory(){return category;}
        public String getBirthday(){
            return birthday;
        }
        public String getBirthPlace(){
            return birthPlace;
        }
        public String getCodiceFiscale(){
            return codiceFiscale;
        }
        public String getHealCard(){
            return this.num_health_card;
        }
}
