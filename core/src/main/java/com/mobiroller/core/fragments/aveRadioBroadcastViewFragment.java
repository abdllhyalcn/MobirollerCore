package com.mobiroller.core.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player.EventListener;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.metadata.icy.IcyInfo;
import com.mobiroller.core.R2;
import com.mobiroller.core.SharedApplication;
import com.mobiroller.core.helpers.LegacyProgressViewHelper;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.models.events.VolumeButtonEvent;
import com.mobiroller.core.R;
import com.mobiroller.core.util.ImageManager;
import com.mobiroller.core.util.radio.RadioManager;
import com.mobiroller.core.views.ScrollingTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NewApi")
public class aveRadioBroadcastViewFragment extends BaseModuleFragment implements EventListener, MetadataOutput {

    LegacyProgressViewHelper legacyProgressViewHelper;

    @BindView(R2.id.radio_broadcast_title)
    TextView radioTitle;
    @BindView(R2.id.radio_broadcast_text)
    ScrollingTextView radioBroadcastText;
    @BindView(R2.id.radio_play)
    ImageButton playButton;
    @BindView(R2.id.volume_mute)
    ImageButton volumeMuteButton;
    @BindView(R2.id.volume_seek_bar)
    SeekBar volumeSeekbar;
    @BindView(R2.id.volume_up)
    ImageButton volumeUpButton;
    @BindView(R2.id.broadcast_cover_layout)
    LinearLayout broadcastLayout;
    @BindView(R2.id.video_broadcast_layout)
    RelativeLayout relativeLayout;
    @BindView(R2.id.logo_image_view)
    ImageView logoImageView;

    private AudioManager audioManager = null;

    RadioManager radioManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.radio_broadcast_view, container, false);
        unbinder = ButterKnife.bind(this, view);

        radioManager = RadioManager.with(SharedApplication.context);
        legacyProgressViewHelper = new LegacyProgressViewHelper(getActivity());
        hideToolbar(view.findViewById(R.id.toolbar_top));
        try {
            loadUi();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (relativeLayout != null) {
            bannerHelper.addBannerAd(relativeLayout, broadcastLayout);
        }

        radioManager.bind();
    }

    private void loadUi() {
        if (networkHelper.isConnected()) {

            getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);

            if (screenModel.backgroundImage != null) {
                ImageManager.loadBackgroundImage(screenModel.backgroundImage, broadcastLayout);

                if (screenModel.logoImage != null) {
                    ImageManager.loadImageView(screenModel.logoImage, logoImageView);
                    logoImageView.setVisibility(View.VISIBLE);
                } else {
                    logoImageView.setVisibility(View.GONE);
                }
            } else {
                ImageManager.loadBackgroundImageFromImageModel(broadcastLayout, screenModel.getBackgroundImageName());
            }

//            if (!screenModel.getContentText().isEmpty() && screenModel.getContentText() != null) {
//                componentHelper.setRadioTitleText(getActivity(), radioTitle, screenModel);
//                radioTitle.setVisibility(View.VISIBLE);
//                radioTitle.setSelected(true);
//            } else {
//                radioTitle.setVisibility(View.INVISIBLE);
//            }

            audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

            volumeUpButton.setOnClickListener(v -> {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                volumeSeekbar.setProgress(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            });

            volumeMuteButton.setOnClickListener(v -> {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                volumeSeekbar.setProgress(0);
            });

            volumeSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            });


            if (radioManager.streamUrl != null && radioManager.streamUrl.equalsIgnoreCase(screenModel.getRadioBroadcastLink())) {
                radioManager.addListeners(this, this);

                onIsPlayingChanged(radioManager.isPlaying());
                String metaDataText = radioManager.getMetadata();
                radioBroadcastText.setText(metaDataText != null ? metaDataText : "");
            }
        }
    }

    @Override
    public void onIsPlayingChanged(boolean isPlaying) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            playButton.setImageDrawable(isPlaying ? ContextCompat.getDrawable(getActivity(), R.drawable.stream_pause_128) : ContextCompat.getDrawable(getActivity(), R.drawable.stream_play_128));
            playButton.setTag(isPlaying ? R.drawable.stream_pause_128 : R.drawable.stream_play_128);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVolumeButtonEvent(VolumeButtonEvent event) {
        onKeyDown(event.getKeyCode());
    }

    private boolean onKeyDown(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            int index = volumeSeekbar.getProgress();
            volumeSeekbar.setProgress(index + 1);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            int index = volumeSeekbar.getProgress();
            volumeSeekbar.setProgress(index - 1);
            return true;
        }
        return true;
    }

    @OnClick(R2.id.radio_play)
    public void onClickPlayButton() {
        radioManager.addListeners(this, this);
        radioManager.playOrPause(screenModel.getRadioBroadcastLink(), UtilManager.localizationHelper().getLocalizedTitle(screenModel.getTitle()));

        onIsPlayingChanged(radioManager.isPlaying());
    }

    @Override
    public void onMetadata(Metadata metadata) {
        for (int i = 0; i < metadata.length(); i++) {
            Metadata.Entry entry = metadata.get(i);
            if (entry instanceof IcyInfo) {
                if (entry != null && ((IcyInfo) entry).title != null) {
                    if (radioBroadcastText != null) {
                        radioBroadcastText.setText(((IcyInfo) entry).title);
                    }
                    radioManager.setMetadata(((IcyInfo) entry).title);
                }
            }
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        if (getActivity() != null && !getActivity().isFinishing())
            Toast.makeText(getActivity(), R.string.common_error, Toast.LENGTH_SHORT).show();
    }

}
