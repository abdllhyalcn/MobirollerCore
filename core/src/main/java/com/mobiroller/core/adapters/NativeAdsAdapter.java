package com.mobiroller.core.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.mobiroller.core.helpers.SharedPrefHelper;
import com.mobiroller.core.R;

import java.util.Arrays;
import java.util.List;

import static com.google.android.gms.ads.formats.NativeAdOptions.ADCHOICES_TOP_RIGHT;


/**
 * Created by ealtaca on 22.05.2017.
 */

public class NativeAdsAdapter extends BaseAdapter {

    private static List<Integer> layoutYoutubeListHeight;

    private static List<Integer> layoutRssListHeight;

    public NativeAdsAdapter(Context context) {
        layoutYoutubeListHeight = Arrays.asList(0,
                context.getResources().getInteger(R.integer.youtube_layout_1),
                context.getResources().getInteger(R.integer.youtube_layout_1),
                context.getResources().getInteger(R.integer.youtube_layout_3),
                context.getResources().getInteger(R.integer.youtube_layout_4),
                context.getResources().getInteger(R.integer.youtube_layout_5),
                context.getResources().getInteger(R.integer.youtube_layout_6)
        );
        layoutRssListHeight = Arrays.asList(0,
                context.getResources().getInteger(R.integer.rss_layout_1),
                context.getResources().getInteger(R.integer.rss_layout_2),
                context.getResources().getInteger(R.integer.rss_layout_2),
                context.getResources().getInteger(R.integer.rss_layout_4),
                context.getResources().getInteger(R.integer.rss_layout_5),
                context.getResources().getInteger(R.integer.rss_layout_2),
                context.getResources().getInteger(R.integer.rss_layout_2),
                context.getResources().getInteger(R.integer.rss_layout_2),
                context.getResources().getInteger(R.integer.rss_layout_1),
                context.getResources().getInteger(R.integer.rss_layout_2)
        );
    }

    public static final String ADMOB_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110";


    class RecyclerViewRssAdMobAdItem extends RecyclerView.ViewHolder {


        RecyclerViewRssAdMobAdItem(final View v, final Activity activity, final int type, final SharedPrefHelper sharedPrefHelper) {
            super(v);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final FrameLayout frameLayout = (FrameLayout) v;
                    final float scale = activity.getResources().getDisplayMetrics().density;
                    int pixels = 0;
                    if (type != 10) {
                        pixels = (int) (layoutRssListHeight.get(type) * scale + 0.5f);
                        frameLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pixels));
                    } else {
                        frameLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    }
                    // Gets linearlayout
//                    frameLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pixels));
//                    AdLoader.Builder builder = new AdLoader.Builder(activity,ADMOB_AD_UNIT_ID);
                    AdLoader.Builder builder = new AdLoader.Builder(activity, sharedPrefHelper.getNativeAddUnitID());

                    View view_empty = activity.getLayoutInflater()
                            .inflate(getRssAdsLayout(type), null);
                    frameLayout.removeAllViews();
                    frameLayout.addView(view_empty);
                    if (type == 2 || type == 3 || type == 6 || type == 7 || type == 8 || type == 4)
                        builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
                            @Override
                            public void onAppInstallAdLoaded(NativeAppInstallAd nativeAppInstallAd) {
                                View view = activity.getLayoutInflater()
                                        .inflate(getRssAdsLayout(type), null);
                                bindAppInstall(nativeAppInstallAd, view);
                                frameLayout.setBackground(null);
                                frameLayout.removeAllViews();
                                frameLayout.addView(view);
                            }
                        });
                    else
                        builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
                            @Override
                            public void onContentAdLoaded(NativeContentAd ad) {
                                View view = activity.getLayoutInflater()
                                        .inflate(getRssAdsLayout(type), null);
                                bindContent(ad, view);
                                frameLayout.setBackground(null);
                                frameLayout.removeAllViews();
                                frameLayout.addView(view);
                            }
                        });


                    NativeAdOptions adOptions = new NativeAdOptions.Builder()
                            .setAdChoicesPlacement(ADCHOICES_TOP_RIGHT)
                            .build();

                    builder.withNativeAdOptions(adOptions);

                    AdLoader adLoader = builder.withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(int errorCode) {
//                mRefresh.setEnabled(true);
                            //Toast.makeText(activity, "Failed to load native ad: " + errorCode, Toast.LENGTH_SHORT).show();
                        }
                    }).build();


