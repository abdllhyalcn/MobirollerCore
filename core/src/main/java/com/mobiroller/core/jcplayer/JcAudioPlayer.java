package com.mobiroller.core.jcplayer;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.jcplayer.JcPlayerExceptions.AudioListNullPointerException;

import java.io.Serializable;
import java.util.List;

import static com.mobiroller.core.jcplayer.JcNotificationPlayerService.MP3_NOTIFICATION_ID;

/**
 * Created by jean on 12/07/16.
 */

class JcAudioPlayer {
    private JcPlayerService jcPlayerService;
    private JcPlayerView.JcPlayerViewServiceListener listener;
    private JcPlayerView.OnInvalidPathListener invalidPathListener;
    private JcPlayerView.JcPlayerViewStatusListener statusListener;
    private JcNotificationPlayerService jcNotificationPlayer;
    private List<JcAudio> playlist;
    private JcAudio currentJcAudio;
    private int currentPositionList;
    private Context context;
    private static JcAudioPlayer instance = null;
    private boolean mBound = false;
    private boolean playing;
    private boolean paused;
    private int position = 1;
    private String screenId;

    JcAudioPlayer(Context context, List<JcAudio> playlist, JcPlayerView.JcPlayerViewServiceListener listener, String screenId) {
        this.context = context;
        this.screenId = screenId;
        this.playlist = playlist;
        this.listener = listener;
        instance = JcAudioPlayer.this;
        this.jcNotificationPlayer = new JcNotificationPlayerService(context);
        initService();
    }

    public void setInstance(JcAudioPlayer instance) {
        JcAudioPlayer.instance = instance;
    }

    void registerNotificationListener(JcPlayerView.JcPlayerViewServiceListener notificationListener) {
        this.listener = notificationListener;
        if (jcNotificationPlayer != null) {
            jcPlayerService.registerNotificationListener(notificationListener);
        }
    }

    void registerInvalidPathListener(JcPlayerView.OnInvalidPathListener registerInvalidPathListener) {
        this.invalidPathListener = registerInvalidPathListener;
        if (jcPlayerService != null) {
            jcPlayerService.registerInvalidPathListener(invalidPathListener);
        }
    }

    void registerServiceListener(JcPlayerView.JcPlayerViewServiceListener jcPlayerServiceListener) {
        this.listener = jcPlayerServiceListener;
        if (jcPlayerService != null) {
            jcPlayerService.registerServicePlayerListener(jcPlayerServiceListener);
        }
    }

    void registerStatusListener(JcPlayerView.JcPlayerViewStatusListener statusListener) {
        this.statusListener = statusListener;
        if (jcPlayerService != null) {
            jcPlayerService.registerStatusListener(statusListener);
        }
    }

    public static JcAudioPlayer getInstance() {
        return instance;
    }

    void playAudio(JcAudio JcAudio) throws AudioListNullPointerException {
        if (playlist == null || playlist.size() == 0) {
            throw new AudioListNullPointerException();
        }
        currentJcAudio = JcAudio;
        jcPlayerService.setScreenId(screenId);
        jcPlayerService.play(currentJcAudio);
        updatePositionAudioList();
        playing = true;
        paused = false;
    }

    private void initService() {
        if (!mBound) {
            startJcPlayerService();
        } else {
            mBound = true;
            if (jcPlayerService != null && !screenId.equals(jcPlayerService.getScreenId())) {
                mBound = false;
                kill();
                startJcPlayerService();
            }
        }
    }

    void nextAudio() throws AudioListNullPointerException {
        if (playlist == null || playlist.size() == 0) {
            throw new AudioListNullPointerException();
        } else {
            if (currentJcAudio != null) {
                try {
                    JcAudio nextJcAudio = playlist.get(currentPositionList + position);
                    this.currentJcAudio = nextJcAudio;
                    jcPlayerService.stop();
                    jcPlayerService.play(nextJcAudio);

                } catch (IndexOutOfBoundsException e) {
                    playAudio(playlist.get(0));
                    e.printStackTrace();
                }
            }

            updatePositionAudioList();
            playing = true;
            paused = false;
        }
    }

