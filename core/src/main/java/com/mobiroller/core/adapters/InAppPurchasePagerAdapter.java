package com.mobiroller.core.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.mobiroller.core.helpers.InAppPurchaseHelper;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.models.InAppPurchaseProduct;
import com.mobiroller.core.R;
import com.mobiroller.core.util.ImageManager;
import com.mobiroller.core.util.MobirollerIntent;

import java.util.List;

public class InAppPurchasePagerAdapter extends PagerAdapter{

    private Activity mContext;
    private String mScreenId;
    private String screenType;
    private boolean mIsFromActivity;
    private List<InAppPurchaseProduct> mDataList;

    public InAppPurchasePagerAdapter(Activity mContext, List<InAppPurchaseProduct> dataList,String screenId,String screenType,boolean isFromActivity) {
        this.mContext = mContext;
        this.mScreenId= screenId;
        this.screenType= screenType;
        this.mDataList = dataList;
        this.mIsFromActivity= isFromActivity;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view ==  object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_in_app_purchase_view_pager_item, container, false);
        ImageView imageView = itemView.findViewById(R.id.image_view);
        TextView title = itemView.findViewById(R.id.title);
        TextView description = itemView.findViewById(R.id.description);
        Button moreButton= itemView.findViewById(R.id.button_more);
        InAppPurchaseProduct inAppPurchaseProduct = mDataList.get(position);
        ImageManager.loadImageViewInAppPurchase(mContext,inAppPurchaseProduct.productImages.get(0).imageUrl,imageView);
        title.setText(UtilManager.localizationHelper().getLocalizedTitle(inAppPurchaseProduct.title));
        description.setText(UtilManager.localizationHelper().getLocalizedTitle(inAppPurchaseProduct.description));
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobirollerIntent.startInAppPurchaseDetailActivity(mContext, InAppPurchaseHelper.getScreenProductList(mScreenId).get(position),mScreenId,screenType,mIsFromActivity);
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobirollerIntent.startInAppPurchaseDetailActivity(mContext, InAppPurchaseHelper.getScreenProductList(mScreenId).get(position),mScreenId,screenType,mIsFromActivity);
            }
        });
        container.addView(itemView);
        itemView.setTag("viewpager" + position);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
