package com.mobiroller.core.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobiroller.core.R2;
import com.mobiroller.core.coreui.Theme;
import com.mobiroller.core.coreui.helpers.ColorHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ealtaca on 11/16/17.
 */
public class HeaderView extends LinearLayout {

    @BindView(R2.id.name)
    TextView name;

    @BindView(R2.id.last_seen)
    TextView lastSeen;

    public HeaderView(Context context) {
        super(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        name.setTextColor(ColorHelper.isColorDark(Theme.primaryColor) ? Color.WHITE : Color.BLACK);
        lastSeen.setTextColor(ColorHelper.isColorDark(Theme.primaryColor) ? Color.WHITE : Color.BLACK);
    }

    public void bindTo(String name, String lastSeen) {
        this.name.setText(name);
        this.lastSeen.setText(lastSeen);
        if (lastSeen == null || lastSeen.isEmpty() )
            this.lastSeen.setVisibility(GONE);
    }

    public void setTextSize(float size) {
        name.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }
}