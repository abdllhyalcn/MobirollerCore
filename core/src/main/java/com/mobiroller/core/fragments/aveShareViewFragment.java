package com.mobiroller.core.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.mobiroller.core.R2;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.helpers.SharedPrefHelper;
import com.mobiroller.core.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by ealtaca on 12/27/17.
 */

public class aveShareViewFragment extends BaseModuleFragment {

    @BindView(R2.id.share_image)
    ImageView shareImage;

    @Inject
    LocalizationHelper localizationHelper;

    @Inject
    SharedPrefHelper sharedPrefHelper;
    Unbinder unbinder;

    private String contentText;
    private String url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_share_view, container, false);
        if (screenModel.getContentText() != null) {
            this.contentText = localizationHelper.getLocalizedTitle(screenModel.getContentText());
            this.url = screenModel.getGooglePlayLink();
        }
        unbinder = ButterKnife.bind(this, view);
        shareImage.setColorFilter(sharedPrefHelper.getActionBarColor(), PorterDuff.Mode.SRC_ATOP);
        return view;
    }


    @OnClick(R2.id.share_image)
    public void onClickShareImage() {
        if (contentText != null) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, this.contentText + "  " + this.url);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, getString(R.string.action_share)));
        } else {
            Toast.makeText(getActivity(), getString(R.string.common_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
