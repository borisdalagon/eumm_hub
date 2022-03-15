package com.ufipay.eummhub.core.classe;

import java.io.Serializable;

public class RapportRequest implements Serializable {

    private String statut;
    private String code;
    private String message;
    private String operation;
    private String typeRequet;

    public RapportRequest() {
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getTypeRequet() {
        return typeRequet;
    }

    public void setTypeRequet(String typeRequet) {
        this.typeRequet = typeRequet;
    }
}
