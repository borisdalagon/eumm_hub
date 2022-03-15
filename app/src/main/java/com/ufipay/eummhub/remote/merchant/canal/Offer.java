package com.ufipay.eummhub.remote.merchant.canal;

import java.io.Serializable;

public class Offer implements Serializable {

    private String amount;
    private String name;

    public Offer(String name, String amount) {
        this.amount = amount;
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
