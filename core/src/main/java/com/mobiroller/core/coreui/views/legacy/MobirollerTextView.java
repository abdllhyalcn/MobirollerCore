package com.mobiroller.core.coreui.views.legacy;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.mobiroller.core.R;
import com.mobiroller.core.coreui.enums.ColorEnum;
import com.mobiroller.core.coreui.enums.FontTypeEnum;

public class MobirollerTextView extends androidx.appcompat.widget.AppCompatTextView {


    private int color;
    private int size;

    public MobirollerTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MobirollerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MobirollerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MobirollerTextView, 0, 0);
        try {
            color = a.getInteger(R.styleable.MobirollerTextView_colorType, -1);
            size = a.getInteger(R.styleable.MobirollerTextView_sizeType, 0);
        } finally {
            setTheme();
            a.recycle();
        }
    }

    public void setTheme() {
        if(color != -1)
        setTextColor(ColorEnum.getResIdByResOrder(color));
        setTextSize(TypedValue.COMPLEX_UNIT_SP, FontTypeEnum.getFontSizeByResOrder(size));;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setTypeface(FontTypeEnum.getResIdByResOrder(size, getContext()));
    }

}
