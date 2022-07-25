package com.mobiroller.core.adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.coreui.models.GalleryModel;
import com.mobiroller.core.R;
import com.mobiroller.core.views.twowayview.StaggeredGridLayoutManager;
import com.mobiroller.core.views.twowayview.TwoWayLayoutManager;
import com.mobiroller.core.views.twowayview.TwoWayView;

import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<GalleryModel> galleryModels;
    private Context context;
    private final TwoWayView mRecyclerView;
    private final int mLayoutId;
    private static final int TYPE_FULL = 0;
    private static final int TYPE_HALF = 1;
    private static final int TYPE_QUARTER = 2;

    public ImageAdapter(Context mContext, List<GalleryModel> galleryModels, TwoWayView recyclerView, int mLayoutId, LocalizationHelper localizationHelper) {
        context = mContext;
        mRecyclerView = recyclerView;
        this.galleryModels = galleryModels;
        this.mLayoutId = mLayoutId;
    }

    @Override
    public int getItemViewType(int position) {
        final int modeEight = position % 8;
        switch (modeEight) {
            case 0:
            case 4:
                return TYPE_HALF;
            case 1:
            case 2:
            case 3:
            case 5:
                return TYPE_QUARTER;
        }
        return TYPE_FULL;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View itemView = null;
        if (mLayoutId == 5)
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_gallery_list_staggered_item, parent, false);
        else
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_gallery_list_item, parent, false);

        return new ImageAdapter.ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {

        boolean isVertical = (mRecyclerView.getOrientation() == TwoWayLayoutManager.Orientation.VERTICAL);
        final View itemView = holder.itemView;
        if (mLayoutId == 5) {
            int span = 0;
            switch (getItemViewType(position)) {
                case TYPE_FULL:
                    span = 3;
                    break;
                case TYPE_HALF:
                    span = 4;
                    break;
                case TYPE_QUARTER:
                    span = 2;
                    break;
            }
            final StaggeredGridLayoutManager.LayoutParams lp =
                    (StaggeredGridLayoutManager.LayoutParams) itemView.getLayoutParams();
            final ViewGroup.LayoutParams lp1 =
                    holder.imageView.getLayoutParams();
            if (!isVertical) {
                lp.span = span;
                itemView.setLayoutParams(lp);
            } else {
                lp.span = span;
                if (getItemViewType(position) == TYPE_QUARTER) {
                    int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 95, context.getResources().getDisplayMetrics());
                    lp.height = height;
                    lp1.height = height;
                }
                itemView.setLayoutParams(lp);
                holder.imageView.setLayoutParams(lp1);
            }
        } else if (mLayoutId == 1) {
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 153, context.getResources().getDisplayMetrics());
            final RecyclerView.LayoutParams lp =
                    (RecyclerView.LayoutParams) itemView.getLayoutParams();
            lp.height = height;
            itemView.setLayoutParams(lp);
        } else if (mLayoutId == 2) {
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 107, context.getResources().getDisplayMetrics());
            final RecyclerView.LayoutParams lp =
                    (RecyclerView.LayoutParams) itemView.getLayoutParams();
            lp.height = height;
            double padding_in_dp = 1.35;  // 6 dps
            final float scale = context.getResources().getDisplayMetrics().density;
            int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
            itemView.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
            itemView.setLayoutParams(lp);
        } else if (mLayoutId == 3) {
            holder.gradientLayout.setVisibility(View.VISIBLE);
        } else if (mLayoutId == 4) {
            holder.gradientLayout.setVisibility(View.VISIBLE);
            if (galleryModels.get(position).getCaption() != null)
                holder.captionTextview.setText(UtilManager.localizationHelper().getLocalizedTitle(galleryModels.get(position).getCaption()));
        }

        Glide.with(context)
                .load(galleryModels.get(position).getURL())
                .apply(RequestOptions.centerCropTransform().placeholder(R.drawable.no_image))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return galleryModels.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        RelativeLayout gradientLayout;
        TextView captionTextview;

        ImageViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image);
            gradientLayout = view.findViewById(R.id.gallery_gradient);
            captionTextview = view.findViewById(R.id.gallery_caption);

        }
    }




}