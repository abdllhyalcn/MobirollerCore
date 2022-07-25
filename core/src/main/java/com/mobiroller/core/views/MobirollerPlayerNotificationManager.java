package com.mobiroller.core.views;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

import java.util.List;

public class MobirollerPlayerNotificationManager extends PlayerNotificationManager {

    public boolean isRadio = false;

    public MobirollerPlayerNotificationManager(Context context, String channelId, int notificationId, MediaDescriptionAdapter mediaDescriptionAdapter) {
        super(context, channelId, notificationId, mediaDescriptionAdapter);
    }

    public MobirollerPlayerNotificationManager(Context context, String channelId, int notificationId, MediaDescriptionAdapter mediaDescriptionAdapter, @Nullable NotificationListener notificationListener) {
        super(context, channelId, notificationId, mediaDescriptionAdapter, notificationListener);
    }

    public MobirollerPlayerNotificationManager(Context context, String channelId, int notificationId, MediaDescriptionAdapter mediaDescriptionAdapter, @Nullable CustomActionReceiver customActionReceiver) {
        super(context, channelId, notificationId, mediaDescriptionAdapter, customActionReceiver);
    }

    public MobirollerPlayerNotificationManager(Context context, String channelId, int notificationId, MediaDescriptionAdapter mediaDescriptionAdapter, @Nullable NotificationListener notificationListener, @Nullable CustomActionReceiver customActionReceiver) {
        super(context, channelId, notificationId, mediaDescriptionAdapter, notificationListener, customActionReceiver);
    }

    @Override
    protected List<String> getActions(Player player) {

        List<String> list = super.getActions(player);

        if(isRadio) {
            list.remove(ACTION_PREVIOUS);
            list.remove(ACTION_NEXT);
            list.remove(ACTION_FAST_FORWARD);
            list.remove(ACTION_REWIND);
        }

        return list;
    }
}
