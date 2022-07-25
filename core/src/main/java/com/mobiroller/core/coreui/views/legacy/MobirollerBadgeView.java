package com.mobiroller.core.coreui.views.legacy;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.mobiroller.core.R;
import com.mobiroller.core.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MobirollerBadgeView extends ConstraintLayout {

    @BindView(R2.id.badge_text)
    MobirollerTextView textView;
    @BindView(R2.id.main_layout)
    RelativeLayout mainLayout;

    private int textColor;
    private int backgroundColor;
    private String text = "";

    public MobirollerBadgeView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MobirollerBadgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MobirollerBadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MobirollerBadgeView, 0, 0);
        try {
            textColor = a.getColor(R.styleable.MobirollerBadgeView_textColor,0);
            backgroundColor = a.getColor(R.styleable.MobirollerBadgeView_backgroundColor,0);
            text = a.getString(R.styleable.MobirollerBadgeView_text);
        } finally {

            inflate(getContext(), R.layout.mobiroller_badge_view, this);
            ButterKnife.bind(this);

            setTheme();
            a.recycle();
        }
    }

    public void setTheme() {
        GradientDrawable d = new GradientDrawable();
        d.setCornerRadius(16.0f);
        d.setColor(backgroundColor);
        mainLayout.setBackground(d);

        setText(text);
        textView.setTextColor(textColor);
    }

    public void setText(String text) {
        textView.setText(text);
        textView.setAllCaps(true);
    }
}
