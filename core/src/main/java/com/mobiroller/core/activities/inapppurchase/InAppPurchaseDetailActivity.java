package com.mobiroller.core.activities.inapppurchase;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.anjlab.android.iab.v3.SkuDetails;
import com.mobiroller.core.R2;
import com.mobiroller.core.activities.ActivityHandler;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.helpers.InAppPurchaseHelper;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.helpers.SharedPrefHelper;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.models.InAppPurchaseProduct;
import com.mobiroller.core.models.InAppPurchaseProductImageModel;
import com.mobiroller.core.models.events.InAppPurchaseSuccessEvent;
import com.mobiroller.core.R;
import com.mobiroller.core.util.ImageManager;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mobiroller.core.activities.inapppurchase.InAppPurchaseActivity.setWindowFlag;
import static com.mobiroller.core.helpers.InAppPurchaseHelper.getPeriodString;

public class InAppPurchaseDetailActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler, BillingProcessor.ISkuDetailsResponseListener {

    public final static String INTENT_EXTRA_IN_APP_PURCHASE_PRODUCT = "inAppPurchaseProduct";

    private InAppPurchaseProduct mProduct;
    private String mScreenId;
    private String mScreenType;
    private String mSubScreenType;
    private SharedPrefHelper mSharedPref;

    @BindView(R2.id.main_layout)
    LinearLayout mainLayout;

    @BindView(R2.id.view_pager)
    ViewPager viewPager;

    @BindView(R2.id.description)
    TextView description;
    @BindView(R2.id.title)
    TextView title;

    @BindView(R2.id.one_time_button)
    ImageView oneTimeButton;
    @BindView(R2.id.life_time_price_text)
    AppCompatTextView lifeTimePriceText;
    @BindView(R2.id.subscription_button)
    ImageView subscriptionButton;
    @BindView(R2.id.subscription_period)
    AppCompatTextView subscriptionPeriod;
    @BindView(R2.id.subscription_price_text)
    AppCompatTextView subscriptionPriceText;

    @BindView(R2.id.detail_action_button)
    Button detailActionButton;

    @BindView(R2.id.back_button)
    ImageView backButton;

    @BindView(R2.id.buy_action_button)
    Button buyActionButton;
    @BindView(R2.id.one_time_layout)
    RelativeLayout oneTimeLayout;
    @BindView(R2.id.subscription_layout)
    RelativeLayout subscriptionLayout;

    @BindView(R2.id.viewPagerCountDots)
    LinearLayout pagerIndicator;

    private BillingProcessor mBillingProcessor;
    private boolean mIsFromActivity;
    private LocalizationHelper mLocalizationHelper;

    private boolean isLifeTimeSelected = false;
    private boolean isSubscriptionSelected = false;

    private int dotsCount;
    private ImageView[] dots;
    private YouTubePlayerView youtubePlayerView;
    private YouTubePlayer initializedYouTubePlayer;
    private SkuDetails mSubscribeSkuDetails;
    private SkuDetails mLifeTimeSkuDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_purchase_detail);
        ButterKnife.bind(this);
        mSharedPref = UtilManager.sharedPrefHelper();
        mLocalizationHelper = UtilManager.localizationHelper();
        if (getIntent().hasExtra(INTENT_EXTRA_IN_APP_PURCHASE_PRODUCT))
            mProduct = (InAppPurchaseProduct) getIntent().getSerializableExtra(INTENT_EXTRA_IN_APP_PURCHASE_PRODUCT);
        if (getIntent().hasExtra(Constants.KEY_SCREEN_ID))
            mScreenId = getIntent().getStringExtra(Constants.KEY_SCREEN_ID);
        if (getIntent().hasExtra(Constants.KEY_SCREEN_TYPE))
            mScreenType = getIntent().getStringExtra(Constants.KEY_SCREEN_TYPE);
        if (getIntent().hasExtra(Constants.KEY_SUB_SCREEN_TYPE))
            mSubScreenType = getIntent().getStringExtra(Constants.KEY_SUB_SCREEN_TYPE);
        if (getIntent().hasExtra(Constants.KEY_IS_FROM_ACTIVITY))
            mIsFromActivity = true;
        loadUi();
        mBillingProcessor = new BillingProcessor(this, InAppPurchaseHelper.getInAppPurchaseLicenseKey(), this);
