package com.mobiroller.core.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by OKN on 07.02.2017.
 *
 * Api level 23 altı için scroll change listener
 * amacıyla kullanılan yardımcı layout sınıfı
 * Min level 23+ olduğu zaman kaldırılabilir
 */

public class CustomHorizontalScrollView  extends ScrollView {

    public OnScrollChangedListener mOnScrollChangedListener;

    public CustomHorizontalScrollView(Context context) {
        super(context);
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener){
        this.mOnScrollChangedListener = onScrollChangedListener;
    }

    public interface OnScrollChangedListener{
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

}
