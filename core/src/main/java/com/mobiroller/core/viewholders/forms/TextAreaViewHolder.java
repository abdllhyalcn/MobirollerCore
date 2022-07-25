package com.mobiroller.core.viewholders.forms;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.ColorInt;
import com.mobiroller.core.R2;
import com.mobiroller.core.helpers.EditTextHelper;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.coreui.models.TableItemsModel;
import com.mobiroller.core.models.response.UserProfileElement;
import com.mobiroller.core.R;
import com.mobiroller.core.coreui.util.ColorUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ealtaca on 6/14/17.
 */

public class TextAreaViewHolder extends FormBaseViewHolder {

    private TableItemsModel tableItemsModel;

    private UserProfileElement userProfileItem;

    @BindView(R2.id.form_item_text_area)
    MaterialEditText textArea;

    private Activity activity;

    public TextAreaViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(TableItemsModel tableItemsModel, LocalizationHelper localizationHelper, Activity activity,@ColorInt int color) {
        this.tableItemsModel = tableItemsModel;
        this.activity = activity;
        textArea.setBaseColor(color);
        textArea.setPrimaryColor(color);
        textArea.setUnderlineColor(color);
        textArea.setFloatingLabelTextColor(color);
        textArea.setMetTextColor(color);
        textArea.setMetHintTextColor(ColorUtil.getLighterColor(color,0.3f));
        EditTextHelper.setCursorColor(textArea,color);
        textArea.setHint(localizationHelper.getLocalizedTitle(tableItemsModel.getTitle()));
        textArea.setFloatingLabelText(localizationHelper.getLocalizedTitle(tableItemsModel.getTitle()));
    }

    @Override
    public void bind(UserProfileElement userProfileItem, LocalizationHelper localizationHelper, Activity activity, int color) {
        this.userProfileItem = userProfileItem;
        this.activity = activity;
        textArea.setBaseColor(color);
        textArea.setPrimaryColor(color);
        textArea.setUnderlineColor(color);
        textArea.setFloatingLabelTextColor(color);
        textArea.setMetTextColor(color);
        textArea.setMetHintTextColor(ColorUtil.getLighterColor(color,0.3f));
        EditTextHelper.setCursorColor(textArea,color);
        textArea.setHint(localizationHelper.getLocalizedTitle(userProfileItem.title));
        textArea.setFloatingLabelText(localizationHelper.getLocalizedTitle(userProfileItem.title));
    }

    @Override
    public String getValue() {
        return textArea.getText().toString().trim();
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
        if(tableItemsModel!=null) {
            if (tableItemsModel.getMandatory().equalsIgnoreCase("YES")) {
                if (textArea.getText().toString().isEmpty()) {
                    textArea.setError(activity.getString(R.string.text_validation_message));
                    textArea.setErrorColor(Color.RED);
                    return false;
                } else
                    return true;
            } else
                return true;
        }else
        {
            if(userProfileItem.mandotory)
            {
                if(textArea.getText().toString().isEmpty())
                {
                    textArea.setError(activity.getString(R.string.text_validation_message));
                    textArea.setErrorColor(Color.RED);
                    return false;
                }else
                    return true;
            }else
                return true;
        }
    }

    @Override
    public void clear() {
        textArea.setText("");
    }

    @Override
    public void setValue(String value) {
        textArea.setText(value);
    }
}
