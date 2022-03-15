package com.ufipay.eummhub.core.data;



public class Country {
    private String name;
    private String codeISO2;
    private String devise;
    private String phoneCode;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodeISO2() {
        return codeISO2;
    }

    public void setCodeISO2(String codeISO2) {
        this.codeISO2 = codeISO2;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }


}
