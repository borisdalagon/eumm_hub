package com.ufipay.eummhub.remote.user;

import java.io.Serializable;

public class BalanceData implements Serializable {
    private String available;
    private String commission;
    private String principal;

    public String getAvailable() {
        return available;
    }

    public String getCommission() {
        return commission;
    }

    public String getPrincipal() {
        return principal;
    }
}
