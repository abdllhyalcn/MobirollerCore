package com.mobiroller.core.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.snackbar.Snackbar;
import com.mobiroller.core.R2;
import com.mobiroller.core.constants.DynamicConstants;
import com.mobiroller.core.activities.aveWebView;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.coreui.Theme;
import com.mobiroller.core.coreui.helpers.ColorHelper;
import com.mobiroller.core.enums.FontStyle;
import com.mobiroller.core.helpers.FontSizeHelper;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.models.WebContent;
import com.mobiroller.core.R;

import java.util.Arrays;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by ealtaca on 17.03.2017.
 */

public class aveSettingsViewFragment extends BaseModuleFragment implements CompoundButton.OnCheckedChangeListener {

    @BindView(R2.id.language_textview)
    TextView languageTextView;
    @BindView(R2.id.language_layout)
    LinearLayout languageLayout;
    @BindView(R2.id.notification_switch)
    Switch notificationSwitch;
    @BindView(R2.id.notification_sound_switch)
    Switch notificationSoundSwitch;
    @BindView(R2.id.app_version_textview)
    TextView appVersionTextView;
    @BindView(R2.id.app_version_layout)
    LinearLayout appVersionLayout;
    @BindView(R2.id.setting_linear_layout)
    LinearLayout settingLinearLayout;
    Unbinder unbinder;
    @BindView(R2.id.text_size_textview)
    TextView textSizeTextview;
    @BindView(R2.id.text_size_layout)
    LinearLayout textSizeLayout;
    @BindView(R2.id.user_agreement_layout)
    LinearLayout userAgreementLayout;
    @BindView(R2.id.user_agreement_view_layout)
    View userAgreementViewLayout;

    @BindView(R2.id.main_layout)
    RelativeLayout mainLayout;
    @BindView(R2.id.overlay_layout)
    RelativeLayout overlayLayout;

