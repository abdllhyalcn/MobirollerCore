package com.mobiroller.core.models.response;

public class APIError {

    private int statusCode;
    private String[] errors;

    public APIError() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return errors[0];
    }
}
