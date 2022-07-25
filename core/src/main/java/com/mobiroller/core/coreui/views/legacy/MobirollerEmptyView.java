package com.mobiroller.core.coreui.views.legacy;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.mobiroller.core.R;
import com.mobiroller.core.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MobirollerEmptyView extends ConstraintLayout {

    @BindView(R2.id.empty_background_image_view)
    ImageView backgroundImageView;
    @BindView(R2.id.empty_image_view)
    ImageView imageView;
    @BindView(R2.id.empty_title_text_view)
    MobirollerTextView titleTextView;
    @BindView(R2.id.empty_description_text_view)
    MobirollerTextView descriptionTextView;
    @BindView(R2.id.no_content_image)
    ImageView noContentImageView;

    private int backgroundImageTintColor;
    private Drawable icon;
    private String title;
    private String description;
    private boolean isOval;
    private boolean showNoContent;

    public MobirollerEmptyView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MobirollerEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MobirollerEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MobirollerEmptyView, 0, 0);
        try {
            backgroundImageTintColor = a.getInteger(R.styleable.MobirollerEmptyView_backgroundImageTintColor, 0);
            title = a.getString(R.styleable.MobirollerEmptyView_title);
            description = a.getString(R.styleable.MobirollerEmptyView_description);
            int iconId = a.getResourceId(R.styleable.MobirollerEmptyView_emptyIcon, -1);
            icon = AppCompatResources.getDrawable(getContext(), iconId);
            isOval = a.getBoolean(R.styleable.MobirollerEmptyView_backgroundImageIsOval,false);
            showNoContent = a.getBoolean(R.styleable.MobirollerEmptyView_showNoContent,false);
        } finally {
            inflate(getContext(), R.layout.mobiroller_empty_view, this);
            ButterKnife.bind(this);

            setTheme();
            a.recycle();
        }
    }

    public void setTheme() {
        setTitle(title);
        setDescription(description);

        if(icon != null)
            imageView.setImageDrawable(icon);

        if(isOval) {
            backgroundImageView.setImageResource(R.drawable.circle_gray_background);
        }

        if(showNoContent) {
            noContentImageView.setVisibility(VISIBLE);
        } else
            noContentImageView.setVisibility(GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            backgroundImageView.setImageTintList(ColorStateList.valueOf(backgroundImageTintColor));
        }
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setDescription(String description) {
        if(description != null) {
            descriptionTextView.setText(description);
            descriptionTextView.setVisibility(VISIBLE);
        } else {
            descriptionTextView.setVisibility(GONE);
        }
    }

    public String getDescription() {
        return description;
    }
}