//                    adLoader.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("E7B0F5C9010533115E7E27B76FA46D21").build());
                    adLoader.loadAd(new AdRequest.Builder().build());
                }
            });
        }


    }

    class RecyclerViewRssFacebookAdItem extends RecyclerView.ViewHolder {


        RecyclerViewRssFacebookAdItem(final View v, final Activity activity, final int type, final SharedPrefHelper sharedPrefHelper) {
            super(v);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final FrameLayout frameLayout = (FrameLayout) v;
                    final float scale = activity.getResources().getDisplayMetrics().density;
                    int pixels = 0;
                    if (type != 10) {
                        pixels = (int) (layoutRssListHeight.get(type) * scale + 0.5f);
                        frameLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pixels));
                    } else {
                        frameLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    }
//                    final NativeAd nativeAd;
//
//                    nativeAd = new NativeAd(activity, sharedPrefHelper.getNativeAddUnitID());
//                    nativeAd.setAdListener(new com.facebook.ads.AdListener() {
//
//                        @Override
//                        public void onError(Ad ad, AdError error) {
//                            // Ad error callback
//                        }
//
//                        @Override
//                        public void onAdLoaded(Ad ad) {
//                            if (nativeAd != null) {
//                                nativeAd.unregisterView();
//                            }
//
//                            // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
//                            View adView = activity.getLayoutInflater()
//                                    .inflate(getRssFacebookAdsLayout(type), null);
//
//                            frameLayout.addView(adView);
//                            bindFacebookAds(nativeAd, frameLayout, activity);
//                        }
//
//                        @Override
//                        public void onAdClicked(Ad ad) {
//                            // Ad clicked callback
//                        }
//
//                        @Override
//                        public void onLoggingImpression(Ad ad) {
//                            // Ad impression logged callback
//                        }
//                    });
//
//                    // Request an ad
//                    nativeAd.loadAd();
                }
            });
        }
    }

    public class RecyclerViewYoutubeAdMobAdItem extends RecyclerView.ViewHolder {


        public RecyclerViewYoutubeAdMobAdItem(final View v, final Activity activity, final int layoutType, final SharedPrefHelper sharedPrefHelper) {
            super(v);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final FrameLayout frameLayout = (FrameLayout) v;
                    final float scale = activity.getResources().getDisplayMetrics().density;
                    int pixels = 0;
//
                    pixels = (int) (layoutYoutubeListHeight.get(layoutType) * scale + 0.5f);
                    frameLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pixels));
                    // Gets linearlayout
//                    AdLoader.Builder builder = new AdLoader.Builder(activity, ADMOB_AD_UNIT_ID);
                    AdLoader.Builder builder = new AdLoader.Builder(activity, sharedPrefHelper.getNativeAddUnitID());
//                    AdLoader.Builder builder = new AdLoader.Builder(activity,"");

                    View view_empty = activity.getLayoutInflater()
                            .inflate(getYoutubeAdsLayout(layoutType), null);
                    frameLayout.removeAllViews();
                    frameLayout.addView(view_empty);
                    if (layoutType == 2 || layoutType == 3 || layoutType == 1)
                        builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
                            @Override
                            public void onAppInstallAdLoaded(NativeAppInstallAd nativeAppInstallAd) {
                                View view = activity.getLayoutInflater()
                                        .inflate(getYoutubeAdsLayout(layoutType), null);
                                bindAppInstall(nativeAppInstallAd, view);
                                frameLayout.setBackground(null);
                                frameLayout.removeAllViews();
                                frameLayout.addView(view);
                            }
                        });
                    else
                        builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
                            @Override
                            public void onContentAdLoaded(NativeContentAd ad) {
                                View view = activity.getLayoutInflater()
                                        .inflate(getYoutubeAdsLayout(layoutType), null);
                                bindContent(ad, view);
                                frameLayout.setBackground(null);
                                frameLayout.removeAllViews();
                                frameLayout.addView(view);
                            }
                        });


                    NativeAdOptions adOptions = new NativeAdOptions.Builder()
                            .setAdChoicesPlacement(ADCHOICES_TOP_RIGHT)
                            .build();

                    builder.withNativeAdOptions(adOptions);

                    AdLoader adLoader = builder.withAdListener(new AdListener() {

                        @Override
                        public void onAdFailedToLoad(int errorCode) {
//                mRefresh.setEnabled(true);
                            //Toast.makeText(activity, "Failed to load native ad: " + errorCode, Toast.LENGTH_SHORT).show();
                        }
                    }).build();


