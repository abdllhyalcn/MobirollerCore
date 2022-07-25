/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain webViewLayout copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.mobiroller.core.activities;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.SharedElementCallback;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.mobiroller.core.R2;
import com.mobiroller.core.coreui.util.ColorUtil;
import com.mobiroller.core.helpers.FavoriteHelper;
import com.mobiroller.core.helpers.LegacyToolbarHelper;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.helpers.SharedPrefHelper;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.coreui.models.GalleryModel;
import com.mobiroller.core.R;
import com.mobiroller.core.util.ShareUtil;
import com.mobiroller.core.views.DepthPageTransformer;
import com.mobiroller.core.coreui.views.HackyViewPager;
import com.mobiroller.core.views.PullBackLayout;

import java.io.File;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class ImagePagerActivity extends AppCompatActivity implements PullBackLayout.Callback {


    LegacyToolbarHelper toolbarHelper;
    LocalizationHelper localizationHelper;
    SharedPrefHelper sharedPrefHelper;

    @BindView(R2.id.pager)
    HackyViewPager pager;
    @BindView(R2.id.puller)
    PullBackLayout puller;
    @BindView(R2.id.toolbar_top)
    Toolbar toolbar;

    private TextView mImageTextView;

    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String TOOLBAR_TITLE = "ToolbarTitle";
    private List<GalleryModel> galleryModels;
    private View mCurrentView = null;

    private Animation animSlideIn;
    private Animation animSlideOut;

    private boolean isDownloadable = true;
    private boolean isTransitionActive = false;
    private boolean fromGalleryView = false;
    private FavoriteHelper mFavoriteHelper;

    private String defaultMimeType = "image/jpeg";

    private final SharedElementCallback mCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (mCurrentView != null) {
                PhotoView sharedElement = mCurrentView.findViewById(R.id.image);
                if (names.get(0).equalsIgnoreCase("galleryImage"))
                    sharedElements.put("galleryImage", sharedElement);
            }
        }
    };

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            params.topMargin = getStatusBarHeight();
            toolbar.setLayoutParams(params);
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle("");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            toolbar.getNavigationIcon().setAutoMirrored(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                try {
                    View viewa = getCurrentFocus();
                    if (viewa != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(viewa.getWindowToken(), 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ColorUtil.getColorWithAlpha(sharedPrefHelper.getActionBarColor(), 0.3f));
        }

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_image_pager);
        ButterKnife.bind(this);
        supportPostponeEnterTransition();
        sharedPrefHelper = UtilManager.sharedPrefHelper();
        toolbarHelper = new LegacyToolbarHelper();
        localizationHelper = UtilManager.localizationHelper();
        animSlideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        animSlideIn.setDuration(300);
        animSlideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out);
        animSlideOut.setDuration(300);
        mFavoriteHelper = new FavoriteHelper(this);
        puller.setCallback(this);
        puller.setEnableSwipe(true);
        setEnterSharedElementCallback(mCallback);
        setupToolbar();
        Bundle bundle = getIntent().getExtras();
        galleryModels = (List<GalleryModel>) bundle.getSerializable("imageList");
        isDownloadable = bundle.getBoolean("isDownloadable");
        fromGalleryView = bundle.getBoolean("fromGalleryView");
        int pagerPosition = bundle.getInt("position", 0);
        pager.setAdapter(new ImagePagerAdapter(galleryModels));
        pager.setCurrentItem(pagerPosition);
        pager.setPageTransformer(true, new DepthPageTransformer());
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                invalidateOptionsMenu();
            }

            @Override
            public void onPageSelected(int position) {
                isTransitionActive = false;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onPullStart() {

    }

    @Override
    public void onPull(float progress) {

    }

    @Override
    public void onPullCancel() {

    }

    @Override
    public void onPullComplete() {
        onBackPressed();
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private List<GalleryModel> images;
        LayoutInflater inflater;

        ImagePagerAdapter(List<GalleryModel> images) {
            inflater = (LayoutInflater) ImagePagerActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.images = images;

        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
            final PhotoView imageView = imageLayout.findViewById(R.id.image);
            final TextView imageText = imageLayout.findViewById(R.id.image_text);
            imageText.setBackgroundColor(ColorUtil.getColorWithAlpha(sharedPrefHelper.getActionBarColor(), 0.2f));
            final ProgressBar spinner = imageLayout.findViewById(R.id.loading);
            /**
             * Allow transition for only current item to avoid animation error
             * Disable all other transitions
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                if (pager.getCurrentItem() != position)
                    imageView.setTransitionName("dummy");
                else
                    isTransitionActive = true;
            }

            if (images.get(position).getFile() != null) {
                Glide.with(ImagePagerActivity.this)
                        .load(images.get(position).getFile())
                        .apply(new RequestOptions().placeholder(R.drawable.no_image))
                        .addListener(createCallBack(imageView, imageText, images.get(position), spinner))
                        .into(imageView);
            } else {
                Glide.with(ImagePagerActivity.this)
                        .load(images.get(position).getURL())
                        .apply(new RequestOptions().placeholder(R.drawable.no_image))
                        .addListener(createCallBack(imageView, imageText, images.get(position), spinner))
                        .into(imageView);
            }

            view.addView(imageLayout, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);

            return imageLayout;
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
            mCurrentView = (View) object;
            mImageTextView = mCurrentView.findViewById(R.id.image_text);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        toolbar.inflateMenu(R.menu.image_menu);
        menu = toolbar.getMenu();
        if (!isDownloadable) {
            MenuItem item = menu.findItem(R.id.action_download);
            item.setVisible(false);
        }
        if (!sharedPrefHelper.isFavoriteActive())
            menu.findItem(R.id.action_favorite).setVisible(false);
        if (!fromGalleryView) {
            MenuItem item = menu.findItem(R.id.action_favorite);
            item.setVisible(false);
        } else {
            if (mFavoriteHelper.isImageContentAddedToList(galleryModels.get(pager.getCurrentItem()))) {
                menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_bookmark_white_24dp);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bitmap bitmap = ((BitmapDrawable) ((ImageView) mCurrentView.findViewById(R.id.image)).getDrawable()).getBitmap();
        int itemId = item.getItemId();
        if (itemId == R.id.action_share) {
            ShareUtil.shareImage(this, galleryModels.get(pager.getCurrentItem()).getCaption(), bitmap, galleryModels.get(pager.getCurrentItem()).getURL());
            return true;
        } else if (itemId == R.id.action_download) {
            ImagePagerActivityPermissionsDispatcher.downloadImageWithPermissionCheck(this);
            return true;
        } else if (itemId == R.id.action_favorite) {
            GalleryModel galleryModel = galleryModels.get(pager.getCurrentItem());
            if (mFavoriteHelper.isImageContentAddedToList(galleryModel)) {
                mFavoriteHelper.removeGalleryContent(galleryModel);
            } else
                mFavoriteHelper.addImageContentToList(galleryModel);
            invalidateOptionsMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isTransitionActive)
            ActivityCompat.finishAfterTransition(this);
        else
            finish();
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void downloadImage() {
        GalleryModel galleryModel = galleryModels.get(pager.getCurrentItem());
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getAbsolutePath(), "/" + getString(R.string.app_name).replaceAll("\\s+", "" + "/"));

        //Check gallery has R.string.app_name directory
        //if doesnt exists create
        if (!file.exists()) {
            file.mkdirs();
        }

        String fileName = galleryModel.getURL().substring(galleryModel.getURL().lastIndexOf('/') + 1, galleryModel.getURL().length());
        if (fileName.contains(".")) {
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        }

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(galleryModel.getURL()));
        String mimeType = ShareUtil.getMimeType(Uri.parse(galleryModel.getURL()), this);
        if (mimeType == null || mimeType.equalsIgnoreCase(""))
            mimeType = defaultMimeType;
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDescription(galleryModel.getCaption())
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, fileName + "." + mimeType.replaceFirst(".*/([^/?]+).*", "$1"));
        downloadManager.enqueue(request);
    }

    private RequestListener<Drawable> createCallBack(final ImageView imageView, final TextView imageText, final GalleryModel galleryModel, final ProgressBar spinner) {
        return new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                supportStartPostponedEnterTransition();
                spinner.setVisibility(View.GONE);
                Toast.makeText(ImagePagerActivity.this, getString(R.string.common_error), Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                supportStartPostponedEnterTransition();
                PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(imageView);
                photoViewAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(ImageView view, float x, float y) {
                        hideOrShowStatusBar();
                    }
                });
                if (galleryModel.getCaption() != null && !galleryModel.getCaption().equalsIgnoreCase("")) {
                    imageText.setText(localizationHelper.getLocalizedTitle(galleryModel.getCaption()));
                } else
                    imageText.setVisibility(View.GONE);
                if ((toolbar.getVisibility() == View.GONE))
                    imageText.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
                return false;
            }

        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        ImagePagerActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void hideOrShowStatusBar() {
        if (toolbar.getVisibility() == View.VISIBLE) {
            toolbar.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
            if (mImageTextView.getText().toString().length() > 1) {
                mImageTextView.startAnimation(animSlideOut);
                animSlideOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mImageTextView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().setStatusBarColor(ColorUtil.getColorWithAlpha(sharedPrefHelper.getActionBarColor(), 0.3f));
            }
            toolbar.setVisibility(View.VISIBLE);

            if (mImageTextView.getText().toString().length() > 1) {
                mImageTextView.startAnimation(animSlideIn);
                animSlideIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mImageTextView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        }
    }
}