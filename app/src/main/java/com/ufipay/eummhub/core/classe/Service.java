package com.ufipay.eummhub.core.classe;

public class Service {

    private String service;
    private String explication;

    public Service() {
    }

    public Service(String service, String explication) {
        this.service = service;
        this.explication = explication;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getExplication() {
        return explication;
    }

    public void setExplication(String explication) {
        this.explication = explication;
    }
}
