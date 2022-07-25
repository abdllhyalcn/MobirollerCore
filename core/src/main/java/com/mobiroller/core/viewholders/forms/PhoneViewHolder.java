package com.mobiroller.core.viewholders.forms;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
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

public class PhoneViewHolder extends FormBaseViewHolder {

    private TableItemsModel tableItemsModel;

    private UserProfileElement userProfileItem;
    @BindView(R2.id.form_item_title)
    TextView title;
    @BindView(R2.id.form_item_phone)
    TextView phone;
    @BindView(R2.id.form_item_phone_icon)
    ImageView phoneIcon;

    @BindView(R2.id.form_item_phone_main_layout)
    LinearLayout phoneMainLayout;

    private int actionBarColor;

    public PhoneViewHolder(View itemView, int actionBarColor) {
        super(itemView);
        this.actionBarColor = actionBarColor;
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(final TableItemsModel tableItemsModel, final LocalizationHelper localizationHelper, final Activity activity, @ColorInt int color) {
        this.tableItemsModel = tableItemsModel;
        title.setText(localizationHelper.getLocalizedTitle(tableItemsModel.getTitle()));
        phone.setText(localizationHelper.getLocalizedTitle(tableItemsModel.getValue()));

        DrawableCompat.setTint(phoneIcon.getDrawable(), actionBarColor);
        phoneMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uri = "tel:" + localizationHelper.getLocalizedTitle(tableItemsModel.getValue());
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                activity.startActivity(intent);
            }
        });
    }

    public void bindEmergency(final TableItemsModel tableItemsModel, final LocalizationHelper localizationHelper, final Activity activity, @ColorInt int color) {
        this.tableItemsModel = tableItemsModel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(color!=-1) {
                phoneIcon.setImageTintList(ColorStateList.valueOf(color));
                title.setTextColor(color);
                phone.setTextColor(color);
            }
            else
                phoneIcon.setImageTintList(ColorStateList.valueOf(actionBarColor));
        }
        title.setText(localizationHelper.getLocalizedTitle(tableItemsModel.getTitle()));
        phone.setText(tableItemsModel.getPhoneNumber());

        phoneMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uri = "tel:" + tableItemsModel.getPhoneNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                activity.startActivity(intent);
            }
        });
    }
    @Override
    public void bind(UserProfileElement userProfileItem, LocalizationHelper localizationHelper, Activity activity, int color) {

        this.userProfileItem = userProfileItem;
    }

    @Override
    public String getValue() {
        return phone.getText().toString();
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
        phone.setText(value);
    }
}
