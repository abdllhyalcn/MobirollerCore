package com.mobiroller.core.jcplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mobiroller.core.jcplayer.JcPlayerExceptions.AudioListNullPointerException;
import com.mobiroller.core.models.events.StartMedia;
import com.mobiroller.core.models.events.StopMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class JcPlayerNotificationReceiver extends BroadcastReceiver {

    private boolean isStopByAds = false;
    public JcPlayerNotificationReceiver() {
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        JcAudioPlayer jcAudioPlayer = JcAudioPlayer.getInstance();
        String action = "";

        if (intent.hasExtra(JcNotificationPlayerService.ACTION)) {
            action = intent.getStringExtra(JcNotificationPlayerService.ACTION);
        }

        switch (action) {
            case JcNotificationPlayerService.PLAY:
                try {
                    jcAudioPlayer.continueAudio();
                    jcAudioPlayer.updateNotification();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case JcNotificationPlayerService.PAUSE:
                try {
                    if (jcAudioPlayer != null) {
                        jcAudioPlayer.pauseAudio();
                        jcAudioPlayer.updateNotification();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case JcNotificationPlayerService.NEXT:
                try {
                    jcAudioPlayer.nextAudio();
                } catch (AudioListNullPointerException e) {
                    try {
                        jcAudioPlayer.continueAudio();
                    } catch (AudioListNullPointerException e1) {
                        e1.printStackTrace();
                    }
                }
                break;

            case JcNotificationPlayerService.PREVIOUS:
                try {
                    jcAudioPlayer.previousAudio();
                } catch (Exception e) {
                    try {
                        jcAudioPlayer.continueAudio();
                    } catch (AudioListNullPointerException e1) {
                        e1.printStackTrace();
                    }
                }
                break;
            case JcNotificationPlayerService.DELETE:
                try {
                    if (jcAudioPlayer != null && jcAudioPlayer.isPlaying()) {
                        jcAudioPlayer.stopAudio();
                    }
                    context.stopService(new Intent(context, JcPlayerService.class));
                    context.stopService(new Intent(context, JcNotificationPlayerService.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Subscribe
    public void onPostStopMedia(StopMedia stopMedia) {
        JcAudioPlayer jcAudioPlayer = JcAudioPlayer.getInstance();
        try {
            if (jcAudioPlayer != null) {
                isStopByAds = true;
                jcAudioPlayer.pauseAudio();
                jcAudioPlayer.updateNotification();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onPostStartMedia(StartMedia startMedia) {
        if(isStopByAds) {
            JcAudioPlayer jcAudioPlayer = JcAudioPlayer.getInstance();
            try {
                jcAudioPlayer.continueAudio();
                jcAudioPlayer.updateNotification();
            } catch (Exception e) {
                e.printStackTrace();
            }
            isStopByAds = false;
        }
    }


}
