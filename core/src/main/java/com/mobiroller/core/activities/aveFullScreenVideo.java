package com.mobiroller.core.activities;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.mobiroller.core.R2;
import com.mobiroller.core.activities.base.AveActivity;
import com.mobiroller.core.helpers.JSONStorage;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.interfaces.ActivityComponent;
import com.mobiroller.core.models.events.StartMedia;
import com.mobiroller.core.models.events.StopMedia;
import com.mobiroller.core.R;
import com.mobiroller.core.util.AdManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class aveFullScreenVideo extends AveActivity implements MediaPlayer.OnErrorListener, OnCompletionListener, OnTouchListener, OnPreparedListener {

    @BindView(R2.id.video_broadcast_view)
    VideoView mVideoView;
    @BindView(R2.id.my_spinner)
    ProgressBar spinnerView;
    @BindView(R2.id.play_tv)
    ImageButton playButton;
    @BindView(R2.id.video_broadcast_layout)
    FrameLayout videoBroadcastLayout;

    private Uri streamUri;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.video_broadcast_view);
        ButterKnife.bind(this);

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        playButton.setClickable(false);
        playButton.setVisibility(View.GONE);
        mVideoView.setOnErrorListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnTouchListener(this);

        if (UtilManager.networkHelper().isConnected()) {
            screenModel = JSONStorage.getScreenModel(screenId);
            String video_url = screenModel.getTvBroadcastLink();
            streamUri = Uri.parse(video_url);

            try {
                mVideoView.setVideoURI(streamUri);
                mVideoView.requestFocus();
                mVideoView.setOnPreparedListener(this);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.please_check_your_stream_link), Toast.LENGTH_LONG).show();
            }
            if (AdManager.isAdOpen)
                if (mVideoView != null) {
                    mVideoView.pause();
                }
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.please_check_your_internet_connection), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public AppCompatActivity injectActivity(ActivityComponent component) {
        component.inject(this);
        return this;
    }

    @Subscribe
    public void onPostStopMedia(StopMedia stopMedia) {
        if (mVideoView != null) {
            mVideoView.pause();
        }
    }

    @Subscribe
    public void onPostStartMedia(StartMedia startMedia) {
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        try {
            mVideoView.clearFocus();
            onBackPressed();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.please_check_your_stream_link), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            onBackPressed();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.please_check_your_stream_link), Toast.LENGTH_LONG).show();
        }

        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        spinnerView.setVisibility(View.GONE);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mVideoView.requestFocus();
                if (mVideoView.isPlaying()) {
                    mVideoView.stopPlayback();
                    spinnerView.setVisibility(View.GONE);
                    playButton.setVisibility(View.VISIBLE);
                } else {
                    spinnerView.setVisibility(View.VISIBLE);
                    playButton.setVisibility(View.GONE);
                    mVideoView.setVideoURI(streamUri);
                }
                return true;
            // Do something that should only happen when the user first touches the screen
            case MotionEvent.ACTION_MOVE:
                return false;
            // Do something that should only happen when the user is touching the screen
            case MotionEvent.ACTION_UP:
                return false;
            // Do something that should only happen when the user stops touching the screen
        }
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        spinnerView.setVisibility(View.GONE);
        mVideoView.start();
    }
}