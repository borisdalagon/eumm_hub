package com.ufipay.eummhub.remote;

import java.io.Serializable;

public class BaseResponse<T> implements Serializable {
    private int code;
    private String status;
    private String message;
    private T data;

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
