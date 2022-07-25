package com.mobiroller.core.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.SharedElementCallback;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.mobiroller.core.R2;
import com.mobiroller.core.constants.DynamicConstants;
import com.mobiroller.core.activities.base.AveActivity;
import com.mobiroller.core.coreui.Theme;
import com.mobiroller.core.enums.FontStyle;
import com.mobiroller.core.helpers.FavoriteHelper;
import com.mobiroller.core.helpers.FontSizeHelper;
import com.mobiroller.core.helpers.NetworkHelper;
import com.mobiroller.core.helpers.SharedPrefHelper;
import com.mobiroller.core.helpers.LegacyToolbarHelper;
import com.mobiroller.core.interfaces.ActivityComponent;
import com.mobiroller.core.interfaces.bottomsheet.ActionListener;
import com.mobiroller.core.models.RssFeaturedHeaderModel;
import com.mobiroller.core.models.RssModel;
import com.mobiroller.core.models.RssSliderHeaderModel;
import com.mobiroller.core.models.bottomsheet.ActionModel;
import com.mobiroller.core.R;
import com.mobiroller.core.util.ShareUtil;
import com.mobiroller.core.views.ActionPickerBottomSheet;
import com.mobiroller.core.views.CustomHorizontalScrollView;
import com.mobiroller.core.views.VideoEnabledWebViewWithTouch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mobiroller.core.constants.Constants.KEY_RSS_LAYOUT_TYPE;
import static com.mobiroller.core.constants.Constants.KEY_RSS_TITLE;

/**
 * Created by ealtaca on 06.02.2017.
 */

public class aveRssContentViewPager extends AveActivity implements ActionListener {

    @BindView(R2.id.viewPager)
    ViewPager viewPager;
    @BindView(R2.id.rss_content_rel_layout)
    RelativeLayout rss_content_rel_layout;
    /**
     * Sayfa scroll edildiğinde Parallax efekti vermek için kullanılır.
     * Sayfanın offset durumuyla ilgilenir.
     */
    private int mLastDampedScroll;

    private View mCurrentView = null;
    public static List<Object> rssModelList;
    private static RssContentPagerAdapter rssContentPagerAdapter;
    private int currentPosition;
    private int layoutType = 9;
    private NativeContentAdView adView;
    private FavoriteHelper favoriteHelper;
    private ActionPickerBottomSheet actionPickerBottomSheet;

    @Inject
    NetworkHelper networkHelper;
    @Inject
    SharedPrefHelper sharedPrefHelper;
    @Inject
    LegacyToolbarHelper toolbarHelper;

    public static void setRssModelList(List<Object> objectList) {
        if (objectList != null && objectList.size() > 0) {
            ArrayList<Object> newDataList = new ArrayList<>(objectList);
            if (newDataList.get(0) instanceof RssSliderHeaderModel) {
                newDataList.addAll(0, ((RssSliderHeaderModel) newDataList.get(0)).getDataList());
                newDataList.remove(5);
            } else if (newDataList.get(0) instanceof RssFeaturedHeaderModel) {
                newDataList.add(0, ((RssFeaturedHeaderModel) newDataList.get(0)).getFeaturedHeader());
                newDataList.remove(1);
            }
            rssModelList = newDataList;
        }
    }

