package com.mobiroller.core.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.mobiroller.core.SharedApplication;
import com.mobiroller.core.coreui.Theme;
import com.mobiroller.core.coreui.helpers.ColorHelper;
import com.mobiroller.core.coreui.models.ImageModel;
import com.mobiroller.core.coreui.util.ScreenUtil;
import com.mobiroller.core.helpers.ScreenHelper;
import com.mobiroller.core.models.NavigationItemModel;
import com.mobiroller.core.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

import static com.mobiroller.core.constants.Constants.Mobiroller_Preferences_FilePath;

/**
 * Created by ealtaca on 08.03.2017.
 */

public class ImageManager {

    private final static String TAG = "ImageManager";

    public ImageManager() {
    }

    public static void displayUserImage(Context context, String imageUrl, final ImageView imageView) {
        if (imageUrl == null || imageUrl.equals(""))
            return;
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.defaultuser)
                .error(R.drawable.defaultuser);
        Glide.with(context).setDefaultRequestOptions(requestOptions).load(imageUrl)
                .into(imageView);
    }

    public static void loadBackgroundImageFromImageModel(View view, ImageModel imageModel) {
        if (imageModel != null && imageModel.getImageURL() != null && view != null) {
            loadBackgroundImage(imageModel.getImageURL(), view);
        }
    }

    public static void loadBackgroundImage(String imageUrl, final View view) {
        RequestListener<Drawable> requestListener = new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                new Handler(Looper.getMainLooper()).post(() -> view.setBackground(resource));
                return false;
            }
        };
        try {
            String[] pathBuffer = imageUrl.split("/");
            int size = pathBuffer.length;
            String imageName = (pathBuffer[size - 1]);
            if (checkImageIsExistInAssets(imageName)) {
                Timber.tag(TAG).d("Loading image from assets. Image name : %s", imageName);
                Glide.with(view.getContext())
                        .load(Uri.parse("file:///android_asset/Files/" + imageName))
                        .listener(requestListener)
                        .submit();
            } else {
                Timber.tag(TAG).d("Loading image from cache or internet. Image name : %s", imageName);
                Glide.with(view.getContext())
                        .load(imageUrl)
                        .listener(requestListener)
                        .submit();
            }

        } catch (Exception e) {
            Timber.tag(TAG).d("Loading from assets FAILED. Loading image from internet. Image url : %s", imageUrl);
            e.printStackTrace();
            Glide.with(view.getContext())
                    .load(imageUrl)
                    .listener(requestListener)
                    .submit();
        }
    }

    public static void loadBackgroundImage(final Context context, String imageUrl, final View view) {
        RequestListener<Drawable> requestListener = new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                new Handler(Looper.getMainLooper()).post(() -> view.setBackgroundDrawable(resource));
                return false;
            }
        };
        try {
            String[] pathBuffer = imageUrl.split("/");
            int size = pathBuffer.length;
            String imageName = (pathBuffer[size - 1]);
            if (checkImageIsExistInAssets(imageName)) {
                Timber.tag(TAG).d("Loading image from assets. Image name : %s", imageName);
                Glide.with(context)
                        .load(Uri.parse("file:///android_asset/Files/" + imageName))
                        .listener(requestListener)
                        .submit();
            } else {
                Timber.tag(TAG).d("Loading image from cache or internet. Image name : %s", imageName);
                Glide.with(context)
                        .load(imageUrl)
                        .listener(requestListener)
                        .submit();
            }

        } catch (Exception e) {
            Timber.tag(TAG).d("Loading from assets FAILED. Loading image from internet. Image url : %s", imageUrl);
            e.printStackTrace();
            Glide.with(context)
                    .load(imageUrl)
                    .listener(requestListener)
                    .submit();
        }
    }

    public static void loadImageView(Context context, String imageUrl, final ImageView view) {
        try {
            String[] pathBuffer = imageUrl.split("/");
            int size = pathBuffer.length;
            String imageName = (pathBuffer[size - 1]);
            if (checkImageIsExistInAssets(imageName)) {
                Timber.tag(TAG).d("Loading image from assets. Image name : %s", imageName);
                Glide.with(context)
                        .load(Uri.parse("file:///android_asset/Files/" + imageName))
                        .into(view);
            } else {
                Timber.tag(TAG).d("Loading image from cache or internet. Image name : %s", imageName);
                Glide.with(context)
                        .load(imageUrl)
                        .into(view);
            }

        } catch (Exception e) {
            Timber.tag(TAG).d("Loading from assets FAILED. Loading image from internet. Image url : %s", imageUrl);
            e.printStackTrace();
            Glide.with(context)
                    .load(imageUrl)
                    .into(view);
        }
    }

    public static void loadImageView(String imageUrl, final ImageView view) {
        try {
            String[] pathBuffer = imageUrl.split("/");
            int size = pathBuffer.length;
            String imageName = (pathBuffer[size - 1]);
            if (checkImageIsExistInAssets(imageName)) {
                Glide.with(view.getContext())
                        .load(Uri.parse("file:///android_asset/Files/" + imageName))
                        .into(view);
            } else {
                Glide.with(view.getContext())
                        .load(imageUrl)
                        .into(view);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Glide.with(view.getContext())
                    .load(imageUrl)
                    .into(view);
        }
    }

    /**
     * if exist load image from productImages in assets
     * else load image from network
     *
     * @param context  Context
     * @param imageUrl ImageUrl
     * @param view     ImageView
     */
    public static void loadImageViewInAppPurchase(Context context, String imageUrl, final ImageView view) {
        Glide.with(context)
                .load(imageUrl)
                .into(view);
    }

    /**
     * Get file location from image url
     *
     * @param imageUrl Image Source
     * @return File
     */
    public static File getFileFromUrl(String imageUrl) {
        if (imageUrl != null && !imageUrl.equals("")) {
            final String[] parts = imageUrl.split("/");
            final int position = parts.length - 1;
            return new File(Mobiroller_Preferences_FilePath + "/" + parts[position]);
        } else
            return null;
    }

    /**
     * Save bitmap to storage
     *
     * @param bitmap downloaded image
     * @param file   will be saved to file
     */
    private static void saveBitmapToStorage(Bitmap bitmap, File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getImageSync(String imageUrl) {
        File f = getFileFromUrl(imageUrl);

        if (f != null) {
            if (!f.exists()) {
                return getBitmapFromURL(imageUrl, f);
            } else {
                return BitmapFactory.decodeFile(f.getPath());
            }
        } else {
            return null;
        }
    }

    public static Bitmap getBitmapFromURL(String src, File file) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            saveBitmapToStorage(myBitmap, file);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (width > height) {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            } else if (height > width) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            }
//            if (ratioMax > 1) {
//                finalWidth = (int) ((float)maxHeight * ratioBitmap);
//            } else {
//                finalHeight = (int) ((float)maxWidth / ratioBitmap);
//            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    public static Bitmap resizeMaxDeviceSize(Bitmap image) {
        if (ScreenUtil.getScreenHeight() > 0 && ScreenUtil.getScreenWidth() - ScreenHelper.dpToPx(40) > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = ((float) ScreenUtil.getScreenWidth() - ScreenHelper.dpToPx(40)) / (float) ScreenUtil.getScreenHeight();

            int finalWidth = ScreenUtil.getScreenWidth() - ScreenHelper.dpToPx(40);
            int finalHeight = ScreenUtil.getScreenHeight();
            if (width > height) {
                finalHeight = (int) (((float) ScreenUtil.getScreenWidth() - ScreenHelper.dpToPx(40)) / ratioBitmap);
            } else if (height > width) {
                finalWidth = (int) ((float) ScreenUtil.getScreenHeight() * ratioBitmap);
            }
//            if (ratioMax > 1) {
//                finalWidth = (int) ((float)maxHeight * ratioBitmap);
//            } else {
//                finalHeight = (int) ((float)maxWidth / ratioBitmap);
//            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    public static boolean checkImageIsExistInAssets(String imageName) {
        return SharedApplication.assetList != null && Arrays.asList(SharedApplication.assetList).contains(imageName);
    }

    public static void loadUserImage(ImageView imageView, String userImageUrl) {
        if (!userImageUrl.equalsIgnoreCase(""))
            Glide.with(imageView).load(userImageUrl)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    ).addListener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                    imageView.setImageResource(R.drawable.ic_account_circle_black_36dp);
                    ColorFilter cf = new PorterDuffColorFilter(ColorHelper.isColorDark(Theme.primaryColor) ? Color.WHITE : Color.BLACK, PorterDuff.Mode.MULTIPLY);
                    imageView.setColorFilter(cf);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(imageView);
    }

    public static void loadSlidingMenuIcons(Context context, List<MenuItem> menuItemList, ArrayList<NavigationItemModel> validNavItems) {
        for (int i = 0; i < validNavItems.size(); i++) {
            if (validNavItems.get(i) == null ||
                    validNavItems.get(i).getIconImage() == null ||
                    validNavItems.get(i).getIconImage().getImageURL() == null ||
                    menuItemList.get(i) == null)
                continue;
            SlidingMenuIconRequestListener slidingMenuIconRequestListener = new SlidingMenuIconRequestListener(menuItemList.get(i));
            loadIcon(context, slidingMenuIconRequestListener.requestListener, validNavItems.get(i).getIconImage().getImageURL());
        }
    }

    public static void loadSlidingMenuIcons(Context context, List<MenuItem> menuItemList, List<String> iconList) {
        for (int i = 0; i < iconList.size(); i++) {
            if (iconList.get(i) == null ||
                    menuItemList.get(i) == null)
                continue;
            SlidingMenuIconRequestListener slidingMenuIconRequestListener = new SlidingMenuIconRequestListener(menuItemList.get(i));
            loadIcon(context, slidingMenuIconRequestListener.requestListener, iconList.get(i));
        }
    }

    public static void loadMainMenuIcons(Context context, Menu menuItemList, ArrayList<NavigationItemModel> validNavItems) {
        for (int i = 0; i < validNavItems.size(); i++) {
            if (validNavItems.get(i) == null ||
                    validNavItems.get(i).getIconImage() == null ||
                    validNavItems.get(i).getIconImage().getImageURL() == null ||
                    menuItemList.findItem(i) == null)
                continue;
            SlidingMenuIconRequestListener slidingMenuIconRequestListener = new SlidingMenuIconRequestListener(menuItemList.findItem(i));
            loadIcon(context, slidingMenuIconRequestListener.requestListener, validNavItems.get(i).getIconImage().getImageURL());
        }

    }

    private static void loadIcon(Context context, RequestListener requestListener, String imageUrl) {
        try {
            String[] pathBuffer = imageUrl.split("/");
            int size = pathBuffer.length;
            String imageName = (pathBuffer[size - 1]);
            if (checkImageIsExistInAssets(imageName)) {
                Glide.with(context)
                        .load(Uri.parse("file:///android_asset/Files/" + imageName))
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                        )
                        .addListener(requestListener).submit();
            } else {
                Glide.with(context)
                        .load(imageUrl)
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                        )
                        .addListener(requestListener).submit();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Glide.with(context)
                    .load(imageUrl)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
                    .addListener(requestListener).submit();
        }
    }

    static class SlidingMenuIconRequestListener {
        MenuItem menuItem;

        SlidingMenuIconRequestListener(MenuItem menuItem) {
            this.menuItem = menuItem;
        }

        RequestListener requestListener = new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                new Handler(Looper.getMainLooper()).post(() -> menuItem.setIcon(resource));
                return false;
            }
        };
    }

}
