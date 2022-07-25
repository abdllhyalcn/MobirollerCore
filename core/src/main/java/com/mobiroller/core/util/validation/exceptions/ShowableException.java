package com.mobiroller.core.util.validation.exceptions;

import android.content.Context;
import android.widget.Toast;

import com.mobiroller.core.R;

public class ShowableException extends Exception {

    public void notifyUserWithToast(Context context) {
        Toast.makeText(context, context.getString(R.string.field_required,toString()), Toast.LENGTH_SHORT).show();
    }
}