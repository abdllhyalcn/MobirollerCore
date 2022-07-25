package com.mobiroller.core.jcplayer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.jcplayer.JcPlayerExceptions.AudioListNullPointerException;
import com.mobiroller.core.models.events.MP3Event;
import com.mobiroller.core.models.events.MP3PositionEvent;
import com.mobiroller.core.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class JcPlayerView extends LinearLayout implements
        View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = JcPlayerView.class.getSimpleName();

    private TextView txtCurrentMusic;
    private ImageButton btnPrev;
    private LottieAnimationView btnPlay;
    private ProgressBar progressBarPlayer;
    private JcAudioPlayer jcAudioPlayer;
    private TextView txtDuration;
    private ImageButton btnNext;
    private SeekBar seekBar;
    private TextView txtCurrentDuration;
    private boolean isInitialized;
    private String screenId;

    private OnInvalidPathListener onInvalidPathListener = new OnInvalidPathListener() {
        @Override
        public void onPathError(JcAudio jcAudio) {
            dismissProgressBar();
        }
    };

    JcPlayerViewServiceListener jcPlayerViewServiceListener = new JcPlayerViewServiceListener() {

        @Override
        public void onPreparedAudio(String audioName, int duration, String screenId1) {

            if (screenId.equalsIgnoreCase(screenId1)) {
                dismissProgressBar();
                resetPlayerInfo();

                long aux = duration / 1000;
                int minute = (int) (aux / 60);
                int second = (int) (aux % 60);

                final String sDuration = // Minutes
                        (minute < 10 ? "0" + minute : minute + "")
                                + ":" +
                                // Seconds
                                (second < 10 ? "0" + second : second + "");

                seekBar.setMax(duration);

                txtDuration.post(new Runnable() {
                    @Override
                    public void run() {
                        txtDuration.setText(sDuration);
                    }
                });
            }
        }

        @Override
        public void onCompletedAudio(String screenId1) {

            if (screenId.equalsIgnoreCase(screenId1)) {
                resetPlayerInfo();

                try {
                    jcAudioPlayer.nextAudio();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onPaused(String screenId1) {
            if (screenId.equalsIgnoreCase(screenId1)) {
                animate(false);
            }
        }

        @Override
        public void onContinueAudio(String screenId) {
            dismissProgressBar();
        }

        @Override
        public void onPlaying(String screenId1) {

            if (screenId.equalsIgnoreCase(screenId1)) {
                animate(true);
            }
        }

        @Override
        public void onTimeChanged(long currentPosition, String screenId1) {
            if (screenId.equalsIgnoreCase(screenId1)) {
                long aux = currentPosition / 1000;
                int minutes = (int) (aux / 60);
                int seconds = (int) (aux % 60);
                final String sMinutes = minutes < 10 ? "0" + minutes : minutes + "";
                final String sSeconds = seconds < 10 ? "0" + seconds : seconds + "";

                seekBar.setProgress((int) currentPosition);
                txtCurrentDuration.post(new Runnable() {
                    @Override
                    public void run() {
                        txtCurrentDuration.setText(String.valueOf(sMinutes + ":" + sSeconds));
                    }
                });

            }
        }

        @Override
        public void updateTitle(final String title, String screenId1) {
            if (screenId.equalsIgnoreCase( screenId1)) {
                txtCurrentMusic.post(new Runnable() {
                    @Override
                    public void run() {
                        txtCurrentMusic.setText(title);
                    }
                });
            }
        }
    };

    public interface OnInvalidPathListener {
        void onPathError(JcAudio jcAudio);
    }

    public interface JcPlayerViewStatusListener {
        void onPausedStatus(JcStatus jcStatus, String screenId);

        void onContinueAudioStatus(JcStatus jcStatus, String screenId);

        void onPlayingStatus(JcStatus jcStatus, String screenId);

        void onTimeChangedStatus(JcStatus jcStatus, String screenId);

        void onCompletedAudioStatus(JcStatus jcStatus, String screenId);

        void onPreparedAudioStatus(JcStatus jcStatus, String screenId);
    }

    public interface JcPlayerViewServiceListener {

        void onPreparedAudio(String audioName, int duration, String screenId);

        void onCompletedAudio(String screenId);

        void onPaused(String screenId);

        void onContinueAudio(String screenId);

        void onPlaying(String screenId);

        void onTimeChanged(long currentTime, String screenId);

        void updateTitle(String title, String screenId);
    }

    public JcPlayerView(Context context) {
        super(context);
        init();
    }

    public JcPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public JcPlayerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void init() {
        inflate(getContext(), R.layout.view_jcplayer, this);
        this.progressBarPlayer = findViewById(R.id.progress_bar_player);
        this.btnNext = findViewById(R.id.btn_next);
        this.btnPrev = findViewById(R.id.btn_prev);
        this.btnPlay = findViewById(R.id.btn_play);
        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
        this.txtDuration = findViewById(R.id.txt_total_duration);
        this.txtCurrentDuration = findViewById(R.id.txt_current_duration);
        this.txtCurrentMusic = findViewById(R.id.txt_current_music);
        this.seekBar = findViewById(R.id.seek_bar);
        this.btnPlay.setTag(R.drawable.ic_play_arrow_white_48dp);
        seekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        progressBarPlayer.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        relativeLayout.setBackgroundColor(UtilManager.sharedPrefHelper().getActionBarColor());
        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
    }

    /**
     * Initialize the playlist and controls.
     *
     * @param playlist List of JcAudio objects that you want play
     */
    public void initPlaylist(List<JcAudio> playlist, String screenId1) {
        // Don't sort if the playlist have position number.
        // We need to do this because there is a possibility that the user reload previous playlist
        // from persistence storage like sharedPreference or SQLite.
        this.screenId = screenId1;
        if (!isAlreadySorted(playlist)) {
            sortPlaylist(playlist);
        }
        jcAudioPlayer = new JcAudioPlayer(getContext().getApplicationContext(), playlist, jcPlayerViewServiceListener, screenId1);
        jcAudioPlayer.setScreenId(screenId1);
        jcAudioPlayer.registerInvalidPathListener(onInvalidPathListener);
        //jcAudioPlayer.registerStatusListener(jcPlayerViewStatusListener);
        isInitialized = true;

        EventBus.getDefault().register(this);
    }

    public void playAudio(JcAudio jcAudio) {
        showProgressBar();
        createJcAudioPlayer();
        if (!jcAudioPlayer.getPlaylist().contains(jcAudio))
            jcAudioPlayer.getPlaylist().add(jcAudio);

        try {
            jcAudioPlayer.playAudio(jcAudio);
        } catch (AudioListNullPointerException e) {
            dismissProgressBar();
            e.printStackTrace();
        }
        createNotification();
    }

    public void next() {
        if (jcAudioPlayer.getCurrentAudio() == null) {
            return;
        }
        resetPlayerInfo();
        showProgressBar();

        try {
            jcAudioPlayer.nextAudio();
        } catch (AudioListNullPointerException e) {
            dismissProgressBar();
            e.printStackTrace();
        }
    }

    public void continueAudio() {
        showProgressBar();

        try {
            jcAudioPlayer.continueAudio();
        } catch (AudioListNullPointerException e) {
            dismissProgressBar();
            e.printStackTrace();
        }
    }

    public void pause() {
        jcAudioPlayer.pauseAudio();
    }

    public void previous() {
        resetPlayerInfo();
        showProgressBar();

        try {
            jcAudioPlayer.previousAudio();
        } catch (AudioListNullPointerException e) {
            dismissProgressBar();
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if (isInitialized) {
            if (view.getId() == R.id.btn_play) {
                if (btnPlay.getTag().equals(R.drawable.ic_pause_white_48dp)) {
                    pause();
                } else {
                    continueAudio();
                }
                createNotification();
            }
            if (view.getId() == R.id.btn_next) {
                next();
            }

            if (view.getId() == R.id.btn_prev) {
                previous();
            }
        }
    }

    /**
     * Create a notification player with same playlist with a default icon
     */
    public void createNotification() {
        if (jcAudioPlayer != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // For light theme
                jcAudioPlayer.createNewNotification(R.drawable.icon);
            } else {
                // For dark theme
                jcAudioPlayer.createNewNotification(R.drawable.icon);
            }
        }
    }

    public boolean isPlaying() {
        return jcAudioPlayer.isPlaying();
    }

    private void createJcAudioPlayer() {
        if (jcAudioPlayer == null) {
            List<JcAudio> playlist = new ArrayList<>();
            jcAudioPlayer = new JcAudioPlayer(getContext(), playlist, jcPlayerViewServiceListener, screenId);
        }
        jcAudioPlayer.registerInvalidPathListener(onInvalidPathListener);
        //jcAudioPlayer.registerStatusListener(jcPlayerViewStatusListener);
        isInitialized = true;
    }

    private void sortPlaylist(List<JcAudio> playlist) {
        for (int i = 0; i < playlist.size(); i++) {
            JcAudio jcAudio = playlist.get(i);
            jcAudio.setId(i);
            jcAudio.setPosition(i);
        }
    }

    /**
     * Check if playlist already sorted or not.
     * We need to check because there is a possibility that the user reload previous playlist
     * from persistence storage like sharedPreference or SQLite.
     *
     * @param playlist list of JcAudio
     * @return true if sorted, false if not.
     */
    private boolean isAlreadySorted(List<JcAudio> playlist) {
        // If there is position in the first audio, then playlist is already sorted.
        if (playlist != null) {
            return playlist.get(0).getPosition() != -1;
        } else {
            return false;
        }
    }

    private void showProgressBar() {
        if (isInitialized) {
            EventBus.getDefault().post(new MP3PositionEvent(jcAudioPlayer.getCurrentAudio(), screenId, isPlaying()));
            progressBarPlayer.setVisibility(ProgressBar.VISIBLE);
            btnPlay.setVisibility(Button.GONE);
            btnNext.setClickable(false);
            btnPrev.setClickable(false);
        }
    }

    private void dismissProgressBar() {
        if (isInitialized) {
            EventBus.getDefault().post(new MP3PositionEvent(jcAudioPlayer.getCurrentAudio(), screenId, isPlaying()));
            progressBarPlayer.setVisibility(ProgressBar.GONE);
            btnPlay.setVisibility(Button.VISIBLE);
            btnNext.setClickable(true);
            btnPrev.setClickable(true);
        }
    }

    private void resetPlayerInfo() {
        seekBar.setProgress(0);
        txtCurrentMusic.setText("");
        txtCurrentDuration.setText("00:00");
        txtDuration.setText("00:00");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {

        if (isInitialized && fromUser && jcAudioPlayer != null) jcAudioPlayer.seekTo(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        showProgressBar();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        dismissProgressBar();
    }

    public void registerServiceListener(JcPlayerViewServiceListener jcPlayerServiceListener) {
        if (jcAudioPlayer != null) {
            jcAudioPlayer.registerServiceListener(jcPlayerServiceListener);
        }
    }


    public String getScreenId() {
        return screenId;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventData(MP3Event event) {

        if (event.getScreenId() == screenId) {
            if (event.getDuration() != 0) {
                seekBar.setMax(event.getDuration());
            }
            if (event.getCurrentJcAudio() != null) {
                txtCurrentMusic.setText(event.getCurrentJcAudio().getTitle());
            }
            if (event.isPlaying()) {
                animate(true, event);
            }
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        // View is now detached, and about to be destroyed
    }

    public void animate(boolean flag) {
        if (flag) {
            btnPlay.setTag(R.drawable.ic_pause_white_48dp);
            btnPlay.setProgress(0);
            btnPlay.setMinAndMaxProgress(0, 0.5f);
        } else {
            btnPlay.setTag(R.drawable.ic_play_arrow_white_48dp);
            btnPlay.setProgress(0.5f);
            btnPlay.setMinAndMaxProgress(0.5f, 1f);
        }
        btnPlay.playAnimation();

        if (jcAudioPlayer.getCurrentAudio() != null) {
            EventBus.getDefault().post(new MP3PositionEvent(jcAudioPlayer.getCurrentAudio(), screenId, flag));
        }
    }

    public void animate(boolean flag, MP3Event event) {
        if (flag) {
            btnPlay.setTag(R.drawable.ic_pause_white_48dp);
            btnPlay.setProgress(0);
            btnPlay.setMinAndMaxProgress(0, 0.5f);
        } else {
            btnPlay.setTag(R.drawable.ic_play_arrow_white_48dp);
            btnPlay.setProgress(0.5f);
            btnPlay.setMinAndMaxProgress(0.5f, 1f);
        }
        btnPlay.playAnimation();

        if (event.getCurrentJcAudio() != null) {
            EventBus.getDefault().post(new MP3PositionEvent(event.getCurrentJcAudio(), screenId, flag));
        }
    }

}
