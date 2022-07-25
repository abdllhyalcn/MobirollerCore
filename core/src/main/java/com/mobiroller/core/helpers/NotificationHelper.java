package com.mobiroller.core.helpers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.applyze.ApplyzeAnalytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mobiroller.core.constants.DynamicConstants;
import com.mobiroller.core.SharedApplication;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.coreui.helpers.LocaleHelper;

import java.util.HashMap;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationHelper {

    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 10;
    private static final Random random = new Random();
    private static String sRegistrationId;

    private static String TOKEN_FIELD = "token";
    private static String ACCOUNT_NAME_FIELD = "accountName";
    private static String UDID_FIELD = "udid";
    private static String LOCALE_FIELD = "locale";
    private static String USER_ID_FIELD = "userId";
    private static String OS_TYPE_FIELD = "OSType";

    public static void sendToken(Context context, String userId) {
        if (UtilManager.networkHelper().isConnected() && !DynamicConstants.MobiRoller_Stage) {
            try {
                sRegistrationId = UtilManager.sharedPrefHelper().getString(Constants.MobiRoller_Preferences_FCM_Token,"");
                if (sRegistrationId.length() == 0)
                    registerBackground(context, userId);
                else {
                    register(context,
                            AppConfigurations.Companion.getAccountEmail(),
                            sRegistrationId,
                            userId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Get FCM token from FCM servers asynchronously.
     */
    private static void registerBackground(Context context, String userId) {
        if (UtilManager.networkHelper().isConnected()) {
            FirebaseApp firebaseApp = FirebaseApp.getInstance("main");
            FirebaseInstanceId.getInstance(firebaseApp).getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w("FCM", "getInstanceId failed", task.getException());
                                return;
                            }

                            String token = task.getResult().getToken();

                            UtilManager.sharedPrefHelper().setFCMToken(token);
                            register(context,
                                    AppConfigurations.Companion.getAccountEmail(),
                                    sRegistrationId,
                                    userId);
                        }
                    });
        }
    }

    public static void register(final Context context,
                                final String accountName,
                                final String regId,
                                final String userId
    ) {
        SharedPrefHelper sharedPrefHelper = UtilManager.sharedPrefHelper();
        if (sharedPrefHelper.getDeviceId() != null && regId != null) {
            try {
                if (!DynamicConstants.MobiRoller_Stage)
                    new ApplyzeAnalytics.UserInfoBuilder().token(regId).build();
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            HashMap<String, String> params = new HashMap<>();
            params.put(TOKEN_FIELD, regId);
            params.put(ACCOUNT_NAME_FIELD, accountName);
            params.put(UDID_FIELD, sharedPrefHelper.getDeviceId());
            params.put(LOCALE_FIELD, LocaleHelper.getLocale(SharedApplication.context).toUpperCase());
            if (userId != null)
                params.put(USER_ID_FIELD, userId);
            params.put(OS_TYPE_FIELD, "1");

            long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(10);
            // Once FCM returns a registration id, we need to register on our server
            // As the server might be down, we will retry it a couple
            // times.
            for (int i = 1; i <= MAX_ATTEMPTS; i++) {
                try {
                    RxJavaRequestStatsHelper rxJavaRequestHelper = new RxJavaRequestStatsHelper(context, sharedPrefHelper);
                    rxJavaRequestHelper.getAPIService().serverRegister(params).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                        }
                    });
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    // Here we are simplifying and retrying on any error; in a real
                    // application, it should retry only on unrecoverable errors
                    // (like HTTP error code 503).
                    if (i == MAX_ATTEMPTS) {
                        break;
                    }
                    try {
                        Thread.sleep(backoff);
                    } catch (InterruptedException e1) {
                        // Activity finished before we complete - exit.
                        Thread.currentThread().interrupt();
                        return;
                    }
                    // increase backoff exponentially
                    backoff *= 2;
                }
            }
        }
    }


}
