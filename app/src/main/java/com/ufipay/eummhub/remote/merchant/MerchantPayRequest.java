package com.ufipay.eummhub.remote.merchant;

import com.ufipay.eummhub.remote.BaseRequest;

import java.io.Serializable;

public class MerchantPayRequest extends BaseRequest implements Serializable {

    private String amount;
    private String merchant;
    private String reference;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

}
