package com.mobiroller.core.util;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.models.ApplyzeResponse;
import com.mobiroller.core.models.response.APIError;
import com.mobiroller.core.R;

import retrofit2.Response;

public class ErrorUtils {

    public static APIError parseError(Response<?> response) {
        Gson gson = new Gson();
        APIError error;

        try {
            error = gson.fromJson(response.errorBody().string(),APIError.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIError();
        }

        return error;
    }

    public static ApplyzeResponse parseErrorApplyze(Response response) {
        Gson gson = new Gson();
        ApplyzeResponse error;

        try {
            error = gson.fromJson(response.errorBody().string(),ApplyzeResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApplyzeResponse();
        }

        return error;
    }

    public static void showErrorToast(Context context)
    {
        if (!UtilManager.networkHelper().isConnected())
            Toast.makeText(context, R.string.please_check_your_internet_connection, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, R.string.common_error, Toast.LENGTH_SHORT).show();
    }
}
