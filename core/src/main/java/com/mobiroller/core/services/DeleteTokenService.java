package com.mobiroller.core.services;

import android.app.IntentService;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mobiroller.core.constants.ChatConstants;
import com.mobiroller.core.helpers.SharedPrefHelper;

import java.io.IOException;

/**
 * Created by ealtaca on 7/18/17.
 */

public class DeleteTokenService extends IntentService
{
    public static final String TAG = DeleteTokenService.class.getSimpleName();

    public DeleteTokenService()
    {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        try
        {
            // Check for current token
            String originalToken = getTokenFromPrefs();

            // Resets Instance ID and revokes all tokens.
            FirebaseInstanceId.getInstance().deleteInstanceId();

            // Clear current saved token
            saveTokenToPrefs("");

            // Check for success of empty token
            String tokenCheck = getTokenFromPrefs();

            // Now manually call onTokenRefresh()
            FirebaseInstanceId.getInstance().getToken();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void saveTokenToPrefs(String token)
    {
        new SharedPrefHelper(getApplicationContext()).setFirebaseToken(token);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child(ChatConstants.ARG_USERS)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(ChatConstants.ARG_FIREBASE_TOKEN)
                    .setValue(token);
        }
    }

    private String getTokenFromPrefs()
    {
        return new SharedPrefHelper(getApplicationContext()).getFirebaseToken();
    }
}