package com.mobiroller.core.jcplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.mobiroller.core.R;

/**
 * Created by jean on 12/07/16.
 */
public class JcNotificationPlayerService implements JcPlayerView.JcPlayerViewServiceListener {
    static final String NEXT = "NEXT";
    static final String PREVIOUS = "PREVIOUS";
    static final String PAUSE = "PAUSE";
    static final String PLAY = "PLAY";
    static final String ACTION = "ACTION";
    static final String PLAYLIST = "PLAYLIST";
    static final String DELETE = "DELETE";
    static final String CURRENT_AUDIO = "CURRENT_AUDIO";

    public static final int MP3_NOTIFICATION_ID = 100;
    private static final int NEXT_ID = 0;
    private static final int PREVIOUS_ID = 1;
    private static final int PLAY_ID = 2;
    private static final int PAUSE_ID = 3;

    private NotificationManager notificationManager;
    private Context context;
    private String title;
    private int iconResource;
    private boolean flag = false;
    private boolean isPlaying = false;

    JcNotificationPlayerService(Context context) {
        this.context = context;
    }


    void createNotificationPlayer(String title, boolean isPlaying) {
        flag = true;
        this.isPlaying = isPlaying;
        createNotificationPlayer(title, R.drawable.icon);
    }

    void createNotificationPlayer(String title, int iconResourceResource) {
        this.title = title;
        this.iconResource = iconResourceResource;
        Intent openUi = new Intent(context, context.getClass());
        openUi.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        JcAudioPlayer.getInstance().registerNotificationListener(this);

        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            if (!flag)
                buildNotification(!JcAudioPlayer.getInstance().isPaused());
            else
                buildNotification(isPlaying);
        } else {
            Notification.Builder notificationCompat = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                notificationCompat = new Notification.Builder(context,"3004")
                        .setSmallIcon(iconResourceResource)
                        .setDeleteIntent(buildPendingIntent(DELETE, 5))
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), iconResourceResource))
                        .setContent(createNotificationPlayerView())
                        .setContentIntent(PendingIntent.getActivity(context, MP3_NOTIFICATION_ID, openUi, PendingIntent.FLAG_CANCEL_CURRENT));
            }else
            {
                notificationCompat = new Notification.Builder(context)
                        .setSmallIcon(iconResourceResource)
                        .setDeleteIntent(buildPendingIntent(DELETE, 5))
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), iconResourceResource))
                        .setContent(createNotificationPlayerView())
                        .setContentIntent(PendingIntent.getActivity(context, MP3_NOTIFICATION_ID, openUi, PendingIntent.FLAG_CANCEL_CURRENT));
            }
            notificationManager.notify(MP3_NOTIFICATION_ID, notificationCompat.build());
        }
    }

    void updateNotification() {
        flag = false;
        createNotificationPlayer(title, iconResource);
    }

    private RemoteViews createNotificationPlayerView() {

        RemoteViews remoteView;
        if (flag) {
            if (!isPlaying) {
                remoteView = new RemoteViews(context.getPackageName(), R.layout.notification_play);
                remoteView.setOnClickPendingIntent(R.id.btn_play_notification, buildPendingIntent(PLAY, PLAY_ID));
            } else {
                remoteView = new RemoteViews(context.getPackageName(), R.layout.notification_pause);
                remoteView.setOnClickPendingIntent(R.id.btn_pause_notification, buildPendingIntent(PAUSE, PAUSE_ID));
            }
        } else {
            if (JcAudioPlayer.getInstance().isPaused()) {
                remoteView = new RemoteViews(context.getPackageName(), R.layout.notification_play);
                remoteView.setOnClickPendingIntent(R.id.btn_play_notification, buildPendingIntent(PLAY, PLAY_ID));
            } else {
                remoteView = new RemoteViews(context.getPackageName(), R.layout.notification_pause);
                remoteView.setOnClickPendingIntent(R.id.btn_pause_notification, buildPendingIntent(PAUSE, PAUSE_ID));
            }
        }

        remoteView.setTextViewText(R.id.txt_current_music_notification, title);

        remoteView.setImageViewResource(R.id.icon_player, iconResource);
        remoteView.setOnClickPendingIntent(R.id.btn_next_notification, buildPendingIntent(NEXT, NEXT_ID));
        remoteView.setOnClickPendingIntent(R.id.btn_prev_notification, buildPendingIntent(PREVIOUS, PREVIOUS_ID));

        return remoteView;
    }

    private void buildNotification(boolean isPlaying) {

        Intent openUi = new Intent(context, context.getClass());
        openUi.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        int notificationAction;//needs to be initialized
        PendingIntent play_pauseAction;

        //Build a new notification according to the current state of the MediaPlayer
        if (isPlaying) {
            notificationAction = R.drawable.ic_pause_white_48dp;
            //create the pause action
            play_pauseAction = buildPendingIntent(PAUSE, PAUSE_ID);
        } else {
            notificationAction = R.drawable.ic_play_arrow_white_48dp;
            //create the play action
            play_pauseAction = buildPendingIntent(PLAY, PLAY_ID);
        }

        // Create a new Notification
        // Hide the timestamp
        // Add playback actions
        getNotificationChannelMusic();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "3034")
                // Hide the timestamp
                .setShowWhen(false)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2))
                .setColor(context.getResources().getColor(R.color.black))
                .setDeleteIntent(buildPendingIntent(DELETE, 5))
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(title)
                .setContentIntent(PendingIntent.getActivity(context, MP3_NOTIFICATION_ID, openUi, PendingIntent.FLAG_CANCEL_CURRENT))
                // Add playback actions
                .addAction(R.drawable.ic_skip_previous_white_48dp, "previous", buildPendingIntent(PREVIOUS, PREVIOUS_ID))
                .addAction(notificationAction, "pause", play_pauseAction)
                .addAction(R.drawable.ic_skip_next_white_48dp, "next", buildPendingIntent(NEXT, NEXT_ID));
        Notification notification = notificationBuilder.build();

        notificationManager.notify(MP3_NOTIFICATION_ID, notification);
    }

    private PendingIntent buildPendingIntent(String action, int id) {
        Intent playIntent = new Intent(context.getApplicationContext(), JcPlayerNotificationReceiver.class);
        playIntent.putExtra(ACTION, action);

        return PendingIntent.getBroadcast(context.getApplicationContext(), id, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onPreparedAudio(String audioName, int duration, String screenId) {

    }

    @Override
    public void onCompletedAudio(String screenId) {

    }

    @Override
    public void onPaused(String screenId) {
        createNotificationPlayer(title, iconResource);
    }

    @Override
    public void onContinueAudio(String screenId) {

    }

    @Override
    public void onPlaying(String screenId) {
    }

    @Override
    public void onTimeChanged(long currentTime, String screenId) {
    }

    @Override
    public void updateTitle(String title, String screenId) {
        this.title = title;
        createNotificationPlayer(title, iconResource);
    }

    void destroyNotificationIfExists() {
        if (notificationManager != null) {
            try {
                notificationManager.cancel(MP3_NOTIFICATION_ID);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private void getNotificationChannelMusic() {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String chanel_id = "3034";
            CharSequence name = context.getString(R.string.notification_music);
            String description = "";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(chanel_id, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }
}