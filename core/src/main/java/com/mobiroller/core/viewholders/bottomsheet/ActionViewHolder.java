package com.mobiroller.core.viewholders.bottomsheet;

import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobiroller.core.R2;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.models.bottomsheet.ActionModel;
import com.mobiroller.core.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ealtaca on 8/27/17.
 */

public class ActionViewHolder extends RecyclerView.ViewHolder {

    @BindView(R2.id.image)
    ImageView image;
    @BindView(R2.id.title)
    TextView title;

    public ActionViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(ActionModel actionModel) {
        image.setImageResource(actionModel.iconRes);
        title.setText(title.getContext().getResources().getText(actionModel.titleRes));

        if(actionModel.colorize) {
            image.setColorFilter(UtilManager.sharedPrefHelper().getActionBarColor(), PorterDuff.Mode.SRC_ATOP);
        }
    }

}
