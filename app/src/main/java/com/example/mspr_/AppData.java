package com.example.mspr_;

import android.app.Application;

public class AppData {
    private static AppData instance = new AppData();

    // Getter-Setters
    public static AppData getInstance() {
        return instance;
    }
    public static void setInstance(AppData instance) {
        AppData.instance = instance;
    }
    private AppData() {
    }

    // Variables globales de l'application
    private String pseudo;
    private String nom;

    public String getPseudo() {
        return pseudo;
    }
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
}