    void previousAudio() throws AudioListNullPointerException {
        if (playlist == null || playlist.size() == 0) {
            throw new AudioListNullPointerException();
        } else {
            if (currentJcAudio != null) {
                try {
                    JcAudio previousJcAudio = playlist.get(currentPositionList - position);
                    this.currentJcAudio = previousJcAudio;
                    jcPlayerService.stop();
                    jcPlayerService.play(previousJcAudio);

                } catch (IndexOutOfBoundsException e) {
                    playAudio(playlist.get(0));
                    e.printStackTrace();
                }
            }

            updatePositionAudioList();
            playing = true;
            paused = false;
        }
    }

    void pauseAudio() {
        jcPlayerService.pause(currentJcAudio);
        paused = true;
        playing = false;
    }

    void stopAudio() {
        jcPlayerService.stop(currentJcAudio);
        paused = true;
        playing = false;
    }

    void continueAudio() throws AudioListNullPointerException {
        if (playlist == null || playlist.size() == 0) {
            throw new AudioListNullPointerException();
        } else {
            if (currentJcAudio == null) {
                currentJcAudio = playlist.get(0);
            }
            playAudio(currentJcAudio);
            playing = true;
            paused = false;
        }
    }

    void createNewNotification(int iconResource) {
        if (currentJcAudio != null) {
            jcNotificationPlayer.createNotificationPlayer(currentJcAudio.getTitle(), iconResource);
        }
    }
    void updateNotification() {
        jcNotificationPlayer.updateNotification();
    }

    void seekTo(int time) {
        if (jcPlayerService != null) {
            jcPlayerService.seekTo(time);
        }
    }

    private void updatePositionAudioList() {
        if (playlist != null)
            for (int i = 0; i < playlist.size(); i++) {
                if (playlist.get(i).getId() == currentJcAudio.getId()) {
                    this.currentPositionList = i;
                }
            }
    }

    private synchronized void startJcPlayerService() {
        if (!mBound) {
            Intent intent = new Intent(context.getApplicationContext(), JcPlayerService.class);
            intent.putExtra(Constants.KEY_SCREEN_ID, screenId);
            intent.putExtra(JcNotificationPlayerService.PLAYLIST, (Serializable) playlist);
            intent.putExtra(JcNotificationPlayerService.CURRENT_AUDIO, currentJcAudio);
            context.startService(intent);
            context.bindService(intent, mConnection, context.getApplicationContext().BIND_AUTO_CREATE);
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            JcPlayerService.JcPlayerServiceBinder binder = (JcPlayerService.JcPlayerServiceBinder) service;
            jcPlayerService = binder.getService();
            if (listener != null) {
                jcPlayerService.registerServicePlayerListener(listener);
            }
            if (invalidPathListener != null) {
                jcPlayerService.registerInvalidPathListener(invalidPathListener);
            }
            if (statusListener != null) {
                jcPlayerService.registerStatusListener(statusListener);
            }
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
            playing = false;
            paused = true;
        }
    };

    boolean isPaused() {
        return paused;
    }

    boolean isPlaying() {
        return playing;
    }

    void kill() {
        if (jcPlayerService != null) {
            jcPlayerService.stop();
            jcPlayerService.destroy();
        }

        if (mBound)
            try {
                context.unbindService(mConnection);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }

        if (jcNotificationPlayer != null) {
            jcNotificationPlayer.destroyNotificationIfExists();
        }

        if (JcAudioPlayer.getInstance() != null)
            JcAudioPlayer.getInstance().setInstance(null);
    }

    public List<JcAudio> getPlaylist() {
        return playlist;
    }

    JcAudio getCurrentAudio() {
        return jcPlayerService.getCurrentAudio();
    }

    public void destroy() {
        try {

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(MP3_NOTIFICATION_ID);
            kill();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setScreenId(String screenId1) {
        this.screenId = screenId1;
    }

    public String getScreenId() {
        return screenId;
    }

}
