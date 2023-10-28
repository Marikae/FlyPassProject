package com.example.lez5;

public class Model {
    private static Model modelInstance; // statico e protetto da accesso esterno

    private Model() {
    }
    public static Model getModel() { // metodo aperto, invocazione tramite codice
        if (modelInstance == null) { // solamente quando non esiste alcuna istanza, ne crea una nuova
            modelInstance = new Model();
        }
        return modelInstance;
    }
}
