package com.mobiroller.core.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.mobiroller.core.R2;
import com.mobiroller.core.activities.aveFullScreenVideo;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.helpers.JSONStorage;
import com.mobiroller.core.helpers.LegacyProgressViewHelper;
import com.mobiroller.core.R;
import com.mobiroller.core.util.InterstitialAdsUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NewApi")
public class aveTVBroadcastViewFragment extends BaseModuleFragment {
    LegacyProgressViewHelper legacyProgressViewHelper;
    @BindView(R2.id.my_spinner)
    ProgressBar spinnerView;
    @BindView(R2.id.play_tv)
    ImageButton playButton;
    @BindView(R2.id.video_broadcast_layout)
    RelativeLayout videoBroadcastLayout;

    private InterstitialAdsUtil interstitialAdsUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_broadcast_view_fullscreen, container, false);
        unbinder = ButterKnife.bind(this, view);
        interstitialAdsUtil = new InterstitialAdsUtil(getActivity());
        legacyProgressViewHelper = new LegacyProgressViewHelper(getActivity());
        hideToolbar(view.findViewById(R.id.toolbar_top));
        legacyProgressViewHelper.dismiss();
        loadUI();

        return view;
    }

    public void loadUI() {
        playButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent fullScreenIntent = new Intent(getActivity(), aveFullScreenVideo.class);
                fullScreenIntent.putExtra(Constants.KEY_SCREEN_ID,screenId);
                fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                JSONStorage.putScreenModel(screenId,screenModel);
                interstitialAdsUtil.checkInterstitialAds(fullScreenIntent);

            }
        });
    }

}
