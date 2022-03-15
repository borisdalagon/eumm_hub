package com.ufipay.eummhub.remote.remittance;

import com.ufipay.eummhub.remote.BaseRequest;

import java.io.Serializable;

public class TransferRequest extends BaseRequest implements Serializable {
    private String beneficiary;
    private int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }
}
