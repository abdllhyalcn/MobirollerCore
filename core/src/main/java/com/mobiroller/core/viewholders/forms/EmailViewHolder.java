package com.mobiroller.core.viewholders.forms;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.core.graphics.drawable.DrawableCompat;

import com.mobiroller.core.R2;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.coreui.models.TableItemsModel;
import com.mobiroller.core.models.response.UserProfileElement;
import com.mobiroller.core.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ealtaca on 6/14/17.
 */

public class EmailViewHolder extends FormBaseViewHolder {

    private TableItemsModel tableItemsModel;

    private UserProfileElement userProfileItem;
    @BindView(R2.id.form_item_title)
    TextView title;

    @BindView(R2.id.form_item_email_main_layout)
    LinearLayout emailMainLayout;

    @BindView(R2.id.form_item_email)
    TextView email;

    @BindView(R2.id.form_item_email_icon)
    ImageView emailIcon;

    private int actionBarColor;
    public EmailViewHolder(View itemView,int actionBarColor) {
        super(itemView);

        this.actionBarColor = actionBarColor;
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(final TableItemsModel tableItemsModel, final LocalizationHelper localizationHelper, final Activity activity, @ColorInt int color) {
        this.tableItemsModel = tableItemsModel;

        DrawableCompat.setTint(emailIcon.getDrawable(), actionBarColor);
        title.setText(localizationHelper.getLocalizedTitle(tableItemsModel.getTitle()));
        email.setText(localizationHelper.getLocalizedTitle(tableItemsModel.getValue()));
        emailMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:" + email.getText().toString()));
                activity.startActivity(Intent.createChooser(i, activity.getString(R.string.send_email)));
            }
        });
    }

    @Override
    public void bind(UserProfileElement userProfileItem, LocalizationHelper localizationHelper, Activity activity, int color) {

        this.userProfileItem = userProfileItem;
    }

    @Override
    public String getValue() {
        return email.getText().toString().trim();
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

    }

    @Override
    public void setValue(String value) {
        email.setText(value);
    }

}
