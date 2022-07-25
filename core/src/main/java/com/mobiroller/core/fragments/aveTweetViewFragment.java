package com.mobiroller.core.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiroller.core.R2;
import com.mobiroller.core.helpers.LegacyProgressViewHelper;
import com.mobiroller.core.R;
import com.mobiroller.core.util.ImageManager;
import com.mobiroller.core.views.SimpleDividerItemDecoration;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import butterknife.BindView;
import butterknife.ButterKnife;

public class aveTweetViewFragment extends BaseModuleFragment {
    LegacyProgressViewHelper legacyProgressViewHelper;

    @BindView(R2.id.twitter_list)
    RecyclerView list;
    @BindView(R2.id.twitter_layout)
    RelativeLayout twitterLayout;
    @BindView(R2.id.twitter_relative_overlay)
    RelativeLayout twitter_relative_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.twitter_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        legacyProgressViewHelper = new LegacyProgressViewHelper(getActivity());
        hideToolbar(view.findViewById(R.id.toolbar_top));

        setHasOptionsMenu(true);
        if (twitter_relative_layout != null) {
            bannerHelper.addBannerAd(twitter_relative_layout, list);
        }

        if (networkHelper.isConnected()) {
            try {
                legacyProgressViewHelper.show();
                ImageManager.loadBackgroundImageFromImageModel(twitterLayout, screenModel.getBackgroundImageName());

                UserTimeline userTimeline = new UserTimeline.Builder().maxItemsPerRequest(20).screenName(screenModel.getUserName()).build();
                final TweetTimelineRecyclerViewAdapter adapterm =
                        new TweetTimelineRecyclerViewAdapter.Builder(getActivity())
                                .setTimeline(userTimeline)
                                .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
                                .setOnActionCallback(new Callback<Tweet>() {
                                    @Override
                                    public void success(Result<Tweet> result) {
                                        legacyProgressViewHelper.dismiss();
                                    }

                                    @Override
                                    public void failure(TwitterException exception) {
                                        legacyProgressViewHelper.dismiss();
                                        Toast.makeText(getActivity(), R.string.common_error, Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .build();

                list.setAdapter(adapterm);
                list.setLayoutManager(new LinearLayoutManager(getActivity()));
                list.addItemDecoration(new SimpleDividerItemDecoration(
                        getActivity()
                        , R.drawable.line_drawer
                ));

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.please_check_your_internet_connection), Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
        return view;
    }

}
