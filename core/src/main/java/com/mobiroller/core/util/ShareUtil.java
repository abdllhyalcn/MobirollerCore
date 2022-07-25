package com.mobiroller.core.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.mobiroller.core.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ealtaca on 02.02.2017.
 */

public class ShareUtil {

    /**
     * Share text
     *
     * @param activity
     * @param message
     * @param url
     */
    public static void shareURL(AppCompatActivity activity, String message, String url) {
        InterstitialAdsUtil interstitialAdsUtil = new InterstitialAdsUtil(activity);
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, message);
        i.putExtra(Intent.EXTRA_TEXT, url);
        interstitialAdsUtil.checkInterstitialAds(Intent.createChooser(i, activity.getString(R.string.action_share)));
    }

    /**
     * Share Image
     *
     * @param activity
     * @param message
     * @param bitmap
     * @param url
     */
    public static void shareImage(AppCompatActivity activity, String message, Bitmap bitmap, String url) {
        InterstitialAdsUtil interstitialAdsUtil = new InterstitialAdsUtil(activity);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        try {

            File cachePath = new File(activity.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        File imagePath = new File(activity.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(activity, activity.getPackageName(), newFile);
        activity.grantUriPermission(activity.getPackageName(),contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, activity.getContentResolver().getType(contentUri));
            share.putExtra(Intent.EXTRA_TEXT, message);
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            interstitialAdsUtil.checkInterstitialAds(Intent.createChooser(shareIntent, "Share Image"));
        }
    }


    /**
     * Get mime type for image
     *
     * @param uri     Image URI
     * @param context Context
     * @return Image mime type
     */
    public static String getMimeType(Uri uri, Context context) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        if (mimeType != null && !mimeType.equalsIgnoreCase(""))
            return mimeType;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        HttpURLConnection httpConn = null;
        try {
            httpConn = (HttpURLConnection) new URL(uri.toString()).openConnection();

            opt.inJustDecodeBounds = true;

            InputStream istream = httpConn.getInputStream();
            BitmapFactory.decodeStream(istream, null, opt);
            istream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return opt.outMimeType;
    }

    /**
     * If Facebook app installed open else open url in browser
     *
     * @param context
     * @param facebook
     */
    public static void getOpenFacebookIntent(Context context, String facebook) {
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + facebook)));
        } catch (Exception e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + facebook)));
        }
    }


    /**
     * If Linkedin app installed open else open url in browser
     *
     * @param context
     * @param linkedin
     */
    public static void getOpenLinkedinIntent(Context context, String linkedin) {
        try {
            context.getPackageManager().getPackageInfo("com.linkedin.android", 0);
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://profile/" + linkedin)));
        } catch (Exception e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/profile/view?id=" + linkedin)));
        }
    }

    /**
     * If Twitter app installed open else open url in browser
     *
     * @param context
     * @param twitter_user_name
     */
    public static void getOpenTwitterIntent(Context context, String twitter_user_name) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitter_user_name)));
        } catch (Exception e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + twitter_user_name)));
        }
    }

    /**
     * If GooglePlus app installed open else open url in browser
     *
     * @param context
     * @param google_plus_id
     */
    public static void getOpenGooglePlusIntent(Context context, String google_plus_id) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://plus.google.com/" + google_plus_id + "/"));
            intent.setPackage("com.google.android.apps.plus"); // don't open the browser, make sure it opens in Google+ app
            context.startActivity(intent);
        } catch (Exception e) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://plus.google.com/" + google_plus_id + "/"));
            context.startActivity(intent);
        }
    }

    /**
     * If instagram app installed open else open url in browser
     *
     * @param context
     * @param instagram
     */
    public static void getOpenInstagramIntent(Context context, String instagram) {
        Uri uri = Uri.parse("http://instagram.com/_u/" + instagram);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            context.startActivity(likeIng);
        } catch (Exception e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/" + instagram)));
        }
    }

}
