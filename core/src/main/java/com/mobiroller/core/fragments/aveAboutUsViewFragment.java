package com.mobiroller.core.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.mobiroller.core.R;
import com.mobiroller.core.R2;
import com.mobiroller.core.util.ImageManager;
import com.mobiroller.core.util.ShareUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class aveAboutUsViewFragment extends BaseModuleFragment {

    @BindView(R2.id.header_text_view)
    TextView headerTextView;
    @BindView(R2.id.about_text_view)
    TextView aboutTextView;
    @BindView(R2.id.description_text_view)
    TextView descriptionTextView;
    @BindView(R2.id.twitter_image)
    ImageView twitterImage;
    @BindView(R2.id.facebook_image)
    ImageView facebookImage;
    @BindView(R2.id.linkedin_image)
    ImageView linkedinImage;
    @BindView(R2.id.web_image)
    ImageView webImage;
    @BindView(R2.id.google_image)
    ImageView googleImage;
    @BindView(R2.id.email_image)
    ImageView emailImage;
    @BindView(R2.id.main_layout)
    RelativeLayout mainLayout;
    @BindView(R2.id.overlay_layout)
    ConstraintLayout overlayLayout;
    @BindView(R2.id.grid_layout)
    GridLayout gridLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_about_us, container, false);
        unbinder = ButterKnife.bind(this, view);
        loadUI();
        return view;
    }

    private void loadUI() {
        setText(headerTextView,screenModel.getContentHeader());
        setText(aboutTextView,screenModel.getAbout());
        setText(descriptionTextView,screenModel.getDescription());
        setContactImage(twitterImage,screenModel.getTwitter());
        setContactImage(facebookImage,screenModel.getFacebook());
        setContactImage(linkedinImage,screenModel.getLinkedin());
        setContactImage(webImage,screenModel.getWebsite());
        setContactImage(googleImage,screenModel.getGoogleplus());
        setContactImage(emailImage,screenModel.getEmail());
        ImageManager.loadBackgroundImageFromImageModel(mainLayout, screenModel.getBackgroundImageName());
    }

    private void setText(TextView textView,String value) {
        if(value!=null)
            textView.setText(localizationHelper.getLocalizedTitle(value));
    }

    private void setContactImage(ImageView imageView, String value) {
        if(value == null || value.equals(""))
            gridLayout.removeView(imageView);
    }

    @OnClick(R2.id.twitter_image)
    public void onClickTwitterImage() {
        ShareUtil.getOpenTwitterIntent(getActivity(),screenModel.getTwitter());
    }

    @OnClick(R2.id.facebook_image)
    public void onClickFacebookImage() {
        ShareUtil.getOpenFacebookIntent(getActivity(),screenModel.getFacebook());

    }

    @OnClick(R2.id.linkedin_image)
    public void onClickLinkedinImage() {
        ShareUtil.getOpenLinkedinIntent(getActivity(),screenModel.getLinkedin());

    }

    @OnClick(R2.id.web_image)
    public void onClickWebImage() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(screenModel.getWebsite()));
        startActivity(intent);
    }

    @OnClick(R2.id.google_image)
    public void onClickGoogleImage() {
        ShareUtil.getOpenGooglePlusIntent(getActivity(),screenModel.getGoogleplus());
    }

    @OnClick(R2.id.email_image)
    public void onClickEmailImage() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, screenModel.getEmail() );
        startActivity(Intent.createChooser(intent, getString(R.string.send_email)));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mainLayout != null) {
            bannerHelper.addBannerAd(mainLayout, overlayLayout);
        }
    }


}
