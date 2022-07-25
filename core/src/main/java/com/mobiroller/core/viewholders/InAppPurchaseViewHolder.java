package com.mobiroller.core.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobiroller.core.R2;
import com.mobiroller.core.models.InAppPurchaseProduct;
import com.mobiroller.core.R;
import com.mobiroller.core.util.ImageManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InAppPurchaseViewHolder extends RecyclerView.ViewHolder{

    @BindView(R2.id.image)
    ImageView image;

    @BindView(R2.id.title)
    TextView title;

    public InAppPurchaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Context context, InAppPurchaseProduct product)
    {
        title.setText(product.title);
        if(product.productImages.size()>0)
            ImageManager.loadImageViewInAppPurchase(context,product.productImages.get(0).imageUrl,image);
        else
            Glide.with(context).load(R.drawable.no_image).into(image);
    }


}
