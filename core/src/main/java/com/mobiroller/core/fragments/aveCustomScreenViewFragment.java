package com.mobiroller.core.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mobiroller.core.R;
import com.mobiroller.core.R2;
import com.mobiroller.core.util.ImageManager;
import com.mobiroller.core.views.VideoEnabledWebChromeClient;
import com.mobiroller.core.views.VideoEnabledWebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.delight.android.webview.AdvancedWebView;

@SuppressLint("NewApi")
public class aveCustomScreenViewFragment extends BaseModuleFragment implements AdvancedWebView.Listener {

    @BindView(R2.id.web_view)
    VideoEnabledWebView mWebView;
    @BindView(R2.id.videoLayout)
    RelativeLayout videoLayout;
    @BindView(R2.id.web_layout)
    RelativeLayout webLayout;
    @BindView(R2.id.main_layout)
    RelativeLayout mainLayout;
    @BindView(R2.id.inner_layout)
    RelativeLayout innerLayout;
    @BindView(R2.id.nonVideoLayout)
    LinearLayout nonVideoLayout;

    private String htmlContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_web_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        hideToolbar(view.findViewById(R.id.toolbar_top));
        loadUi();
        setWebViewSettings();
        htmlContent = screenModel.getContentHtml();

        mWebView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null);
        return view;
    }

    private int getScale() {
        Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = (double) width / 400d;
        val = val * 100d;
        return val.intValue();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (nonVideoLayout != null) {
            bannerHelper.addBannerAd(mainLayout, innerLayout);
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void setWebViewSettings() {
        mWebView.setPadding(0, 0, 0, 0);
        mWebView.setInitialScale(getScale());
        mWebView.clearHistory();
        mWebView.clearFormData();
        mWebView.clearCache(true);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(PluginState.ON);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.setListener(getActivity(),this);

        VideoEnabledWebChromeClient webChromeClient = new VideoEnabledWebChromeClient(webLayout, videoLayout, null, mWebView) // See all available constructors...
        {
            // Subscribe to standard events, such as onProgressChanged()...
            @Override
            public void onProgressChanged(WebView view, int progress)
            {
                // Your code...
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, android.os.Message resultMsg) {
                WebView.HitTestResult result = view.getHitTestResult();
                String data = result.getExtra();
                Context context = view.getContext();
                if (data != null) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
                    context.startActivity(browserIntent);
                    return true;
                }
                return false;
            }
        };
        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback()
        {
            @Override
            public void toggledFullscreen(boolean fullscreen)
            {
                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                if (fullscreen)
                {
                    try {
                        View view = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
                        view.findViewById(R.id.toolbar_top).setVisibility(View.GONE);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getActivity().getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14)
                    {
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }
                }
                else
                {

                    try {
                        View view = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
                        view.findViewById(R.id.toolbar_top).setVisibility(View.VISIBLE);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getActivity().getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14)
                    {
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }

            }
        });
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setWebViewClient(new WebViewClient());
    }

    public void loadUi() {
        mWebView.setBackgroundColor(Color.argb(1, 0, 0, 0));
        ImageManager.loadBackgroundImageFromImageModel(mainLayout, screenModel.getBackgroundImageName());
    }

    @Override
    public void onDestroyView() {
        try {
            mWebView.destroy();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onDestroyView();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
        Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.web_module_redirecting_for_download), Toast.LENGTH_LONG).show();
        mWebView.goBack();
    }

    @Override
    public void onExternalPageRequest(String url) {

    }
}
