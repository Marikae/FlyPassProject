package com.example.lez5;

public class Worker extends User{
    public Evento.Sede office;
    public Worker(String name, String surname, String email, String password, Evento.Sede office) {
        super(name,  surname, email, password);
        this.office = office;

    }
    public Evento.Sede getOffice(){
        return this.office;
    }
}
