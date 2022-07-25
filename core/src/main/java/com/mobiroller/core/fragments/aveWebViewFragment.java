package com.mobiroller.core.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.TimingLogger;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.crashlytics.android.Crashlytics;
import com.mobiroller.core.R;
import com.mobiroller.core.R2;
import com.mobiroller.core.SharedApplication;
import com.mobiroller.core.activities.GenericActivity;
import com.mobiroller.core.constants.DynamicConstants;
import com.mobiroller.core.helpers.LegacyProgressViewHelper;
import com.mobiroller.core.helpers.RxJavaRequestHelper;
import com.mobiroller.core.models.WebContent;
import com.mobiroller.core.util.AdManager;
import com.mobiroller.core.views.VideoEnabledWebChromeClient;
import com.mobiroller.core.views.VideoEnabledWebView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.delight.android.webview.AdvancedWebView;

@SuppressLint("NewApi")
public class aveWebViewFragment extends BaseModuleFragment implements AdvancedWebView.Listener {
    LegacyProgressViewHelper legacyProgressViewHelper;

    @BindView(R2.id.web_view)
    public VideoEnabledWebView mWebView;

    @BindView(R2.id.web_layout)
    RelativeLayout relLayout;

    private ViewGroup videoLayout;
    private static String webScript;
    private static final String PDF_MIME_TYPE = "application/pdf";
    private boolean isPageLoading = false;
    private boolean isProgressViewEnabled = true;
    private boolean isCacheEnabled = false;
    private String mGeoLocationRequestOrigin;
    private GeolocationPermissions.Callback mGeoLocationCallBack;
    private final static int mGeoLocationRequestCode = 1234;

    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR = 1;
    private WebContent webContent;