//                    adLoader.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("E7B0F5C9010533115E7E27B76FA46D21").build());
                    adLoader.loadAd(new AdRequest.Builder().build());
                }
            });
        }

    }

    public class RecyclerViewFacebookAdItem extends RecyclerView.ViewHolder {


        public RecyclerViewFacebookAdItem(final View v, final Activity activity, final int layoutType, final SharedPrefHelper sharedPrefHelper) {
            super(v);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final FrameLayout frameLayout = (FrameLayout) v;
                    final float scale = activity.getResources().getDisplayMetrics().density;
                    int pixels = 0;
                    if (layoutType != 10) {
                        pixels = (int) (layoutYoutubeListHeight.get(layoutType) * scale + 0.5f);
                        frameLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pixels));
                    } else {
                        frameLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    }
//                    final NativeAd nativeAd;
//
//                    nativeAd = new NativeAd(activity, sharedPrefHelper.getNativeAddUnitID());
//                    nativeAd.setAdListener(new com.facebook.ads.AdListener() {
//
//                        @Override
//                        public void onError(Ad ad, AdError error) {
//                            // Ad error callback
//                        }
//
//                        @Override
//                        public void onAdLoaded(Ad ad) {
//                            if (nativeAd != null) {
//                                nativeAd.unregisterView();
//                            }
//
//                            // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
//                            View adView = activity.getLayoutInflater()
//                                    .inflate(getYoutubeFacebookAdsLayout(layoutType), null);
//
//                            frameLayout.addView(adView);
//                            bindFacebookAds(nativeAd, frameLayout, activity);
//                        }
//
//                        @Override
//                        public void onAdClicked(Ad ad) {
//                            // Ad clicked callback
//                        }
//
//                        @Override
//                        public void onLoggingImpression(Ad ad) {
//                            // Ad impression logged callback
//                        }
//                    });
//
//                    // Request an ad
//                    nativeAd.loadAd();
                }
            });
        }

    }

    private void bindContent(NativeContentAd nativeAd, View view) {
        TextView tvHeader;
        TextView tvDescription;
        ImageView ivLogo;
        ImageView ivImage;
        Button btnAction;

        NativeContentAd ad = nativeAd;

        NativeContentAdView adView = view.findViewById(R.id.content_ad_view);

        view.findViewById(R.id.empty_image_view).setVisibility(View.GONE);
        tvHeader = view.findViewById(R.id.tvHeader);
        tvDescription = view.findViewById(R.id.tvDescription);
        ivLogo = view.findViewById(R.id.ivLogo);
        btnAction = view.findViewById(R.id.btnAction);
        ivImage = view.findViewById(R.id.ivImage);
        // Locate the view that will hold the headline, set its text, and call the
        // NativeContentAdView's setHeadlineView method to register it.
        tvHeader.setText(ad.getHeadline());
        adView.setHeadlineView(tvHeader);
//
        tvDescription.setText(ad.getBody());
        adView.setBodyView(tvDescription);

        btnAction.setText(ad.getCallToAction());
        adView.setCallToActionView(btnAction);


//            if (ad.getImages() != null) {
//                ivImage.setImageDrawable(ad.getLogo().getDrawable());
//                ivImage.setVisibility(View.VISIBLE);
//            } else ivImage.setVisibility(View.GONE);
//            adView.setLogoView(ivImage);
        if (ad.getImages() != null && ad.getImages().size() > 0) {
            ivImage.setImageDrawable(ad.getImages().get(0).getDrawable());
            ivImage.setVisibility(View.VISIBLE);
        } else ivImage.setVisibility(View.GONE);
        adView.setImageView(ivImage);

        adView.setNativeAd(nativeAd);
//            v.addView(view);
    }

    private void bindAppInstall(NativeAppInstallAd nativeAd, View view) {

        TextView tvHeader;
        TextView tvDescription;
        ImageView ivLogo;
        ImageView ivImage;
        Button btnAction;
        NativeAppInstallAd ad = nativeAd;
        NativeAppInstallAdView adView = view.findViewById(R.id.app_install_ad_view);
        view.findViewById(R.id.empty_image_view).setVisibility(View.GONE);
        tvHeader = view.findViewById(R.id.tvHeader);
        tvDescription = view.findViewById(R.id.tvDescription);
        ivLogo = view.findViewById(R.id.ivLogo);
        btnAction = view.findViewById(R.id.btnAction);
        ivImage = view.findViewById(R.id.ivImage);
        // Locate the view that will hold the headline, set its text, and call the
        // NativeContentAdView's setHeadlineView method to register it.
        tvHeader.setText(ad.getHeadline());
        adView.setHeadlineView(tvHeader);

        tvDescription.setText(ad.getBody());
        adView.setBodyView(tvDescription);

        btnAction.setText(ad.getCallToAction());
        adView.setCallToActionView(btnAction);

        if (ad.getImages() != null && ad.getImages().size() > 0) {
            ivLogo.setImageDrawable(ad.getImages().get(0).getDrawable());
            ivLogo.setVisibility(View.VISIBLE);
        } else ivLogo.setVisibility(View.GONE);
        adView.setImageView(ivLogo);

        if (ad.getIcon() != null) {
            ivImage.setImageDrawable(ad.getIcon().getDrawable());
            ivImage.setVisibility(View.VISIBLE);
        } else ivImage.setVisibility(View.GONE);
        adView.setIconView(ivImage);

        adView.setNativeAd(nativeAd);
//            v.addView(view);
    }


