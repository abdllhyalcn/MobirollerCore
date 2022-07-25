package com.mobiroller.core.helpers;

import android.app.Activity;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobiroller.core.SharedApplication;
import com.mobiroller.core.coreui.helpers.LocaleHelper;
import com.mobiroller.core.coreui.models.ScreenModel;
import com.mobiroller.core.util.ImageManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Created by ealtaca on 26.05.2017.
 */

public class ComponentHelper {

    private ScreenHelper screenHelper;
    SharedPrefHelper sharedPrefHelper;

    public ComponentHelper(ScreenHelper screenHelper, SharedPrefHelper sharedPrefHelper) {
        this.screenHelper = screenHelper;
        this.sharedPrefHelper = sharedPrefHelper;
    }

    private int lineCount = 0;

    // Returns height of image

    public void setMainImage(Activity context, ImageView view, ScreenModel screenModel) {
        if(screenModel!=null && screenModel.getMainImageName()!=null && screenModel.getMainImageName().getImageURL()!=null)
            ImageManager.loadImageView(context,screenModel.getMainImageName().getImageURL(),view);
    }


    public void setRadioTitleText(Activity context, TextView textView, ScreenModel jObj) {
        String text = jObj.getContentText();
        String mainText ;
        textView.invalidate();
        if (text.contains("<" + LocaleHelper.getLocale(SharedApplication.context).toUpperCase() + ">")) {
            String[] parts = text.split("<" + LocaleHelper.getLocale(SharedApplication.context).toUpperCase() + ">");
            int position = parts.length - 2;
            mainText = parts[position];
        } else if (text.contains("<" + sharedPrefHelper.getDefaultLang().toUpperCase() + ">")) {
            String[] parts = text.split("<" + sharedPrefHelper.getDefaultLang().toUpperCase() + ">");
            int position = parts.length - 2;
            mainText = parts[position];
        } else {
            mainText = text;
        }
        setTextView(textView, mainText, context);

        if (!jObj.getContentText().equals("null")) {
            int textColor = screenHelper.setColorUnselected(jObj.getContentTextColor());
            textView.setTextColor(textColor);
        }
    }

    // Returns height of textView
    @SuppressWarnings("static-access")
    public void setMainTextView(Activity context, TextView textView, ScreenModel screenModel) {
        String text = screenModel.getContentText();
        List<String> localeCodes = Collections.singletonList("");
        String mainText ;
        if (sharedPrefHelper.getLocaleCodes() != null) {
            localeCodes = Arrays.asList(sharedPrefHelper.getLocaleCodes().split(","));
        }
        textView.invalidate();
        if (text.contains("<" + LocaleHelper.getLocale(SharedApplication.context).toUpperCase() + ">")) {
            String[] parts = text.split("<" + LocaleHelper.getLocale(SharedApplication.context).toUpperCase() + ">");
            int position = parts.length - 2;
            mainText = parts[position];
        } else if (localeCodes.contains(LocaleHelper.getLocale(SharedApplication.context).toUpperCase())) {
            return;
        } else if (text.contains("<" + sharedPrefHelper.getDefaultLang().toUpperCase() + ">")) {
            String[] parts = text.split("<" + sharedPrefHelper.getDefaultLang().toUpperCase() + ">");
            int position = parts.length - 2;
            mainText = parts[position];
        } else {
            mainText = text;
        }
        setTextView(textView, mainText, context);
        int height = textView.getLineCount() * textView.getLineHeight();

        if (screenModel.getContentFontSize() != 0) {
            textView.setTextSize(screenModel.getContentFontSize());
        }

        if (height > screenHelper.getHeightForDevice(screenModel.getContentTextHeight(), context) || height == 0) {
            textView.setHeight(screenHelper.getHeightForDevice(screenModel.getContentTextHeight(), context));
            lineCount = screenModel.getContentTextHeight();
        }

        if (screenModel.getContentTextBackColor() != null) {
            int backgroundColor = screenHelper.setColorUnselected(screenModel.getContentTextBackColor());
            textView.setBackgroundColor(backgroundColor);
        }
        int textColor = 0;
        if (screenModel.getContentTextColor() != null) {
            textColor = screenHelper.setColorUnselected(screenModel.getContentTextColor());
        }
        textView.setTextColor(textColor);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.getHeight();
    }

    private void setTextView(final TextView textView, String text, final Activity activity) {
        textView.setText(text);
        textView.post(new Runnable() {

            @Override
            public void run() {
                if (lineCount == 0) {
                    while (textView.getLineCount() == 0 && !textView.getText().toString().isEmpty()) {
                    }
                    lineCount = textView.getLineCount();
                    textView.setHeight((lineCount * textView.getLineHeight()) + screenHelper.getHeightForDevice(10, activity)); // adding 10 for padding
                }

            }
        });
    }
}
