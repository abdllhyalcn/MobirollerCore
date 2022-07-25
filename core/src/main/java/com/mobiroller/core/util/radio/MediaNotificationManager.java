package com.mobiroller.core.util.radio;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.metadata.icy.IcyInfo;
import com.mobiroller.core.R;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;

public class MediaNotificationManager implements MetadataOutput {

    public static final int NOTIFICATION_ID = 555;
    private final String PRIMARY_CHANNEL = "PRIMARY_CHANNEL_ID";
    private final String PRIMARY_CHANNEL_NAME = "PRIMARY";

    private RadioService service;

    private String strAppName;

    private Resources resources;

    private NotificationManagerCompat notificationManager;
    private String screenName = "";
    private String title = "";

    public MediaNotificationManager(RadioService service) {
        service.addNotificationMetadataListener(this);
        this.service = service;
        this.resources = service.getResources();

        strAppName = resources.getString(R.string.app_name);

        notificationManager = NotificationManagerCompat.from(service);
    }

    public void startNotify(String playbackStatus, String title, String screenName) {

        if (screenName != null && !screenName.equalsIgnoreCase(""))
            this.screenName = screenName;
        if (title != null && !title.equalsIgnoreCase(""))
            this.title = title;
        Bitmap largeIcon = BitmapFactory.decodeResource(resources, R.drawable.icon);

        int icon = R.drawable.ic_pause_white_24dp;
        Intent playbackAction = new Intent(service, RadioService.class);
        playbackAction.setAction(RadioService.ACTION_PAUSE);
        PendingIntent action = PendingIntent.getService(service, 1, playbackAction, 0);

        if (playbackStatus.equals(PlaybackStatus.PAUSED)) {

            icon = R.drawable.exo_icon_play;
            playbackAction.setAction(RadioService.ACTION_PLAY);
            action = PendingIntent.getService(service, 2, playbackAction, FLAG_CANCEL_CURRENT);

        }

        notificationManager.cancel(NOTIFICATION_ID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(PRIMARY_CHANNEL, PRIMARY_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(service, PRIMARY_CHANNEL)
                .setContentTitle(this.title)
                .setContentText(this.screenName)
                .setContentIntent(PendingIntent.getActivity(service.getApplicationContext(), 0, new Intent(), 0))
                .setLargeIcon(largeIcon)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(android.R.drawable.stat_sys_headset)
                .addAction(icon, "pause", action)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setWhen(System.currentTimeMillis())
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(service.getMediaSession().getSessionToken())
                        .setShowActionsInCompactView(0));
        service.startForeground(NOTIFICATION_ID, builder.build());
        if(!service.isPlaying())
            service.stopForeground(false);

    }

    public void cancelNotify() {

        service.stopForeground(true);
    }

    @Override
    public void onMetadata(Metadata metadata) {
        for (int i = 0; i < metadata.length(); i++) {
            Metadata.Entry entry = metadata.get(i);
            if (entry instanceof IcyInfo) {
                if (entry != null && ((IcyInfo) entry).title != null) {
                    startNotify(service.getStatus(), ((IcyInfo) entry).title, screenName);
                }
            }
        }
    }
}
