package com.mobiroller.core.coreui.views.legacy;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mobiroller.core.R;
import com.mobiroller.core.R2;
import com.mobiroller.core.coreui.Theme;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MobirollerTextInputEditText extends ConstraintLayout {

    @BindView(R2.id.text_input_layout)
    TextInputLayout textInputLayout;
    @BindView(R2.id.text_input_edit_text)
    TextInputEditText textInputEditText;

    private int maxLength;

    public MobirollerTextInputEditText(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MobirollerTextInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MobirollerTextInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MobirollerTextInputEditText, 0, 0);
        try {
            maxLength = a.getInteger(R.styleable.MobirollerTextInputEditText_inputEditTextMaxCount, 0);
        } finally {
            inflate(getContext(), R.layout.mobiroller_text_input_edit_text, this);
            ButterKnife.bind(this);

            setTheme();
            a.recycle();
        }
    }

    public void setTheme() {

        textInputEditText.setHintTextColor(Theme.primaryColor);
        textInputEditText.setTextColor(Theme.primaryColor);
        textInputLayout.setBoxBackgroundColor(Theme.primaryColor);

        setMaxLength(maxLength);

    }

    public void setMaxLength(int maxLength) {
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(maxLength);
        textInputEditText.setFilters(filters);
    }

}
