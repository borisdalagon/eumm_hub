package com.ufipay.eummhub.remote.billing.pay;

import com.ufipay.eummhub.remote.BaseRequest;

import java.io.Serializable;

/**
 * Object for Pay Bill Request :
 */
public class PBRequest extends BaseRequest implements Serializable {
    private String billNumber;

    /**
     * Type : Eum PBType
     */
    private String type;
    //private enum type {ENEO, CAMAWATER}

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
