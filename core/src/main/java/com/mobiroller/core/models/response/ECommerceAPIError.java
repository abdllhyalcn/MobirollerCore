package com.mobiroller.core.models.response;

public class ECommerceAPIError {

    private String message;
    private boolean isUserFriendlyMessage;
    private String[] errors;

    public ECommerceAPIError() {
    }

    public String getMessage() {
        return message;
    }

    public boolean isUserFriendlyMessage() {
        return isUserFriendlyMessage;
    }

    public String[] getErrors() {
        return errors;
    }
}
