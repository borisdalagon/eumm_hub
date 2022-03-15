package com.ufipay.eummhub.core.classe;

public class Facture {

    private String numero;
    private String montant;
    private String issueDate;

    public Facture(String numero, String montant, String issueDate) {
        this.numero = numero;
        this.montant = montant;
        this.issueDate = issueDate;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }
}
