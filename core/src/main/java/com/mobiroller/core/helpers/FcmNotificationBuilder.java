package com.mobiroller.core.helpers;

/**
 * Created by ealtaca on 7/18/17.
 */

import android.content.Context;

import com.mobiroller.core.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ealtaca on 7/18/17.
 */

public class FcmNotificationBuilder {
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "FcmNotificationBuilder";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String AUTHORIZATION = "Authorization";
    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    // json related keys
    private static final String KEY_TO = "to";
    private static final String KEY_NOTIFICATION = "notification";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TEXT = "text";
    private static final String KEY_DATA = "data";
    private static final String KEY_INFO_TYPE = "info_type";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_UID = "uid";
    private static final String KEY_SCREEN_ID = "screen_id";
    private static final String KEY_NOTIFICATION_TYPE = "notification_type";
    private static final String KEY_RECEIVER_UID = "receiver_uid";
    private static final String KEY_FCM_TOKEN = "fcm_token";

    private String mTitle;
    private String mMessage;
    private String mUsername;
    private String mUid;
    private String mReceiverUid;
    private String mFirebaseToken;
    private String mScreenId;
    private int mInfoType;
    private String mReceiverFirebaseToken;
    private String mNotificationType = "textMessage";

    private Context context;

    private FcmNotificationBuilder() {

    }

    public static FcmNotificationBuilder initialize() {
        return new FcmNotificationBuilder();
    }

    public FcmNotificationBuilder title(String title) {
        mTitle = title;
        return this;
    }
    public FcmNotificationBuilder infoType(int infoType) {
        mInfoType = infoType;
        return this;
    }

    public FcmNotificationBuilder context(Context context) {
        this.context = context;
        return this;
    }

    public FcmNotificationBuilder message(String message) {
        mMessage = message;
        return this;
    }

    public FcmNotificationBuilder notificationType(String notificationType) {
        mNotificationType = notificationType;
        return this;
    }

    public FcmNotificationBuilder username(String username) {
        mUsername = username;
        return this;
    }

    public FcmNotificationBuilder uid(String uid) {
        mUid = uid;
        return this;
    }

    public FcmNotificationBuilder receiverUid(String receiverUid) {
        mReceiverUid = receiverUid;
        return this;
    }

    public FcmNotificationBuilder screenId(String screenId) {
        mScreenId = screenId;
        return this;
    }

    public FcmNotificationBuilder firebaseToken(String firebaseToken) {
        mFirebaseToken = firebaseToken;
        return this;
    }

    public FcmNotificationBuilder receiverFirebaseToken(String receiverFirebaseToken) {
        mReceiverFirebaseToken = receiverFirebaseToken;
        return this;
    }

    public void send() {
        RequestBody requestBody = null;
        try {
            requestBody = RequestBody.create(MEDIA_TYPE_JSON, getValidJsonBody().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .addHeader(AUTHORIZATION, "key=" + AesBase64Wrapper.decodeBase64(context.getString(R.string.firebase_gcm_server_api_key)))
                .url(FCM_URL)
                .post(requestBody)
                .build();

        Call call = new OkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });
    }

    private JSONObject getValidJsonBody() throws JSONException {
        JSONObject jsonObjectBody = new JSONObject();
        jsonObjectBody.put(KEY_TO, mReceiverFirebaseToken);

        JSONObject jsonObjectData = new JSONObject();
        jsonObjectData.put(KEY_TITLE, mTitle);
        jsonObjectData.put(KEY_TEXT, mMessage);
        jsonObjectData.put(KEY_USERNAME, mUsername);
        jsonObjectData.put(KEY_UID, mUid);
        jsonObjectData.put(KEY_RECEIVER_UID, mReceiverUid);
        jsonObjectData.put(KEY_FCM_TOKEN, mFirebaseToken);
        jsonObjectData.put(KEY_SCREEN_ID, mScreenId);
        jsonObjectData.put(KEY_INFO_TYPE, mInfoType);
        jsonObjectData.put(KEY_NOTIFICATION_TYPE, mNotificationType);
        jsonObjectBody.put(KEY_DATA, jsonObjectData);

        return jsonObjectBody;
    }
}