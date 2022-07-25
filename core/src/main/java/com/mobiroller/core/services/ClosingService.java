package com.mobiroller.core.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.mobiroller.core.jcplayer.JcNotificationPlayerService;
import com.mobiroller.core.util.radio.MediaNotificationManager;

public class ClosingService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(MediaNotificationManager.NOTIFICATION_ID);// Remove radio notification
        notificationManager.cancel(JcNotificationPlayerService.MP3_NOTIFICATION_ID); // Remove mp3 notification

        // Destroy the service
        stopSelf();
    }
}
