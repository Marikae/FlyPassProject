package com.example.lez5;

import java.time.LocalDate;
import java.time.LocalTime;

public class Evento {
    private LocalDate data;

    public enum Sede {
        Verona,
        Venezia,

        Treviso,

        Padova
    }

    String tipoServizio;

    Sede sede;


    private int id;

    private LocalTime inizio;
    private LocalTime fine;

    //public int numeroDisponibilità;

    private boolean disponibile;

    private boolean prenotato;

    private String worker;

    public Evento() {
    }

    public Evento(int id,String worker, LocalDate data, LocalTime inizio, LocalTime fine, boolean disponibile, boolean prenotato, Sede sede, String tipoServizio) {
        this.worker = worker;
        this.id = id;
        this.data = data;
        this.inizio = inizio;
        this.fine = fine;
        this.disponibile = disponibile;
        this.prenotato = prenotato;
        this.sede = sede;
        this.tipoServizio = tipoServizio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getTipoServizio() {
        return tipoServizio;
    }

    public void setTipoServizio(String tipoServizio) {
        this.tipoServizio = tipoServizio;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public LocalTime getInizio() {
        return inizio;
    }

    public void setInizio(LocalTime inizio) {
        this.inizio = inizio;
    }

    public LocalTime getFine() {
        return fine;
    }

    public void setFine(LocalTime fine) {
        this.fine = fine;
    }

    public boolean isDisponibile() {
        return disponibile;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }

    public boolean isPrenotato() {
        return prenotato;
    }

    public void setPrenotato(boolean prenotato) {
        this.prenotato = prenotato;
    }
}

