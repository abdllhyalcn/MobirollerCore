package com.mobiroller.core.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.mobiroller.core.R;
import com.mobiroller.core.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ealtaca on 12/25/17.
 */

public class aveCallNowViewFragment extends BaseModuleFragment {

    @BindView(R2.id.call_now_image)
    ImageView callNowImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.call_now, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R2.id.call_now_image)
    public void onClickCallImage() {
        if (screenModel.getPhoneNumber() != null || !screenModel.getPhoneNumber().equalsIgnoreCase("null")) {
            startActivity(new Intent(Intent.ACTION_DIAL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setData(Uri.parse("tel:" + screenModel.getPhoneNumber())));
        }
    }
}
