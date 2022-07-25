package com.mobiroller.core.viewholders.forms;

import android.app.Activity;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.coreui.models.TableItemsModel;
import com.mobiroller.core.models.response.UserProfileElement;

/**
 * Created by ealtaca on 6/14/17.
 */

public abstract class FormBaseViewHolder extends RecyclerView.ViewHolder {

    public FormBaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(TableItemsModel tableItemsModel, LocalizationHelper localizationHelper, Activity activity, @ColorInt int color);

    public abstract void bind(UserProfileElement userProfileItem, LocalizationHelper localizationHelper, Activity activity, @ColorInt int color);

    public abstract String getValue();

    public abstract String getId();

    public abstract boolean isImage();

    public abstract boolean isValid();

    public abstract byte[] getImage();

    public abstract void clear();

    public abstract void setValue(String value);
}
