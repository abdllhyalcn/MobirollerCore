package com.mobiroller.core.util.validation.exceptions;

import android.content.Context;

public class RequiredFieldException extends ShowableException {
    private String fieldName;
    public int localisedErrorMessage;
    private Context context;

    public RequiredFieldException(int localisedErrorMessage,Context context) {
        this.context = context;
        this.localisedErrorMessage = localisedErrorMessage;
    }

    public RequiredFieldException(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String toString() {
        if (localisedErrorMessage == 0)
            return fieldName + " cannot be null";
        else
            return context.getString(localisedErrorMessage);
    }

}