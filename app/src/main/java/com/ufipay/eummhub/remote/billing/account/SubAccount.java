package com.ufipay.eummhub.remote.billing.account;

import java.io.Serializable;

public class SubAccount implements Serializable {
    private String id;
    private String owner;
    private String type;
    private String value;

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