    private String[] languageArray;
    private String[] languageCodeArray;
    private String[] localeCodes;
    private String language;
    private boolean notification;
    private boolean notificationSound;
    private Snackbar snackbar;
    private FontSizeHelper fontSizeHelper;
    private LocalizationHelper mLocalizationHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_setting,
                container, false);
        unbinder = ButterKnife.bind(this, view);
        mLocalizationHelper = UtilManager.localizationHelper();
        fontSizeHelper = new FontSizeHelper(getActivity());
        language = getResources().getString(R.string.locale);
        String languageCodes = sharedPrefHelper.getLocaleCodes();
        notification = sharedPrefHelper.getNotification();
        notificationSound = sharedPrefHelper.getNotificationSound();
        languageArray = getResources().getStringArray(R.array.supported_languages);
        languageCodeArray = getResources().getStringArray(R.array.supported_languages_language_codes);
        if (languageCodes == null)
            localeCodes = new String[0];
        else
            localeCodes = languageCodes.split(",");
        if (localeCodes.length <= 1) {
            languageLayout.setVisibility(View.GONE);
        }
        loadUiProperties();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        snackbar = Snackbar
                .make(settingLinearLayout, getString(R.string.settings_saved), Snackbar.LENGTH_SHORT)
                .setAction(getString(R.string.OK), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
    }

    private void loadUiProperties() {

        setLanguage();
        setFontSizeText();

        notificationSwitch.setChecked(notification);
        notificationSoundSwitch.setChecked(notificationSound);
        if (!notification)
            notificationSoundSwitch.setEnabled(false);

        notificationSwitch.setOnCheckedChangeListener(this);
        notificationSoundSwitch.setOnCheckedChangeListener(this);
        try {
            appVersionTextView.setText(getActivity().getPackageManager()
                    .getPackageInfo(getActivity().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            appVersionTextView.setText(getString(R.string.unknown));
            e.printStackTrace();
        }
        if (sharedPrefHelper.getIsUserAgremeentActive()) {
            userAgreementLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        WebContent webContent = new WebContent(getString(R.string.user_agreement_settings),String.valueOf(Uri.parse(mLocalizationHelper.getLocalizedTitle(sharedPrefHelper.getUserAgremeent()))),true);
                        Intent i = new Intent(getActivity(), aveWebView.class);
                        i.putExtra("webContent", webContent);
                        startActivity(i);
                        getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(mLocalizationHelper.getLocalizedTitle(sharedPrefHelper.getUserAgremeent())));
                        startActivity(i);
                    }
                }
            });
        } else {
            userAgreementViewLayout.setVisibility(View.GONE);
            userAgreementLayout.setVisibility(View.GONE);
        }
    }

    private void setLanguage() {
        for (int j = 0; j < languageCodeArray.length; j++) {
            if (languageCodeArray[j].equalsIgnoreCase(language)) {
                languageTextView.setText(languageArray[j]);
                break;
            }
        }
    }

    private String[] getLanguageList() {
        String[] languagesArray = new String[localeCodes.length];
        for (int i = 0; i < localeCodes.length; i++) {
            boolean isFound = false;
            for (int j = 0; j < languageCodeArray.length; j++) {
                if (languageCodeArray[j].equalsIgnoreCase(localeCodes[i])) {
                    languagesArray[i] = languageArray[j];
                    isFound = true;
                    break;
                }
            }
            if(!isFound) {
                languagesArray[i] = new Locale(localeCodes[i]).getDisplayLanguage(Locale.forLanguageTag(localeCodes[i]));
            }
        }
        return languagesArray;
    }

    private String[] getFontSizeList() {
        return getResources().getStringArray(R.array.text_size_list);
    }

    private void setFontSizeText() {
        int selectedItemOrder = fontSizeHelper.getFontOrder();
        if (selectedItemOrder == 0)
            textSizeTextview.setText(R.string.text_size_small);
        else if (selectedItemOrder == 1)
            textSizeTextview.setText(R.string.text_size_medium);
        else if (selectedItemOrder == 2)
            textSizeTextview.setText(R.string.text_size_big);
        else if (selectedItemOrder == 3)
            textSizeTextview.setText(R.string.text_size_very_big);
    }


    public void setNotification(boolean value) {
        notificationSoundSwitch.setEnabled(value);
        sharedPrefHelper.setNotification(value);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
        if (id == R.id.notification_switch) {
            setNotification(b);
        } else if (id == R.id.notification_sound_switch) {
            sharedPrefHelper.setNotificationSound(b);
        }
        snackbar.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void restartApplication() {
        Intent i = getActivity().getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getActivity().finish();
        getActivity().startActivity(i);
    }

    public void saveFontSelectedFontSize(int selectedItemOrder) {
        if (selectedItemOrder == 0)
            fontSizeHelper.setFontStyle(FontStyle.Small);
        else if (selectedItemOrder == 1)
            fontSizeHelper.setFontStyle(FontStyle.Medium);
        else if (selectedItemOrder == 2)
            fontSizeHelper.setFontStyle(FontStyle.Large);
        else if (selectedItemOrder == 3)
            fontSizeHelper.setFontStyle(FontStyle.XLarge);
    }


    @OnClick(R2.id.text_size_layout)
    public void textSizeLayoutOnClick() {
        final int textSizeOrder = fontSizeHelper.getFontOrder();
        new MaterialDialog.Builder(getActivity())
                .title(R.string.change_font_size)
                .items(getFontSizeList())
                .choiceWidgetColor(ColorStateList.valueOf(ColorHelper.isColorDark(Theme.primaryColor) ? Theme.primaryColor : Color.BLACK))
                .positiveColor(ColorStateList.valueOf(ColorHelper.isColorDark(Theme.primaryColor) ? Theme.primaryColor : Color.BLACK))
                .negativeColor(ColorStateList.valueOf(ColorHelper.isColorDark(Theme.primaryColor) ? Theme.primaryColor : Color.BLACK))
                .itemsCallbackSingleChoice(textSizeOrder, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, final int which, CharSequence text) {
                        if (which != textSizeOrder) {
                            if (!DynamicConstants.MobiRoller_Stage) {
                                new MaterialDialog.Builder(getActivity())
                                        .content(R.string.application_restart_info)
                                        .negativeText(R.string.cancel)
                                        .positiveText(R.string.OK)
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                                                saveFontSelectedFontSize(which);
                                                restartApplication();
                                            }
                                        })
                                        .show();
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.not_supported_on_preview), Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                    }
                })
                .negativeText(R.string.cancel)
                .positiveText(R.string.OK)
                .show();
    }

    @OnClick(R2.id.language_layout)
    public void languageLayoutOnClick() {
        int index = -1;
        try {
            index = Arrays.asList(localeCodes).indexOf(language);
        } catch (Exception e) {
        }
        final int finalIndex = index;
        new MaterialDialog.Builder(getActivity())
                .title(R.string.change_language)
                .items(getLanguageList())
                .choiceWidgetColor(ColorStateList.valueOf(ColorHelper.isColorDark(Theme.primaryColor) ? Theme.primaryColor : Color.BLACK))
                .positiveColor(ColorStateList.valueOf(ColorHelper.isColorDark(Theme.primaryColor) ? Theme.primaryColor : Color.BLACK))
                .negativeColor(ColorStateList.valueOf(ColorHelper.isColorDark(Theme.primaryColor) ? Theme.primaryColor : Color.BLACK))
                .itemsCallbackSingleChoice(finalIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (!language.equalsIgnoreCase(localeCodes[which])) {
                            if (!DynamicConstants.MobiRoller_Stage) {
                                UtilManager.sharedPrefHelper().put(Constants.DISPLAY_LANGUAGE,localeCodes[which].toLowerCase());
                                sharedPrefHelper.languageSetByUser();
                                restartApplication();
                            } else
                                Toast.makeText(getActivity(), getString(R.string.not_supported_on_preview), Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                })
                .negativeText(R.string.cancel)
                .positiveText(R.string.OK)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mainLayout != null) {
            bannerHelper.addBannerAd(mainLayout, overlayLayout);
        }
    }
}
