package com.mobiroller.core.viewholders.forms;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.core.widget.CompoundButtonCompat;

import com.mobiroller.core.R2;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.coreui.models.TableItemsModel;
import com.mobiroller.core.models.response.UserProfileElement;
import com.mobiroller.core.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ealtaca on 6/14/17.
 * <p>
 * Form
 */

public class CheckViewHolder extends FormBaseViewHolder {

    private TableItemsModel tableItemsModel;
    private UserProfileElement userProfileItem;

    @BindView(R2.id.form_item_title)
    TextView title;
    @BindView(R2.id.form_item_check_box)
    Switch checkBox;
    public boolean isChecked = false;

    public CheckViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                isChecked = b;
            }
        });
    }

    @Override
    public void bind(TableItemsModel tableItemsModel, LocalizationHelper localizationHelper, Activity activity, @ColorInt int color) {
        this.tableItemsModel = tableItemsModel;
        title.setText(localizationHelper.getLocalizedTitle(tableItemsModel.getTitle()));
        title.setTextColor(color);
        checkBox.setTextColor(color);
        int states[][] = {{android.R.attr.state_checked}, {}};
        int colors[] = {Color.WHITE, color, Color.WHITE};
        CompoundButtonCompat.setButtonTintList(checkBox, new ColorStateList(states, colors));
        checkBox.setText(localizationHelper.getLocalizedTitle(tableItemsModel.getTitle()));
        ColorStateList thumbStates = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                },
                colors
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkBox.setThumbTintList(thumbStates);
        }
        if (Build.VERSION.SDK_INT >= 24) {
            ColorStateList trackStates = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_enabled},
                            new int[]{}
                    },
                    colors
            );
            checkBox.setTrackTintList(trackStates);
            checkBox.setTrackTintMode(PorterDuff.Mode.OVERLAY);
        }
    }

    @Override
    public void bind(UserProfileElement userProfileItem, LocalizationHelper localizationHelper, Activity activity, int color) {
        this.userProfileItem = userProfileItem;
        title.setText(localizationHelper.getLocalizedTitle(userProfileItem.title));
        title.setTextColor(color);
        checkBox.setTextColor(color);
        int states[][] = {{android.R.attr.state_checked}, {}};
        int colors[] = {Color.WHITE, color, Color.WHITE};
        CompoundButtonCompat.setButtonTintList(checkBox, new ColorStateList(states, colors));
        checkBox.setText(localizationHelper.getLocalizedTitle(userProfileItem.title));
        ColorStateList thumbStates = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                },
                colors
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkBox.setThumbTintList(thumbStates);
        }
        if (Build.VERSION.SDK_INT >= 24) {
            ColorStateList trackStates = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_enabled},
                            new int[]{}
                    },
                    colors
            );
            checkBox.setTrackTintList(trackStates);
            checkBox.setTrackTintMode(PorterDuff.Mode.OVERLAY);
        }
        if (userProfileItem.subType != null && userProfileItem.subType.equalsIgnoreCase("check")) {
            if (userProfileItem.value != null && (userProfileItem.value.equalsIgnoreCase("1") || userProfileItem.value.equalsIgnoreCase("yes")))
                checkBox.setChecked(true);
            else
                checkBox.setChecked(false);
        }
    }

    @Override
    public String getValue() {
        if (isChecked)
            return "1";
        else
            return "0";
    }

    @Override
    public String getId() {
        if (tableItemsModel != null)
            return tableItemsModel.getId();
        return userProfileItem.id;
    }

    @Override
    public byte[] getImage() {
        return null;
    }

    @Override
    public boolean isImage() {
        return false;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void clear() {
        isChecked = false;
        checkBox.setChecked(false);
    }

    @Override
    public void setValue(String value) {
        if (value != null && (value.equalsIgnoreCase("1") || value.equalsIgnoreCase("yes")))
        checkBox.setChecked(true);
    else
        checkBox.setChecked(false);
    }


}