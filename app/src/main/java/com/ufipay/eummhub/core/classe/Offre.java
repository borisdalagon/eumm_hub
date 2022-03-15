package com.ufipay.eummhub.core.classe;

public class Offre {

    private String nom;
    private String montant;

    public Offre() {
    }

    public Offre(String nom, String montant) {
        this.nom = nom;
        this.montant = montant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }
}