//        mBillingProcessor.connect(this);
        mBillingProcessor.initialize();
        mBillingProcessor.loadOwnedPurchasesFromGoogleAsync(new BillingProcessor.IPurchasesResponseListener() {
            @Override
            public void onPurchasesSuccess() {

            }

            @Override
            public void onPurchasesError() {

            }
        });

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    @OnClick(R2.id.back_button)
    public void onBackButtonClick() {
        finish();
    }

    private String getYouTubeId(String youTubeUrl) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youTubeUrl);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "error";
        }
    }

    private void loadUi() {
        String videoId = null;
        if (mProduct.videoUrl != null)
            videoId = getYouTubeId(mLocalizationHelper.getLocalizedTitle(mProduct.videoUrl));
        CustomPagerAdapter adapter = new CustomPagerAdapter(this, mProduct.productImages, videoId);
        viewPager.setAdapter(adapter);
        if (mProduct.description != null)
            description.setText(mLocalizationHelper.getLocalizedTitle(mProduct.description));
        if (mProduct.title != null)
            title.setText(mLocalizationHelper.getLocalizedTitle(mProduct.title));
        if (mProduct.detailActionText != null)
            detailActionButton.setText(mLocalizationHelper.getLocalizedTitle(mProduct.detailActionText));
        if (mProduct.detailActionUrl == null) {
            detailActionButton.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) buyActionButton.getLayoutParams();
            params.setMargins(0, 0, 0, 0); //substitute parameters for left, top, right, bottom
            buyActionButton.setLayoutParams(params);
        }
        if (mProduct.buyActionText != null)
            buyActionButton.setText(mLocalizationHelper.getLocalizedTitle(mProduct.buyActionText));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotsCount; i++) {
                    Drawable dot = ContextCompat.getDrawable(InAppPurchaseDetailActivity.this, R.drawable.in_app_purchase_nonselecteditem_dot);
                    dots[i].setImageDrawable(dot);
                }

                Drawable dot = ContextCompat.getDrawable(InAppPurchaseDetailActivity.this, R.drawable.selecteditem_dot);
                dot.setColorFilter(new
                        PorterDuffColorFilter(mSharedPref.getActionBarColor(), PorterDuff.Mode.MULTIPLY));
                dots[position].setImageDrawable(dot);

                if (initializedYouTubePlayer != null)
                    initializedYouTubePlayer.pause();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(10);
        dotsCount = adapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(InAppPurchaseDetailActivity.this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(InAppPurchaseDetailActivity.this, R.drawable.in_app_purchase_nonselecteditem_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pagerIndicator.addView(dots[i], params);
        }

        Drawable dot = ContextCompat.getDrawable(InAppPurchaseDetailActivity.this, R.drawable.selecteditem_dot);
        dot.setColorFilter(new
                PorterDuffColorFilter(mSharedPref.getActionBarColor(), PorterDuff.Mode.MULTIPLY));
        dots[0].setImageDrawable(dot);
    }

    @OnClick(R2.id.detail_action_button)
    public void onClickDetailButton() {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(mSharedPref.getActionBarColor());
        builder.setShowTitle(true);
        builder.addDefaultShareMenuItem();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(mLocalizationHelper.getLocalizedTitle(mProduct.detailActionUrl)));
    }

    @OnClick(R2.id.buy_action_button)
    public void onClickBuyButton() {
        if (isLifeTimeSelected && InAppPurchaseHelper.checkIabAvailable(this)) {
            mBillingProcessor.purchase(this, mProduct.oneTimeProductId);
        } else if (isSubscriptionSelected && InAppPurchaseHelper.checkIabAvailable(this)) {
            mBillingProcessor.subscribe(this, mProduct.subscriptionProductId[0]);
        }
    }

    @OnClick(R2.id.subscription_button)
    public void onClickSubscriptionButton() {
        isSubscriptionSelected = true;
        isLifeTimeSelected = false;
        setPackageBackground();
    }

    @OnClick(R2.id.one_time_button)
    public void onClickOneTimeButton() {
        isSubscriptionSelected = false;
        isLifeTimeSelected = true;
        setPackageBackground();
    }