    //select whether you want to upload multiple files (set 'true' for yes)
    private boolean multiple_files = false;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_web_view, container, false);
        unbinder = ButterKnife.bind(this, view);

        legacyProgressViewHelper = new LegacyProgressViewHelper(getActivity());
        if (screenModel != null) {
            isProgressViewEnabled = screenModel.getShowProgress();
            if (screenModel.isCacheEnabled != null && screenModel.isCacheEnabled) {
                isCacheEnabled = true;
            }
        } else if (getArguments() != null && getArguments().containsKey("webContent")) {
            webContent = (WebContent) getArguments().getSerializable("webContent");
        }

        videoLayout = view.findViewById(R.id.videoLayout); // Your own view, read class comments
        hideToolbar(view.findViewById(R.id.toolbar_top));

        if (!getActivity().isFinishing() && isProgressViewEnabled)
            legacyProgressViewHelper.show();

        if (savedInstanceState != null) {
            mWebView.restoreState(savedInstanceState);
            if (isProgressViewEnabled)
                legacyProgressViewHelper.dismiss();
        } else
            loadUi();

        return view;
    }

    public void loadUi() {
        if (networkHelper.isConnected()) {

            CookieManager cookieManager = CookieManager.getInstance();
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.setAcceptThirdPartyCookies(mWebView,true);
            } else {
                cookieManager.setAcceptCookie(true);
            }

            final TimingLogger timings = new TimingLogger("WebView", "methodA");
            mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            if (isCacheEnabled)
                mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            else
                mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            mWebView.setListener(getActivity(), this);
            mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.82 Mobile Safari/537.36");


            // Otherwise allow the OS to handle it
            WebViewClient wvClient = new WebViewClient() {

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    isPageLoading = true;
                    timings.addSplit("work A");
                    if (isProgressViewEnabled) {
                        if (getUserVisibleHint() && getActivity() != null && !getActivity().isFinishing())
                            legacyProgressViewHelper.show();
                    }
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    isPageLoading = false;
                    timings.addSplit("work B");
                    if (webScript != null)
                        view.loadUrl("javascript:" + webScript);
                    if (isProgressViewEnabled)
                        legacyProgressViewHelper.dismiss();
                    timings.dumpToLog();
                }

                @Override
                public void onReceivedError(WebView view, int errorCode,
                                            String description, String failingUrl) {
                    isPageLoading = false;
                    try {
                        mWebView.stopLoading();
                        if (isProgressViewEnabled)
                            legacyProgressViewHelper.dismiss();
                    } catch (Exception e) {
                    }

                    super.onReceivedError(view, errorCode, description, failingUrl);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    try {
                        if (url.startsWith("http:") || url.startsWith("https:")) {
                            checkInterstitialAd();
                            return false;
                        }
                        if (url.startsWith("intent://") && url.contains("scheme=http")) {
                            url = Uri.decode(url);
                            String bkpUrl = null;
                            Pattern regexBkp = Pattern.compile("intent://(.*?)#");
                            Matcher regexMatcherBkp = regexBkp.matcher(url);
                            if (regexMatcherBkp.find()) {
                                bkpUrl = regexMatcherBkp.group(1);
                                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + bkpUrl));
                                startActivity(myIntent);
                                return true;
                            }
                        }
                        // Otherwise allow the OS to handle it
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    } catch (Exception e) {
                        Crashlytics.logException(e);
                    }
                    return true;
                }
            };

            //fit content to screen
            mWebView.getSettings().setUseWideViewPort(true);
            mWebView.getSettings().setLoadWithOverviewMode(true);
            // pinch to zoom ,not display zoom controllers
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.getSettings().setDisplayZoomControls(false);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setAllowFileAccess(true);
            mWebView.getSettings().setDomStorageEnabled(true);
            mWebView.getSettings().setAppCacheEnabled(true);
            mWebView.getSettings().setGeolocationEnabled(true);
            mWebView.getSettings().setAppCachePath("/data/data/" + mWebView.getContext().getPackageName() + "/databases/");


            if (Build.VERSION.SDK_INT >= 21) {
                mWebView.getSettings().setMixedContentMode(0);
                mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else if (Build.VERSION.SDK_INT >= 19) {
                mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            mWebView.setWebViewClient(wvClient);
            //noinspection all
            View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class comments
            // See all available constructors...
            // Subscribe to standard events, such as onProgressChanged()...
            // Your code...
            /*
             * openFileChooser is not a public Android API and has never been part of the SDK.
             */
            //handling input[type="file"] requests for android API 16+
            //handling input[type="file"] requests for android API 21+
            //checking for storage permission to write images for upload
            //checking for WRITE_EXTERNAL_STORAGE permission
            //checking for CAMERA permissions
            VideoEnabledWebChromeClient webChromeClient = new VideoEnabledWebChromeClient(relLayout, videoLayout, loadingView, mWebView) // See all available constructors...
            {
                // Subscribe to standard events, such as onProgressChanged()...
                @Override
                public void onProgressChanged(WebView view, int progress) {
                    // Your code...
                }

                @Override
                public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                    mGeoLocationCallBack = callback;
                    mGeoLocationRequestOrigin = origin;
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                        mGeoLocationCallBack = callback;
                        mGeoLocationRequestOrigin = origin;
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, mGeoLocationRequestCode);
                        else
                            Toast.makeText(getActivity(), R.string.permission_location_denied_webview_user_location_never, Toast.LENGTH_SHORT).show();
                    } else {
                        callback.invoke(origin, true, false);
                    }
                }

                /*
                 * openFileChooser is not a public Android API and has never been part of the SDK.
                 */
                //handling input[type="file"] requests for android API 16+
                @SuppressLint("ObsoleteSdkInt")
                @SuppressWarnings("unused")
                public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                    mUM = uploadMsg;
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("*/*");
                    if (multiple_files && Build.VERSION.SDK_INT >= 18) {
                        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    }
                    startActivityForResult(Intent.createChooser(i, "File Chooser"), FCR);
                }

                //handling input[type="file"] requests for android API 21+
                @SuppressLint("InlinedApi")
                public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                    if (checkFilePermission()) {
                        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

                        //checking for storage permission to write images for upload
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), perms, FCR);

                            //checking for WRITE_EXTERNAL_STORAGE permission
                        } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, FCR);

                            //checking for CAMERA permissions
                        }
                        if (mUMA != null) {
                            mUMA.onReceiveValue(null);
                        }
                        mUMA = filePathCallback;
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                                takePictureIntent.putExtra("PhotoPath", mCM);
                            } catch (IOException ex) {
                                Log.e("Fail", "Image file creation failed", ex);
                            }
                            if (photoFile != null) {
                                mCM = "file:" + photoFile.getAbsolutePath();
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            } else {
                                takePictureIntent = null;
                            }
                        }
                        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                        contentSelectionIntent.setType("*/*");
                        if (multiple_files) {
                            contentSelectionIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        }
                        Intent[] intentArray;
                        if (takePictureIntent != null) {
                            intentArray = new Intent[]{takePictureIntent};
                        } else {
                            intentArray = new Intent[0];
                        }

                        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                        chooserIntent.putExtra(Intent.EXTRA_TITLE, "File Chooser");
                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                        startActivityForResult(chooserIntent, FCR);
                        return true;
                    } else {
                        return false;
                    }
                }
            };

            webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
                @Override
                public void toggledFullscreen(boolean fullscreen) {
                    // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                    if (fullscreen) {
                        WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                        attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                        getActivity().getWindow().setAttributes(attrs);
                        if (android.os.Build.VERSION.SDK_INT >= 14) {
                            //noinspection all
                            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                        }
                    } else {
                        WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                        attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                        getActivity().getWindow().setAttributes(attrs);
                        if (android.os.Build.VERSION.SDK_INT >= 14) {
                            //noinspection all
                            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                        }
                    }

                }
            });
            mWebView.setWebChromeClient(webChromeClient);
            //noinspection all
            try {
                String webUrl = null;
                if (screenModel != null) {
                    if (screenModel.getLocalizedURL() == null)
                        webUrl = screenModel.getURL();
                    else {
                        webUrl = localizationHelper.getLocalizedTitle(screenModel.getLocalizedURL());
                        if (webUrl == null || webUrl.isEmpty())
                            webUrl = screenModel.getURL();
                    }
                    if (screenModel.getScriptPath() != null && !screenModel.getScriptPath().equalsIgnoreCase("")) {
                        String scriptUrl = screenModel.getScriptPath();
                        RxJavaRequestHelper rxJavaRequestHelper = new RxJavaRequestHelper(getActivity(), sharedPrefHelper);
                        webScript = rxJavaRequestHelper.getAPIService().getScript(scriptUrl).execute().body();
                    }

                } else if (webContent != null) {
                    webUrl = webContent.url;
                }
                boolean isRestored = false;
                if (!isRestored && webUrl != null)
                    mWebView.loadUrl(webUrl);
            } catch (Exception e) {
                if (isProgressViewEnabled)
                    legacyProgressViewHelper.dismiss();
                Toast.makeText(getActivity(), getString(R.string.common_error), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            mWebView.setOnKeyListener(new OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) && mWebView.canGoBack()) {
                        mWebView.goBack();
                        return true;
                    }
                    return false;
                }
            });
        } else {
            Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.please_check_your_internet_connection), Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (relLayout != null) {
            bannerHelper.addBannerAd(relLayout, mWebView);
        }
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mWebView.stopLoading();
                    ((AudioManager) getActivity().getSystemService(
                            Context.AUDIO_SERVICE)).requestAudioFocus(
                            new OnAudioFocusChangeListener() {
                                @Override
                                public void onAudioFocusChange(int focusChange) {
                                }
                            }, AudioManager.STREAM_MUSIC,
                            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                    if (mWebView.canGoBack())
                        mWebView.goBack();
                    else {
                        mWebView.removeAllViews();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        if (isProgressViewEnabled)
            if (legacyProgressViewHelper != null)
                legacyProgressViewHelper.dismiss();
        super.onDestroyView();
    }

    @Override
    public void onPageStarted(String s, Bitmap bitmap) {
        isPageLoading = true;

    }

    @Override
    public void onPageFinished(String s) {
        isPageLoading = false;
        if (isProgressViewEnabled)
            legacyProgressViewHelper.dismiss();

    }

    @Override
    public void onPageError(int i, String s, String s1) {
        isPageLoading = false;
        if (isProgressViewEnabled)
            legacyProgressViewHelper.dismiss();

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
        if (mimeType.equals(PDF_MIME_TYPE)) {
            if (getActivity() != null) {
                Intent i = new Intent(getActivity(), GenericActivity.class);
                i.putExtra("pdf_url", url);
                startActivity(i);
                mWebView.goBack();
            }
        } else if (url.startsWith("data:image/png;base64")) {
            String imageB64 = url.substring(22);
            byte[] decodedStr = Base64.decode(imageB64, Base64.NO_WRAP);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedStr, 0, decodedStr.length);
            Intent i = new Intent(Intent.ACTION_VIEW, getImageUri(requireContext(), bitmap));
            startActivity(i);
            Toast.makeText(getActivity(), getResources().getString(R.string.web_module_redirecting_for_download), Toast.LENGTH_LONG).show();
            mWebView.goBack();
        } else {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);
            Toast.makeText(getActivity(), getResources().getString(R.string.web_module_redirecting_for_download), Toast.LENGTH_LONG).show();
            mWebView.goBack();
        }
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "image", null);
        return Uri.parse(path);
    }

    @Override
    public void onExternalPageRequest(String s) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FCR) {

            if (Build.VERSION.SDK_INT >= 21) {
                Uri[] results = null;
                //checking if response is positive
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    if (null == mUMA) {
                        return;
                    }
                    if (data == null || data.getData() == null) {
                        if (mCM != null) {
                            results = new Uri[]{Uri.parse(mCM)};
                        }
                    } else {
                        String dataString = data.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                        } else {
                            if (multiple_files) {
                                if (data.getClipData() != null) {
                                    final int numSelectedFiles = data.getClipData().getItemCount();
                                    results = new Uri[numSelectedFiles];
                                    for (int i = 0; i < numSelectedFiles; i++) {
                                        results[i] = data.getClipData().getItemAt(i).getUri();
                                    }
                                }
                            }
                        }
                    }
                }
                mUMA.onReceiveValue(results);
                mUMA = null;
            } else {
                if (null == mUM) return;
                Uri result = data == null || resultCode != AppCompatActivity.RESULT_OK ? null : data.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        } else {

            mWebView.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible && isPageLoading && isProgressViewEnabled)
            legacyProgressViewHelper.show();
    }

    private void checkInterstitialAd() {
        if (AdManager.getInstance().isInterstitialAdReady() && !SharedApplication.isInterstitialShown) {
            AdManager.getInstance().showInterstitialAd(new AdManager.InterstitialAdCallBack() {
                @Override
                public void onAdClosed() {

                }
            });
        } else if (networkHelper.isConnected()
                && !DynamicConstants.MobiRoller_Stage
                && sharedPrefHelper.getIsAdmobInterstitialAdEnabled()
                && sharedPrefHelper.getInterstitialClickCount() >= sharedPrefHelper.getInterstitialClickInterval()
                && sharedPrefHelper.getInterstitialTimer(new Date())
                && sharedPrefHelper.getInterstitialMultipleDisplayEnabled()) {
            if (AdManager.getInstance().isInterstitialAdReady()) {
                AdManager.getInstance().showInterstitialAd(new AdManager.InterstitialAdCallBack() {
                    @Override
                    public void onAdClosed() {

                    }
                });
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == mGeoLocationRequestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mGeoLocationCallBack != null) {
                    mGeoLocationCallBack.invoke(mGeoLocationRequestOrigin, true, true);
                }

            } else {
                if (mGeoLocationCallBack != null) {
                    mGeoLocationCallBack.invoke(mGeoLocationRequestOrigin, false, false);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private boolean checkFilePermission() {
        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            return false;
        } else {
            return true;
        }
    }

}
