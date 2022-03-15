package com.ufipay.eummhub.core.classe;

public class Abonnee {

    private String nom;
    private String numero;

    public Abonnee() {
    }

    public Abonnee(String nom, String numero) {
        this.nom = nom;
        this.numero = numero;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
