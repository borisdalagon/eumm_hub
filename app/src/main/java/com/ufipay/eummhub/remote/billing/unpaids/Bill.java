package com.ufipay.eummhub.remote.billing.unpaids;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.io.Serializable;

public class Bill implements Serializable {
    @JsonAlias("unpaid_bills_number")
    private String billNumber;

    @JsonAlias("bill_amount")
    private String amount;

    @JsonAlias("unpaid_billsissuedate")
    private String issueDate;

    public String getBillNumber() {
        return billNumber;
    }

    public String getAmount() {
        return amount;
    }

    public String getIssueDate() {
        return issueDate;
    }
}
