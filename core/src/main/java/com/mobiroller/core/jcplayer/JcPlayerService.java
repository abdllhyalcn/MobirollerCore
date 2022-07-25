package com.mobiroller.core.jcplayer;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.jcplayer.JcPlayerExceptions.AudioAssetsInvalidException;
import com.mobiroller.core.jcplayer.JcPlayerExceptions.AudioFilePathInvalidException;
import com.mobiroller.core.jcplayer.JcPlayerExceptions.AudioRawInvalidException;
import com.mobiroller.core.jcplayer.JcPlayerExceptions.AudioUrlInvalidException;
import com.mobiroller.core.models.events.MP3Event;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mobiroller.core.jcplayer.JcNotificationPlayerService.MP3_NOTIFICATION_ID;

public class JcPlayerService extends Service implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnErrorListener,
        AudioManager.OnAudioFocusChangeListener {

    private static final String TAG = JcPlayerService.class.getSimpleName();

    private final IBinder mBinder = new JcPlayerServiceBinder();
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;
    private int duration;
    private int currentTime;
    private JcAudio currentJcAudio;
    private JcStatus jcStatus = new JcStatus();
    private List<JcPlayerView.JcPlayerViewServiceListener> jcPlayerServiceListeners;
    private List<JcPlayerView.OnInvalidPathListener> invalidPathListeners;
    private List<JcPlayerView.JcPlayerViewStatusListener> jcPlayerStatusListeners;
    private JcPlayerView.JcPlayerViewServiceListener notificationListener;
    private String screenId;
    private boolean isPausedFromState = false;
    private AudioManager audioManager;
    private JcNotificationPlayerService jcNotificationPlayer;

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public String getScreenId() {
        return screenId;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        //Invoked when the audio focus of the system is updated.
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (mediaPlayer != null) {
                    if (!mediaPlayer.isPlaying() && isPausedFromState) {
                        play(currentJcAudio);
                        jcNotificationPlayer.createNotificationPlayer(currentJcAudio.getTitle(), true);
                        isPausedFromState = false;
                    }
                    mediaPlayer.setVolume(1.0f, 1.0f);
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    isPausedFromState = true;
                    pause(currentJcAudio);
                    jcNotificationPlayer.createNotificationPlayer(currentJcAudio.getTitle(), false);
                }
                audioManager.abandonAudioFocus(this);

                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    isPausedFromState = true;
                    pause(currentJcAudio);
                    jcNotificationPlayer.createNotificationPlayer(currentJcAudio.getTitle(), false);
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.setVolume(0.1f, 0.1f);
                    jcNotificationPlayer.createNotificationPlayer(currentJcAudio.getTitle(), true);
                }
                break;
        }

    }

    public class JcPlayerServiceBinder extends Binder {
        public JcPlayerService getService() {
            EventBus.getDefault().post(new MP3Event(screenId, isPlaying, duration, currentTime, currentJcAudio));
            return JcPlayerService.this;
        }
    }

    public void registerNotificationListener(JcPlayerView.JcPlayerViewServiceListener notificationListener) {
        this.notificationListener = notificationListener;
    }

    public void registerServicePlayerListener(JcPlayerView.JcPlayerViewServiceListener jcPlayerServiceListener) {
        if (jcPlayerServiceListeners == null) {
            jcPlayerServiceListeners = new ArrayList<>();
        }

        if (!jcPlayerServiceListeners.contains(jcPlayerServiceListener)) {
            jcPlayerServiceListeners.add(jcPlayerServiceListener);
        }
    }

    public void registerInvalidPathListener(JcPlayerView.OnInvalidPathListener invalidPathListener) {
        if (invalidPathListeners == null) {
            invalidPathListeners = new ArrayList<>();
        }

        if (!invalidPathListeners.contains(invalidPathListener)) {
            invalidPathListeners.add(invalidPathListener);
        }
    }

    public void registerStatusListener(JcPlayerView.JcPlayerViewStatusListener statusListener) {
        if (jcPlayerStatusListeners == null) {
            jcPlayerStatusListeners = new ArrayList<>();
        }

        if (!jcPlayerStatusListeners.contains(statusListener)) {
            jcPlayerStatusListeners.add(statusListener);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (intent.hasExtra(Constants.KEY_SCREEN_ID)) {
            screenId = intent.getStringExtra(Constants.KEY_SCREEN_ID);
        }
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.jcNotificationPlayer = new JcNotificationPlayerService(getApplicationContext());

        registerBecomingNoisyReceiver();
    }

    public JcPlayerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.hasExtra(Constants.KEY_SCREEN_ID)) {
            screenId = intent.getStringExtra(Constants.KEY_SCREEN_ID);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(MP3_NOTIFICATION_ID);

        try {
            removeAudioFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            unregisterReceiver(becomingNoisyReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pause(JcAudio jcAudio) {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            duration = mediaPlayer.getDuration();
            currentTime = mediaPlayer.getCurrentPosition();
            isPlaying = false;
        }

        for (JcPlayerView.JcPlayerViewServiceListener jcPlayerServiceListener : jcPlayerServiceListeners) {
            jcPlayerServiceListener.onPaused(screenId);
        }

        if (notificationListener != null) {
            notificationListener.onPaused(screenId);
        }

        if (jcPlayerStatusListeners != null) {
            for (JcPlayerView.JcPlayerViewStatusListener jcPlayerStatusListener : jcPlayerStatusListeners) {
                jcStatus.setJcAudio(jcAudio);
                jcStatus.setDuration(duration);
                jcStatus.setCurrentPosition(currentTime);
                jcStatus.setPlayState(JcStatus.PlayState.PAUSE);
                jcPlayerStatusListener.onPausedStatus(jcStatus, screenId);
            }
        }
    }

    public void stop(JcAudio jcAudio) {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            duration = mediaPlayer.getDuration();
            currentTime = mediaPlayer.getCurrentPosition();
            isPlaying = false;
        }

        for (JcPlayerView.JcPlayerViewServiceListener jcPlayerServiceListener : jcPlayerServiceListeners) {
            jcPlayerServiceListener.onPaused(screenId);
        }


        if (jcPlayerStatusListeners != null) {
            for (JcPlayerView.JcPlayerViewStatusListener jcPlayerStatusListener : jcPlayerStatusListeners) {
                jcStatus.setJcAudio(jcAudio);
                jcStatus.setDuration(duration);
                jcStatus.setCurrentPosition(currentTime);
                jcStatus.setPlayState(JcStatus.PlayState.PAUSE);
                jcPlayerStatusListener.onPausedStatus(jcStatus, screenId);
            }
        }
    }

    public void destroy() {
        stop();
        stopSelf();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(MP3_NOTIFICATION_ID);
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        isPlaying = false;
    }

    public void play(JcAudio jcAudio) {
        JcAudio tempJcAudio = this.currentJcAudio;
        this.currentJcAudio = jcAudio;
        requestAudioFocus();
        if (isAudioFileValid(jcAudio.getPath(), jcAudio.getOrigin())) {
            try {
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();

                    if (jcAudio.getOrigin() == Origin.URL) {
                        mediaPlayer.setDataSource(jcAudio.getPath());
                    }

                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(this);
                    mediaPlayer.setOnBufferingUpdateListener(this);
                    mediaPlayer.setOnCompletionListener(this);
                    mediaPlayer.setOnErrorListener(this);

                } else {
                    if (isPlaying) {
                        stop();
                        play(jcAudio);
                    } else {
                        if (tempJcAudio != jcAudio) {
                            stop();
                            play(jcAudio);
                        } else {
                            mediaPlayer.start();
                            isPlaying = true;

                            if (jcPlayerServiceListeners != null) {
                                for (JcPlayerView.JcPlayerViewServiceListener jcPlayerServiceListener : jcPlayerServiceListeners) {
                                    jcPlayerServiceListener.onContinueAudio(screenId);
                                }
                            }

                            if (jcPlayerStatusListeners != null) {
                                for (JcPlayerView.JcPlayerViewStatusListener jcPlayerViewStatusListener : jcPlayerStatusListeners) {
                                    jcStatus.setJcAudio(jcAudio);
                                    jcStatus.setPlayState(JcStatus.PlayState.PLAY);
                                    jcStatus.setDuration(mediaPlayer.getDuration());
                                    jcStatus.setCurrentPosition(mediaPlayer.getCurrentPosition());
                                    jcPlayerViewStatusListener.onContinueAudioStatus(jcStatus, screenId);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            updateTimeAudio();

            for (JcPlayerView.JcPlayerViewServiceListener jcPlayerServiceListener : jcPlayerServiceListeners) {
                jcPlayerServiceListener.onPlaying(screenId);
            }

            if (jcPlayerStatusListeners != null) {
                for (JcPlayerView.JcPlayerViewStatusListener jcPlayerViewStatusListener : jcPlayerStatusListeners) {
                    jcStatus.setJcAudio(jcAudio);
                    jcStatus.setPlayState(JcStatus.PlayState.PLAY);
                    jcStatus.setDuration(0);
                    jcStatus.setCurrentPosition(0);
                    jcPlayerViewStatusListener.onPlayingStatus(jcStatus, screenId);
                }
            }

            if (notificationListener != null) notificationListener.onPlaying(screenId);

        } else {
            try {
                throwError(jcAudio.getPath(), jcAudio.getOrigin());
            }catch (Exception e) {
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void seekTo(int time) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(time);
        }
    }

    private void updateTimeAudio() {
        new Thread() {
            public void run() {
                while (isPlaying) {
                    try {

                        if (jcPlayerServiceListeners != null) {
                            for (JcPlayerView.JcPlayerViewServiceListener jcPlayerServiceListener : jcPlayerServiceListeners) {
                                jcPlayerServiceListener.onTimeChanged(mediaPlayer.getCurrentPosition(), screenId);
                            }
                        }
                        if (notificationListener != null) {
                            notificationListener.onTimeChanged(mediaPlayer.getCurrentPosition(), screenId);
                        }

                        if (jcPlayerStatusListeners != null) {
                            for (JcPlayerView.JcPlayerViewStatusListener jcPlayerViewStatusListener : jcPlayerStatusListeners) {
                                jcStatus.setPlayState(JcStatus.PlayState.PLAY);
                                jcStatus.setDuration(mediaPlayer.getDuration());
                                jcStatus.setCurrentPosition(mediaPlayer.getCurrentPosition());
                                jcPlayerViewStatusListener.onTimeChangedStatus(jcStatus, screenId);
                            }
                        }
                        Thread.sleep(200);
                    } catch (IllegalStateException | InterruptedException | NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (jcPlayerServiceListeners != null) {
            for (JcPlayerView.JcPlayerViewServiceListener jcPlayerServiceListener : jcPlayerServiceListeners) {
                jcPlayerServiceListener.onCompletedAudio(screenId);
            }
        }
        if (notificationListener != null) {
            notificationListener.onCompletedAudio(screenId);
        }

        if (jcPlayerStatusListeners != null) {
            for (JcPlayerView.JcPlayerViewStatusListener jcPlayerViewStatusListener : jcPlayerStatusListeners) {
                jcPlayerViewStatusListener.onCompletedAudioStatus(jcStatus, screenId);
            }
        }
    }

    private void throwError(String path, Origin origin) {
        if (origin == Origin.URL) {
            throw new AudioUrlInvalidException(path);
        } else if (origin == Origin.RAW) {
            try {
                throw new AudioRawInvalidException(path);
            } catch (AudioRawInvalidException e) {
                e.printStackTrace();
            }
        } else if (origin == Origin.ASSETS) {
            try {
                throw new AudioAssetsInvalidException(path);
            } catch (AudioAssetsInvalidException e) {
                e.printStackTrace();
            }
        } else if (origin == Origin.FILE_PATH) {
            try {
                throw new AudioFilePathInvalidException(path);
            } catch (AudioFilePathInvalidException e) {
                e.printStackTrace();
            }
        }

        if (invalidPathListeners != null) {
            for (JcPlayerView.OnInvalidPathListener onInvalidPathListener : invalidPathListeners) {
                onInvalidPathListener.onPathError(currentJcAudio);
            }
        }
    }


    private boolean isAudioFileValid(String path, Origin origin) {
        // For Asset and Raw file.
        AssetFileDescriptor assetFileDescriptor = null;
        if (origin == Origin.URL) {
            return path.startsWith("http") || path.startsWith("https");
        } else if (origin == Origin.RAW) {
            assetFileDescriptor = null;
            assetFileDescriptor = getApplicationContext().getResources().openRawResourceFd(Integer.parseInt(path));
            return assetFileDescriptor != null;
        } else if (origin == Origin.ASSETS) {
            try {
                assetFileDescriptor = null;
                assetFileDescriptor = getApplicationContext().getAssets().openFd(path);
                return assetFileDescriptor != null;
            } catch (IOException e) {
                return false;
            }
        } else if (origin == Origin.FILE_PATH) {
            File file = new File(path);
            return file.exists();
        } else {
            // We should never arrive here.
            return false; // We don't know what the origin of the Audio File
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        isPlaying = true;
        this.duration = mediaPlayer.getDuration();
        this.currentTime = mediaPlayer.getCurrentPosition();
        updateTimeAudio();

        if (jcPlayerServiceListeners != null) {
            for (JcPlayerView.JcPlayerViewServiceListener jcPlayerServiceListener : jcPlayerServiceListeners) {
                jcPlayerServiceListener.updateTitle(currentJcAudio.getTitle(), screenId);
                jcPlayerServiceListener.onPreparedAudio(currentJcAudio.getTitle(), mediaPlayer.getDuration(), screenId);
            }
        }

        if (notificationListener != null) {
            notificationListener.updateTitle(currentJcAudio.getTitle(), screenId);
            notificationListener.onPreparedAudio(currentJcAudio.getTitle(), mediaPlayer.getDuration(), screenId);
        }

        if (jcPlayerStatusListeners != null) {
            for (JcPlayerView.JcPlayerViewStatusListener jcPlayerViewStatusListener : jcPlayerStatusListeners) {
                jcStatus.setJcAudio(currentJcAudio);
                jcStatus.setPlayState(JcStatus.PlayState.PLAY);
                jcStatus.setDuration(duration);
                jcStatus.setCurrentPosition(currentTime);
                jcPlayerViewStatusListener.onPreparedAudioStatus(jcStatus, screenId);
            }
        }
    }

    public JcAudio getCurrentAudio() {
        return currentJcAudio;
    }


    /**
     * AudioFocus
     */
    private void requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        //Could not gain focus
    }

    private void removeAudioFocus() {
        try {
            audioManager.abandonAudioFocus(this);
        } catch (Exception e) {
            //try catch block added for Null Pointer Exception
        }
    }


    /**
     * ACTION_AUDIO_BECOMING_NOISY -- change in audio outputs
     */
    private BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //pause audio on ACTION_AUDIO_BECOMING_NOISY
            if (mediaPlayer != null && mediaPlayer.isPlaying())
                pause(currentJcAudio);
        }
    };

    private void registerBecomingNoisyReceiver() {
        //register after getting audio focus
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(becomingNoisyReceiver, intentFilter);
    }
}
