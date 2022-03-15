package com.ufipay.eummhub.remote.user;

import java.io.Serializable;

public class AccountInfoData implements Serializable {
    private String name;
    private String phone;
    private String category;
    private String status;

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getCategory() {
        return category;
    }

    public String getStatus() {
        return status;
    }
}