    private final SharedElementCallback mCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (mCurrentView != null) {
                ImageView sharedElement = mCurrentView.findViewById(R.id.rss_content_image);
                if (names.get(0).equalsIgnoreCase("rssImage"))
                    sharedElements.put("rssImage", sharedElement);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_content_view_pager);
        ButterKnife.bind(this);
        favoriteHelper = new FavoriteHelper(this);
        if (sharedPrefHelper.getNativeAddUnitID().startsWith("ca-"))
            loadAds();
        int pagerPosition = getIntent().getIntExtra("position", 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setEnterSharedElementCallback(mCallback);

        setMobirollerToolbar(findViewById(R.id.toolbar_top));
        if (getIntent().hasExtra(KEY_RSS_TITLE)) {
            if (getIntent().getStringExtra(KEY_RSS_TITLE) != null)
                toolbarHelper.setToolbarTitle(this, Html.fromHtml(getIntent().getStringExtra(KEY_RSS_TITLE)).toString());
            else
                toolbarHelper.setToolbarTitle(this, "");
        } else
            toolbarHelper.setToolbarTitle(this, "");
        if (getIntent().hasExtra(KEY_RSS_LAYOUT_TYPE))
            layoutType = getIntent().getIntExtra(KEY_RSS_LAYOUT_TYPE, 9);

        rssContentPagerAdapter = new RssContentPagerAdapter(rssModelList);
        viewPager.setAdapter(rssContentPagerAdapter);
        viewPager.setCurrentItem(pagerPosition);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (rssModelList.get(position) instanceof RssModel)
                    toolbarHelper.setToolbarTitle(aveRssContentViewPager.this,
                            Html.fromHtml(((RssModel) rssModelList.get(position)).getTitle()).toString());
                else
                    toolbarHelper.setToolbarTitle(aveRssContentViewPager.this,
                            getString(R.string.advertisement));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mLastDampedScroll = 0;
        if (getSupportActionBar() != null) {
            Toolbar myToolbar = findViewById(R.id.toolbar_top);
            myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public AppCompatActivity injectActivity(ActivityComponent component) {
        component.inject(this);
        return this;
    }

    class RssContentPagerAdapter extends PagerAdapter {

        private List<Object> rssModelList;
        LayoutInflater inflater;
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");

        RssContentPagerAdapter(List<Object> rssModelList) {
            inflater = (LayoutInflater) aveRssContentViewPager.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.rssModelList = rssModelList;

        }

        @Override
        public int getCount() {
            return rssModelList.size();
        }

        @Override
        public Object instantiateItem(final ViewGroup view, int position) {
            if (rssModelList.get(position) instanceof RssModel) {
                final RssModel rssModel = (RssModel) rssModelList.get(position);
                View imageLayout = inflater.inflate(R.layout.rss_content, view, false);
                VideoEnabledWebViewWithTouch contentDescription = imageLayout.findViewById(R.id.rss_content_text);
                TextView contentTime = imageLayout.findViewById(R.id.rss_content_time);
                TextView contentTitle = imageLayout.findViewById(R.id.rss_content_title);
                final ImageView rssImage = imageLayout.findViewById(R.id.rss_content_image);
                /**
                 * Allow transition for only current item to avoid animation error
                 * Disable all other transitions
                 */
                if (viewPager.getCurrentItem() != position) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rssImage.setTransitionName("dummy");
                    }
                }
                CustomHorizontalScrollView scrollView = imageLayout.findViewById(R.id.rss_content_scroll_view);
                //Scroll durumunu handle eder, parallax ve gerekirse toolbar alpha değerlerini düzenler
                scrollView.setOnScrollChangedListener(new CustomHorizontalScrollView.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged(int l, int t, int oldl, int oldt) {
                        try {
                            updateParallaxEffect(t, rssImage);
                        } catch (Exception e) {
                            // Scroll change handle error
                        }
                    }
                });
                setEnterSharedElementCallback(new SharedElementCallback() {
                    @Override
                    public void onMapSharedElements(List<String> names,
                                                    Map<String, View> sharedElements) {
                        if (names.get(0).equalsIgnoreCase("businessImage"))
                            sharedElements.put("businessImage", rssImage);
                    }
                });
                Button contentMore = imageLayout.findViewById(R.id.rss_content_more_button);
                contentTitle.setText(Html.fromHtml(rssModel.getTitle()));
                if (rssModel.getPubDate() != null) {
                    try {
                        contentTime.setText(dateFormat.format(rssModel.getPubDate()));
                    } catch (Exception e) {
                        // Date format exception (probably broken date data received)
                        contentTime.setText("-");
                    }
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setEnterSharedElementCallback(new SharedElementCallback() {
                        @Override
                        public void onMapSharedElements(List<String> names,
                                                        Map<String, View> sharedElements) {
                            if (names.get(0).equalsIgnoreCase("businessImage"))
                                sharedElements.put("businessImage", rssImage);
                        }
                    });
                }
                if (rssModel.getImage() != null && !rssModel.getImage().equalsIgnoreCase("")) {
                    Glide.with(aveRssContentViewPager.this)
                            .load(rssModel.getImage())
                            .apply(new RequestOptions().placeholder(R.drawable.no_image))
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    rssImage.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(rssImage);
                } else
                    rssImage.setVisibility(View.GONE);
                contentTitle.setText(Html.fromHtml(rssModel.getTitle()));
                if (rssModel.getPubDate() != null) {
                    try {
                        contentTime.setText(dateFormat.format(rssModel.getPubDate()));
                    } catch (Exception e) {
                        // Date format exception (probably broken date data received)
                        contentTime.setText("-");
                    }
                } else
                    contentTime.setText("-");
                String summary = "<html><style>img{display: inline;height: auto;max-width: 100%;} iframe{display: inline;height: auto;max-width: 100%;}" +
                        " body{ background-color: #ffffff; } </style><body>" + rssModel.getDescription() + "</body></html>";

                contentDescription.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        try {
                            if (URLUtil.isValidUrl(url)) {
                                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                                builder.setToolbarColor(Theme.primaryColor);
                                builder.setShowTitle(true);
                                builder.addDefaultShareMenuItem();
                                CustomTabsIntent customTabsIntent = builder.build();
                                customTabsIntent.launchUrl(aveRssContentViewPager.this, Uri.parse(url));
                                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            if (URLUtil.isValidUrl(url)) {
                                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                                builder.setToolbarColor(Theme.primaryColor);
                                builder.setShowTitle(true);
                                builder.addDefaultShareMenuItem();
                                CustomTabsIntent customTabsIntent = builder.build();
                                customTabsIntent.launchUrl(aveRssContentViewPager.this, Uri.parse(url));
                            }
                        }
                        return true;
                    }
                });
//                contentDescription.getSettings().setUseWideViewPort(true);
                contentDescription.getSettings().setLoadWithOverviewMode(true);
                // pinch to zoom ,not display zoom controllers
                contentDescription.getSettings().setBuiltInZoomControls(true);
                contentDescription.getSettings().setDisplayZoomControls(false);
                contentDescription.loadDataWithBaseURL("blarg://ignored", summary, "text/html", "utf-8", "");
                contentMore.setText(getString(R.string.action_more));
                contentMore.setAllCaps(true);
                contentMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                            builder.setToolbarColor(Theme.primaryColor);
                            builder.setShowTitle(true);
                            builder.addDefaultShareMenuItem();
                            CustomTabsIntent customTabsIntent = builder.build();
                            customTabsIntent.launchUrl(aveRssContentViewPager.this, Uri.parse(rssModel.getLink()));
                            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (URLUtil.isValidUrl(rssModel.getLink())) {
                                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                                builder.addDefaultShareMenuItem();
                                builder.setToolbarColor(Theme.primaryColor);
                                CustomTabsIntent customTabsIntent = builder.build();
                                customTabsIntent.launchUrl(aveRssContentViewPager.this, Uri.parse(rssModel.getLink()));
                            }

                        }
                    }
                });
                view.addView(imageLayout, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
                return imageLayout;
            } else {
                if (sharedPrefHelper.getNativeAddUnitID().startsWith("ca-")) {
                    view.addView(adView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
                    return adView;
                } else {
                    final View rssContentFacebook = inflater.inflate(R.layout.rss_content_facebook, view, false);
                    view.addView(rssContentFacebook, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
                    return rssContentFacebook;
                }
            }

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            invalidateOptionsMenu();
            mCurrentView = (View) object;
            currentPosition = position;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (getToolbar() != null && getToolbar().getMenu() != null)
            getToolbar().getMenu().clear();
        if (rssModelList.get(currentPosition) instanceof RssModel) {
            RssModel rssModel = (RssModel) rssModelList.get(currentPosition);
            // Inflate the menu; this adds items to the action bar if it is present.
            if (rssModel.getLink() != null && !rssModel.getLink().equalsIgnoreCase(""))
                getToolbar().inflateMenu(R.menu.rss_content_menu);

//            menu.findItem(R.id.a_More).setVisible(true);
            if (!sharedPrefHelper.isFavoriteActive()) {
                getToolbar().getMenu().findItem(R.id.action_favorite).setVisible(false);
//                menu.findItem(R.id.a_More).setVisible(false);
            }
            if (favoriteHelper.isRssContentAddedToList(rssModel)) {
                getToolbar().getMenu().findItem(R.id.action_favorite).setIcon(R.drawable.ic_bookmark_white_24dp);
            }

        } else
            getToolbar().inflateMenu(R.menu.empty_menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_favorite) {
            RssModel rssModel = (RssModel) rssModelList.get(currentPosition);
            if (favoriteHelper.isRssContentAddedToList(rssModel)) {
                favoriteHelper.removeRssContent(rssModel);
            } else
                favoriteHelper.addRssContentToList(rssModel);
            invalidateOptionsMenu();
        } else if (item.getItemId() == R.id.a_More) {
            actionPickerBottomSheet = new ActionPickerBottomSheet.Builder()
                    .setActionListener(this)
                    .setActions(getActionList())
                    .show(getSupportFragmentManager(), "Tag");

        }
        return super.onOptionsItemSelected(item);
    }

    private List<ActionModel> getActionList() {
        List<ActionModel> actionModels = new ArrayList<>();
        actionModels.add(new ActionModel(SHARE_ID, R.drawable.ic_share_white_24dp, R.string.action_share, true));
        actionModels.add(new ActionModel(FONT_ID, R.drawable.ic_format_size_white_24dp, R.string.content_change_font_size, true));
        return actionModels;
    }

    /**
     * Parallax efekti devreye sokar.
     *
     * @param scrollPosition
     */
    private void updateParallaxEffect(int scrollPosition, ImageView rssImage) {
        float damping = 0.5f;
        int dampedScroll = (int) (scrollPosition * damping);
        int offset = mLastDampedScroll - dampedScroll;
        rssImage.offsetTopAndBottom(-offset);
        mLastDampedScroll = dampedScroll;
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && layoutType != 10 && layoutType != 8)
            this.supportFinishAfterTransition();
        else
            finish();
    }

    public static void notifyAdapter() {
        if (rssContentPagerAdapter != null)
            rssContentPagerAdapter.notifyDataSetChanged();
    }

    public void bind(NativeContentAd nativeAd, NativeContentAdView nativeAdView) {
        TextView tvHeader;
        TextView tvDescription;
        ImageView ivLogo;
        ImageView ivImage;
        Button btnAction;
        TextView tvAdvertiser;

        tvHeader = nativeAdView.findViewById(R.id.tvHeader);
        tvDescription = nativeAdView.findViewById(R.id.tvDescription);
        ivLogo = nativeAdView.findViewById(R.id.ivLogo);
        btnAction = nativeAdView.findViewById(R.id.btnAction);
        tvAdvertiser = nativeAdView.findViewById(R.id.tvAdvertiser);
        ivImage = nativeAdView.findViewById(R.id.ivImage);
        NativeContentAd ad = nativeAd;
        NativeContentAdView adView = nativeAdView;

        // Locate the view that will hold the headline, set its text, and call the
        // NativeContentAdView's setHeadlineView method to register it.
        tvHeader.setText(ad.getHeadline());
        adView.setHeadlineView(tvHeader);

        tvDescription.setText(ad.getBody());
        adView.setBodyView(tvDescription);

        if (ad.getLogo() != null)
            ivLogo.setImageDrawable(ad.getLogo().getDrawable());
        adView.setLogoView(ivLogo);

        btnAction.setText(ad.getCallToAction());
        adView.setCallToActionView(btnAction);

        tvAdvertiser.setText(ad.getAdvertiser());
        adView.setAdvertiserView(tvAdvertiser);

        if (ad.getImages() != null && ad.getImages().size() > 0) {
            ivImage.setImageDrawable(ad.getImages().get(0).getDrawable());
            ivImage.setVisibility(View.VISIBLE);
        } else ivImage.setVisibility(View.GONE);
        adView.setImageView(ivImage);
        nativeAdView.setNativeAd(nativeAd);
    }

    public void loadAds() {

        AdLoader.Builder builder = new AdLoader.Builder(aveRssContentViewPager.this, sharedPrefHelper.getNativeAddUnitID());
        adView = (NativeContentAdView) getLayoutInflater()
                .inflate(R.layout.ad_content, null);
        builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
            @Override
            public void onContentAdLoaded(NativeContentAd ad) {

                bind(ad, adView);
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {

            @Override
            public void onAdFailedToLoad(int errorCode) {
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void contentTextSizeLayoutOnClick() {
        FontSizeHelper fontSizeHelper = new FontSizeHelper(this);
        int contentTextSizeOrder = fontSizeHelper.getContentFontOrder();
        if (contentTextSizeOrder == -1)
            contentTextSizeOrder = 1;
        final int finalContentTextSizeOrder = contentTextSizeOrder;
        new MaterialDialog.Builder(this)
                .title(R.string.content_change_font_size)
                .items(getFontSizeList())
                .itemsCallbackSingleChoice(finalContentTextSizeOrder, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, final int which, CharSequence text) {
                        if (which != finalContentTextSizeOrder) {
                            if (!DynamicConstants.MobiRoller_Stage) {
                                saveContentFontSelectedFontSize(which);
                                restartActivity();
                            } else {
                                Toast.makeText(aveRssContentViewPager.this, getString(R.string.not_supported_on_preview),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                    }
                })
                .negativeText(R.string.cancel)
                .positiveText(R.string.OK)
                .show();
    }

    public void saveContentFontSelectedFontSize(int selectedItemOrder) {
        FontSizeHelper fontSizeHelper = new FontSizeHelper(this);
        if (selectedItemOrder == 0)
            fontSizeHelper.setContentFontStyle(FontStyle.Small);
        else if (selectedItemOrder == 1)
            fontSizeHelper.setContentFontStyle(FontStyle.Medium);
        else if (selectedItemOrder == 2)
            fontSizeHelper.setContentFontStyle(FontStyle.Large);
        else if (selectedItemOrder == 3)
            fontSizeHelper.setContentFontStyle(FontStyle.XLarge);
    }

    private String[] getFontSizeList() {
        return getResources().getStringArray(R.array.text_size_list);
    }

    private void restartActivity() {
        getIntent().putExtra("position", currentPosition);
        recreate();
    }

    private static int SHARE_ID = 12;
    private static int FONT_ID = 13;

    @Override
    public void actionSelected(int position, ActionModel actionModel) {
        if (actionModel.id == SHARE_ID) {
            if (rssModelList.get(currentPosition) instanceof RssModel) {
                RssModel rssModel = (RssModel) rssModelList.get(currentPosition);
                ShareUtil.shareURL(aveRssContentViewPager.this, Html.fromHtml(rssModel.getTitle()).toString(), rssModel.getLink());
            }
        } else if (actionModel.id == FONT_ID) {
            contentTextSizeLayoutOnClick();
        }

        if (actionPickerBottomSheet != null)
            actionPickerBottomSheet.dismiss();
    }

}
