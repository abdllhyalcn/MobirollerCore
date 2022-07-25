package com.mobiroller.core.activities.inapppurchase;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.mobiroller.core.R2;
import com.mobiroller.core.adapters.InAppPurchasePagerAdapter;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.helpers.InAppPurchaseHelper;
import com.mobiroller.core.helpers.ScreenHelper;
import com.mobiroller.core.helpers.SharedPrefHelper;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.models.events.InAppPurchaseSuccessEvent;
import com.mobiroller.core.R;
import com.mobiroller.core.util.RssUtil;
import com.mobiroller.core.views.WrapContentViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InAppPurchaseActivity extends AppCompatActivity {

    @BindView(R2.id.view_pager)
    WrapContentViewPager viewPager;
    @BindView(R2.id.image)
    ImageView image;
    @BindView(R2.id.image_filter_one)
    ImageView imageFilterOne;
    @BindView(R2.id.image_filter_two)
    ImageView imageFilterTwo;
    @BindView(R2.id.view_pager_count_dots)
    LinearLayout viewPagerCountDots;
    @BindView(R2.id.close_button)
    ImageView closeButton;

    private String mScreenId;
    private String mScreenType;
    private boolean mIsFromActivity;
    private int dotsCount;
    private ImageView[] dots;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_purchase);
        ButterKnife.bind(this);
        final SharedPrefHelper sharedPrefHelper = UtilManager.sharedPrefHelper();
        image.setColorFilter(sharedPrefHelper.getActionBarColor());
        imageFilterOne.setColorFilter(ScreenHelper.getLighterColor(sharedPrefHelper.getActionBarColor(), 0.5f));
        imageFilterTwo.setColorFilter(ScreenHelper.getLighterColor(sharedPrefHelper.getActionBarColor(), 0.8f));
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        if (getIntent().hasExtra(Constants.KEY_SCREEN_ID))
            mScreenId = getIntent().getStringExtra(Constants.KEY_SCREEN_ID);
        if (getIntent().hasExtra(Constants.KEY_SCREEN_TYPE))
            mScreenType = getIntent().getStringExtra(Constants.KEY_SCREEN_TYPE);
        if (getIntent().hasExtra(Constants.KEY_IS_FROM_ACTIVITY))
            mIsFromActivity = getIntent().getBooleanExtra(Constants.KEY_IS_FROM_ACTIVITY, false);
        InAppPurchasePagerAdapter adapter = new InAppPurchasePagerAdapter(this, InAppPurchaseHelper.getScreenProductList(mScreenId), mScreenId,mScreenType, mIsFromActivity);
        viewPager.setAdapter(adapter);
        viewPager.setPageMargin(RssUtil.convertDpToPixel(25,this));
        viewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                int pageWidth = viewPager.getMeasuredWidth() ;
                int pageHeight = viewPager.getHeight();
                int paddingLeft = viewPager.getPaddingLeft();
                float transformPos = (float) (page.getLeft() - (viewPager.getScrollX() + paddingLeft)) / pageWidth;
                int max = -pageHeight / 20;

                if (transformPos < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    page.setTranslationY(0);
                } else if (transformPos <= 1) { // [-1,1]
                    page.setTranslationY(max * (1 - Math.abs(transformPos)));

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    page.setTranslationY(0);
                }


            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotsCount; i++) {
                    Drawable dot = ContextCompat.getDrawable(InAppPurchaseActivity.this, R.drawable.nonselecteditem_dot);
                    dots[i].setImageDrawable(dot);
                }


                Drawable dot = ContextCompat.getDrawable(InAppPurchaseActivity.this, R.drawable.selecteditem_dot);
                dot.setColorFilter(new
                        PorterDuffColorFilter(sharedPrefHelper.getActionBarColor(), PorterDuff.Mode.MULTIPLY));
                dots[position].setImageDrawable(dot);
                viewPager.reMeasureCurrentPage(viewPager.getCurrentItem());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        dotsCount = adapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(InAppPurchaseActivity.this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(InAppPurchaseActivity.this, R.drawable.nonselecteditem_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            viewPagerCountDots.addView(dots[i], params);
        }

        Drawable dot = ContextCompat.getDrawable(InAppPurchaseActivity.this, R.drawable.selecteditem_dot);
        dot.setColorFilter(new
                PorterDuffColorFilter(sharedPrefHelper.getActionBarColor(), PorterDuff.Mode.MULTIPLY));
        dots[0].setImageDrawable(dot);

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


    @OnClick(R2.id.close_button)
    public void onClickCloseButton() {
        finish();
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Subscribe
    public void onPostInAppPurchaseSuccessEvent(InAppPurchaseSuccessEvent event) {
        finish();
    }

}
