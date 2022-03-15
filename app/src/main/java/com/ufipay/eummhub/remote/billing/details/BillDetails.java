package com.ufipay.eummhub.remote.billing.details;

import java.io.Serializable;

public class BillDetails implements Serializable {
    private String amount;
    private String message;
    private String state;
    private String status;
    private String issueDate;

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getAmount() {
        return amount;
    }

    public String getMessage() {
        return message;
    }

    public String getState() {
        return state;
    }

    public String getStatus() {
        return status;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