//    @Override
//    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
//        if (((mProduct.oneTimeProductId != null && productId.equalsIgnoreCase(mProduct.oneTimeProductId))
//                || (mProduct.subscriptionProductId.length != 0 && productId.equalsIgnoreCase(mProduct.subscriptionProductId[0])))
//                && InAppPurchaseHelper.isScreenPurchased(mBillingProcessor,mScreenId)) {
//            verifyPurchase(details.purchaseInfo.responseData, details.purchaseInfo.signature);
//        } else {
//            Toast.makeText(InAppPurchaseDetailActivity.this, getString(R.string.common_error), Toast.LENGTH_SHORT).show();
//        }
//    }

    private void verifyPurchase(String jsonResponse, String signature) {
        onProductPurchased();
    }

    private void onProductPurchased() {
        if (mIsFromActivity) {
            ActivityHandler.startActivity(this, mScreenId, mScreenType, mSubScreenType, null, null);
        }
        finish();
        EventBus.getDefault().post(new InAppPurchaseSuccessEvent(mScreenId, null, mIsFromActivity));
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo details) {

        if (((mProduct.oneTimeProductId != null && productId.equalsIgnoreCase(mProduct.oneTimeProductId))
                || (mProduct.subscriptionProductId.length != 0 && productId.equalsIgnoreCase(mProduct.subscriptionProductId[0])))
                && InAppPurchaseHelper.isScreenPurchased(mBillingProcessor,mScreenId)) {
            verifyPurchase(details.responseData, details.signature);
        } else {
            Toast.makeText(InAppPurchaseDetailActivity.this, getString(R.string.common_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
//        Log.e("error", " " + errorCode + " " + error.getLocalizedMessage());
    }

    @Override
    public void onBillingInitialized() {
        if (mProduct.oneTimeProductId != null && !mProduct.oneTimeProductId.isEmpty()) {
            mBillingProcessor.getPurchaseListingDetailsAsync(mProduct.oneTimeProductId ,this);
        }

        if (mProduct.subscriptionProductId != null && mProduct.subscriptionProductId.length != 0) {
            mBillingProcessor.getSubscriptionListingDetailsAsync(mProduct.subscriptionProductId[0], this);
        }
    }

    private void setPackageBackground() {
        if (isSubscriptionSelected) {
            subscriptionButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.in_app_purchase_plan_selected));
            oneTimeButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.in_app_purchase_plan_unselected));
        } else {
            subscriptionButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.in_app_purchase_plan_unselected));
            oneTimeButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.in_app_purchase_plan_selected));
        }
    }

    @Override
    public void onSkuDetailsResponse(@Nullable List<SkuDetails> products) {
        if (products == null || products.size() == 0) {
            return;
        }
        SkuDetails skuDetails = products.get(0);
        runOnUiThread(() -> {
            if (skuDetails != null && skuDetails.productId != null) {
                if (mProduct.subscriptionProductId != null && mProduct.subscriptionProductId.length > 0 && skuDetails.productId.equals(mProduct.subscriptionProductId[0])) {
                    mSubscribeSkuDetails = skuDetails;
                    if (mSubscribeSkuDetails != null) {
                        subscriptionPeriod.setText(getPeriodString(InAppPurchaseDetailActivity.this, mSubscribeSkuDetails.subscriptionPeriod));
                        subscriptionPriceText.setText(Utils.getCurrencySymbol(mSubscribeSkuDetails.currency) + mSubscribeSkuDetails.priceValue);

                        isSubscriptionSelected = true;
                        subscriptionLayout.setVisibility(View.VISIBLE);
                    } else {
                        subscriptionLayout.setVisibility(View.GONE);
                    }
                } else if (skuDetails.productId.equals(mProduct.oneTimeProductId)) {
                    mLifeTimeSkuDetails = skuDetails;
                    if (mLifeTimeSkuDetails != null) {
                        lifeTimePriceText.setText(Utils.getCurrencySymbol(mLifeTimeSkuDetails.currency) + mLifeTimeSkuDetails.priceValue);
                        isLifeTimeSelected = true;
                        oneTimeLayout.setVisibility(View.VISIBLE);
                    } else {
                        oneTimeLayout.setVisibility(View.GONE);
                    }
                }
                setPackageBackground();
            }
        });
    }

    @Override
    public void onSkuDetailsError(String error) {

    }

    public class CustomPagerAdapter extends PagerAdapter {

        private Context mContext;
        private List<Object> mDataList;
        private boolean isVideoActive;

        public CustomPagerAdapter(Context context, List<InAppPurchaseProductImageModel> dataList, String videoUrl) {
            mContext = context;
            mDataList = new ArrayList<>();
            if (videoUrl != null) {
                mDataList.add(0, videoUrl);
                isVideoActive = true;
            }
            mDataList.addAll(dataList);
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            if (position == 0 && isVideoActive) {
                ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.layout_in_app_purchase_video_detail_view_pager_item, collection, false);
                youtubePlayerView = layout.findViewById(R.id.video_view);
                youtubePlayerView.getPlayerUIController().showCurrentTime(false);
                youtubePlayerView.getPlayerUIController().showSeekBar(false);
                youtubePlayerView.getPlayerUIController().showYouTubeButton(false);
                youtubePlayerView.getPlayerUIController().showBufferingProgress(false);
                youtubePlayerView.getPlayerUIController().showMenuButton(false);
                youtubePlayerView.getPlayerUIController().showFullscreenButton(false);
                youtubePlayerView.getPlayerUIController().showVideoTitle(false);
                youtubePlayerView.getPlayerUIController().showDuration(false);
                getLifecycle().addObserver(youtubePlayerView);
                youtubePlayerView.initialize(new YouTubePlayerInitListener() {
                    @Override
                    public void onInitSuccess(@NonNull final YouTubePlayer initializedYouTubePlayer) {
                        InAppPurchaseDetailActivity.this.initializedYouTubePlayer = initializedYouTubePlayer;
                        initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onReady() {
                                initializedYouTubePlayer.loadVideo(String.valueOf(mDataList.get(0)), 0);
                            }
                        });
                    }
                }, true);
                collection.addView(layout);
                return layout;

            } else {
                ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.layout_in_app_purchase_detail_view_pager_item, collection, false);
                ImageView imageView = layout.findViewById(R.id.image_view);
                ImageManager.loadImageViewInAppPurchase(mContext, ((InAppPurchaseProductImageModel) mDataList.get(position)).imageUrl, imageView);
                collection.addView(layout);
                return layout;
            }
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

    }

    @Override
    public void onDestroy() {
        if (mBillingProcessor != null) {
            mBillingProcessor.release();
        }
        super.onDestroy();
        if (youtubePlayerView != null)
            youtubePlayerView.release();
    }

    static class Utils {
        static SortedMap<Currency, Locale> currencyLocaleMap;

        static {
            currencyLocaleMap = new TreeMap<Currency, Locale>(new Comparator<Currency>() {
                public int compare(Currency c1, Currency c2) {
                    return c1.getCurrencyCode().compareTo(c2.getCurrencyCode());
                }
            });
            for (Locale locale : Locale.getAvailableLocales()) {
                try {
                    Currency currency = Currency.getInstance(locale);
                    currencyLocaleMap.put(currency, locale);
                } catch (Exception e) {
                }
            }
        }

        static String getCurrencySymbol(String currencyCode) {
            Currency currency = Currency.getInstance(currencyCode);
            System.out.println(currencyCode + ":-" + currency.getSymbol(currencyLocaleMap.get(currency)));
            return currency.getSymbol(currencyLocaleMap.get(currency));
        }

    }

}
