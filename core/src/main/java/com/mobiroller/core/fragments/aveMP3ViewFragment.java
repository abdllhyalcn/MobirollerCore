package com.mobiroller.core.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiroller.core.R2;
import com.mobiroller.core.adapters.MP3ListAdapter;
import com.mobiroller.core.helpers.LegacyProgressViewHelper;
import com.mobiroller.core.jcplayer.JcAudio;
import com.mobiroller.core.jcplayer.JcPlayerView;
import com.mobiroller.core.models.Audio;
import com.mobiroller.core.coreui.models.TableItemsModel;
import com.mobiroller.core.models.events.MP3PositionEvent;
import com.mobiroller.core.R;
import com.mobiroller.core.util.ImageManager;
import com.mobiroller.core.views.ItemClickSupport;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NewApi")
public class aveMP3ViewFragment extends BaseModuleFragment implements JcPlayerView.JcPlayerViewServiceListener {

    LegacyProgressViewHelper legacyProgressViewHelper;

    @BindView(R2.id.list_mp3)
    RecyclerView list;
    @BindView(R2.id.mp3_ad_layout)
    RelativeLayout mp3AdLayout;
    @BindView(R2.id.mp3_layout)
    RelativeLayout mp3Layout;
    @BindView(R2.id.jcplayer)
    JcPlayerView jcplayerView;
    @BindView(R2.id.image)
    ImageView image;

    private MP3ListAdapter adapter;
    private int selectedPosition = -1;
    private ArrayList<Audio> audioList = new ArrayList<>();
    private ArrayList<JcAudio> jcAudios = new ArrayList<>();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_mp3, container, false);
        legacyProgressViewHelper = new LegacyProgressViewHelper(getActivity());
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, view);
        hideToolbar(view.findViewById(R.id.toolbar_top));
        loadUi();
        loadAudioList();
        return view;
    }

    private void loadAudioList() {
        initRecyclerView();
    }

    private void initRecyclerView() {

        if (audioList != null && audioList.size() > 0) {
            for (int i = 0; i < audioList.size(); i++) {
                jcAudios.add(JcAudio.createFromURL(audioList.get(i).getTitle(), audioList.get(i).getData()));
            }
            jcplayerView.registerServiceListener(this);
            jcplayerView.initPlaylist(jcAudios, screenId);
            adapter = new MP3ListAdapter(audioList, getActivity().getApplicationContext(),screenModel.getTableCellBackground1(),screenModel.getTableTextColor());
            list.setAdapter(adapter);
            list.setLayoutManager(new LinearLayoutManager(getActivity()));
            ItemClickSupport.addTo(list).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    jcplayerView.playAudio(jcAudios.get(position));
                }
            });
        }
    }

    private void loadUi() {

        if (networkHelper.isConnected()) {
            try {
                ImageManager.loadBackgroundImageFromImageModel(mp3Layout, screenModel.getBackgroundImageName());
                if (screenModel.getMainImageName() != null) {
                    ImageManager.loadImageView(getActivity(), screenModel.getMainImageName().getImageURL(), image);
                    image.setScaleType(ScaleType.FIT_XY);
                    image.setVisibility(View.VISIBLE);
                } else {
                    image.setVisibility(View.GONE);
                }

                ArrayList<TableItemsModel> jsonArray = screenModel.getTableItems();
                for (int i = 0; i < jsonArray.size(); i++) {
                    TableItemsModel jsonObject = jsonArray.get(i);
                    audioList.add(new Audio(jsonObject.getFileURL(), jsonObject.getTitle()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPreparedAudio(String audioName, int duration, String screenId) {

    }

    @Override
    public void onCompletedAudio(String screenId) {

    }

    @Override
    public void onPaused(String screenId) {

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
        int position = findItemFromTitle(title);
        if (position != -1) {
            setSelected(position);
        }
    }


    public void setSelected(int position) {
        if (selectedPosition != position) {
            if (selectedPosition != -1) {
                adapter.getItemByPosition(selectedPosition).setSelected(false);
                adapter.notifyItemChanged(selectedPosition);
            }

            selectedPosition = position;
            adapter.getItemByPosition(selectedPosition).setSelected(true);
            adapter.notifyItemChanged(selectedPosition);
        }
    }

    private int findItemFromTitle(String title) {
        for (int i = 0; i < audioList.size(); i++) {
            if (title.equalsIgnoreCase(audioList.get(i).getTitle())) {
                return i;
            }
        }
        return -1;
    }

    @Subscribe
    public void onPostMp3PositionEvent(MP3PositionEvent event) {
        if (screenId.equalsIgnoreCase(event.getScreenId()) && event.getJcAudio() != null) {
            int position = findItemFromTitle(event.getJcAudio().getTitle());
            if (position != -1)
                setSelected(position);
            if (!event.isPlaying()) {
                ((MP3ListAdapter.Mp3ContentViewHolder) list.findViewHolderForAdapterPosition(selectedPosition)).pauseAnimation();
            } else {
                ((MP3ListAdapter.Mp3ContentViewHolder) list.findViewHolderForAdapterPosition(selectedPosition)).playAnimation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mp3Layout != null) {
            bannerHelper.addBannerAd(mp3Layout, mp3AdLayout);
        }
    }


}