//    private void bindFacebookAds(NativeAd nativeAd, View view, Context context) {
//        TextView tvHeader;
//        TextView tvDescription;
//        ImageView ivLogo;
//        ImageView ivImage;
//        Button btnAction;
//        NativeAd ad = nativeAd;
//        view.findViewById(R.id.empty_image_view).setVisibility(View.GONE);
//        tvHeader = view.findViewById(R.id.tvHeader);
//        tvDescription = view.findViewById(R.id.tvDescription);
//        ivLogo = view.findViewById(R.id.ivLogo);
//        btnAction = view.findViewById(R.id.btnAction);
//        ivImage = view.findViewById(R.id.ivImage);
//        LinearLayout adChoicesContainer = view.findViewById(R.id.ad_choices_container);
//
//        tvHeader.setText(ad.getAdTitle());
//
//        tvDescription.setText(ad.getAdBody());
//
//        btnAction.setText(ad.getAdCallToAction());
//
//
//        NativeAd.Image adIcon = nativeAd.getAdIcon();
//        NativeAd.downloadAndDisplayImage(adIcon, ivImage);
//
//        NativeAd.Image adCover = nativeAd.getAdCoverImage();
//        NativeAd.downloadAndDisplayImage(adCover, ivLogo);
//
//        nativeAd.getAdCoverImage();
//        AdChoicesView adChoicesView = new AdChoicesView(context, nativeAd, true);
//        adChoicesContainer.addView(adChoicesView);
//
//        List<View> clickableViews = new ArrayList<>();
//        clickableViews.add(tvHeader);
//        clickableViews.add(btnAction);
//        clickableViews.add(ivLogo);
//        clickableViews.add(ivImage);
//        nativeAd.registerViewForInteraction(view, clickableViews);
//    }


    private int getRssFacebookAdsLayout(int type) {
        switch (type) {
            case 2:
            case 3:
            case 6:
            case 7:
            case 8:
                return R.layout.rss_ads_facebook_list_item_classic;
            case 5:
                return R.layout.rss_ads_facebook_list_item_square_featured;
            case 10:
                return R.layout.rss_ads_facebook_list_item_stragged;
            case 4:
                return R.layout.rss_ads_facebook_app_install_list_item_square_featured;
            case 1:
            case 9:
                return R.layout.rss_ads_facebook_list_item_full_image;
        }
        return 0;
    }
    private int getYoutubeFacebookAdsLayout(int layoutType) {
        switch (layoutType) {
            case 1:
            case 2:
                return R.layout.video_facebook_ads_app_install_list_item_classic;
            case 4:
                return R.layout.video_facebook_ads_content_list_item_type_3;
            case 5:
                return R.layout.video_facebook_ads_content_list_item_type_5;
            case 6:
                return R.layout.video_facebook_ads_content_list_item_full_image;
            case 3:
                return R.layout.rss_ads_facebook_app_install_list_item_square_featured;
        }
        return 0;
    }


    private int getYoutubeAdsLayout(int layoutType) {
        switch (layoutType) {
            case 1:
            case 2:
                return R.layout.video_ads_app_install_list_item_classic;
            case 4:
                return R.layout.video_ads_content_list_item_type_3;
            case 5:
                return R.layout.video_ads_content_list_item_type_5;
            case 6:
                return R.layout.video_ads_content_list_item_full_image;
            case 3:
                return R.layout.rss_ads_app_install_list_item_square_featured;
        }
        return 0;
    }

    private int getRssAdsLayout(int type) {
        switch (type) {
            case 2:
            case 3:
            case 6:
            case 7:
            case 8:
                return R.layout.rss_ads_app_install_list_item_classic;
            case 5:
                return R.layout.rss_ads_content_list_item_square_featured;
            case 10:
                return R.layout.rss_ads_content_list_item_stragged;
            case 4:
                return R.layout.rss_ads_app_install_list_item_square_featured;
            case 1:
            case 9:
                return R.layout.rss_ads_content_list_item_full_image;
        }
        return 0;
    }

}
