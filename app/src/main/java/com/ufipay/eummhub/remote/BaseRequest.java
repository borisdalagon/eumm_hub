package com.ufipay.eummhub.remote;

import java.io.Serializable;

public abstract class BaseRequest implements Serializable {
    private String mpin;
    private String accountNumber;

    public String getMpin() {
        return mpin;
    }

    public void setMpin(String mpin) {
        this.mpin = mpin;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
