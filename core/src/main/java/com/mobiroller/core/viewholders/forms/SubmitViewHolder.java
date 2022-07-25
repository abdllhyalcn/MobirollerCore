package com.mobiroller.core.viewholders.forms;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.mobiroller.core.R2;
import com.mobiroller.core.models.SubmitModel;
import com.mobiroller.core.R;

import org.greenrobot.eventbus.EventBus;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ealtaca on 6/14/17.
 */

public class SubmitViewHolder extends RecyclerView.ViewHolder {

    @BindView(R2.id.form_item_submit)
    CircularProgressButton submit;

    public SubmitViewHolder(View itemView, int textColor) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @OnClick(R2.id.form_item_submit)
    public void submit() {
        EventBus.getDefault().post(new SubmitModel());
    }

    public CircularProgressButton getSubmitButton() {
        return submit;
    }

}
