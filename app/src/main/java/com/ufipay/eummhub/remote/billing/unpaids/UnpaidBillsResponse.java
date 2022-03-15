package com.ufipay.eummhub.remote.billing.unpaids;

import java.io.Serializable;

public class UnpaidBillsResponse implements Serializable {
    private UnpaidBillsData data;
    private String message;
    private String status;

    public UnpaidBillsData getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
