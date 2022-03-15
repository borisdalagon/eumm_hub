package com.ufipay.eummhub.remote.billing.unpaids;

import java.io.Serializable;
import java.util.List;

public class UnpaidBillsData implements Serializable {
    private List<Bill> bills;
    private String message;
    private String status;

    public List<Bill> getBills() {
        return bills;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
