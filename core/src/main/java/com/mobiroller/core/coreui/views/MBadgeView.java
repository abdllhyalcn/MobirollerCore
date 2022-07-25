package com.mobiroller.core.coreui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.mobiroller.core.R;

public class MBadgeView extends ConstraintLayout {

    TextView textView;
    RelativeLayout mainLayout;

    private int textColor;
    private int backgroundColor;
    private String text = "";

    public MBadgeView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MBadgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MBadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
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

            inflate(getContext(), R.layout.m_badge_view, this);
            textView = findViewById(R.id.badge_text);
            mainLayout = findViewById(R.id.main_layout);

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
    }
}
