package com.mobiroller.core.viewholders.forms;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.RatingBar;
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

public class StartRatingViewHolder extends FormBaseViewHolder {

    private TableItemsModel tableItemsModel;

    private LocalizationHelper localizationHelper;

    private Activity activity;

    @BindView(R2.id.form_item_title)
    TextView title;
    @BindView(R2.id.form_item_rating_bar)
    RatingBar ratingBar;

    public StartRatingViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(TableItemsModel tableItemsModel, LocalizationHelper localizationHelper, Activity activity, @ColorInt int color) {
        this.tableItemsModel = tableItemsModel;
        this.localizationHelper = localizationHelper;
        this.activity = activity;
        title.setText(localizationHelper.getLocalizedTitle(tableItemsModel.getTitle()));
        title.setTextColor(color);
        ratingBar.setNumStars(Integer.parseInt(tableItemsModel.getRatingLevel()));
        ratingBar.setStepSize(1);
        Drawable progress = ratingBar.getProgressDrawable();
        DrawableCompat.setTint(progress, color);
    }

    @Override
    public void bind(UserProfileElement userProfileItem, LocalizationHelper localizationHelper, Activity activity, int color) {

    }

    @Override
    public String getValue() {
        return String.valueOf(ratingBar.getRating());
    }

    @Override
    public String getId() {
        return tableItemsModel.getId();
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
        if (tableItemsModel.getMandatory().equalsIgnoreCase("yes"))
            if (ratingBar.getRating() == 0) {
                title.setText(Html.fromHtml(localizationHelper.getLocalizedTitle(tableItemsModel.getTitle()) + " - " + "<font color='red'>"+activity.getResources().getString(R.string.required_field)+"</font>"));
                return false;
            }
        title.setText(localizationHelper.getLocalizedTitle(tableItemsModel.getTitle()));
        return true;
    }

    @Override
    public void clear() {
        title.setText(localizationHelper.getLocalizedTitle(tableItemsModel.getTitle()));
        ratingBar.setRating(0);
    }

    @Override
    public void setValue(String value) {
    }
}
