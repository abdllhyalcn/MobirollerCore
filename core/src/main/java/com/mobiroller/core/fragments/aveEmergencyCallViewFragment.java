package com.mobiroller.core.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiroller.core.adapters.EmergencyCallAdapter;
import com.mobiroller.core.coreui.models.TableItemsModel;
import com.mobiroller.core.R;
import com.mobiroller.core.util.ImageManager;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by ealtaca on 17.05.2017.
 */

public class aveEmergencyCallViewFragment extends BaseModuleFragment {

    private RecyclerView recyclerView;
    private RelativeLayout mainLayout;
    private RelativeLayout overlayLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.emergency_call_layout, container, false);
        unbinder = ButterKnife.bind(this, view);

        recyclerView = view.findViewById(R.id.faq_recycler_view);
        mainLayout = view.findViewById(R.id.faq_main_layout);
        overlayLayout = view.findViewById(R.id.overlay_layout);

        loadUI(screenModel.getTableItems());
        return view;
    }

    private void loadUI(ArrayList<TableItemsModel> objectArrayList) {
        ImageManager.loadBackgroundImageFromImageModel(mainLayout, screenModel.getBackgroundImageName());
        EmergencyCallAdapter emergencyCallAdapter = new EmergencyCallAdapter(getActivity(), objectArrayList, sharedPrefHelper, localizationHelper);
        recyclerView.setAdapter(emergencyCallAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mainLayout != null) {
            bannerHelper.addBannerAd(mainLayout, overlayLayout);
        }
    }
}
