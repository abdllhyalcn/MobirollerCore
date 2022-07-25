package com.mobiroller.core.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.mobiroller.core.SharedApplication;
import com.mobiroller.core.models.RssModel;
import com.mobiroller.core.R;

import java.util.List;

/**
 * Created by ealtaca on 20.12.2016.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private Activity mContext;
    private List<Object> rssModelList;
//    private ArrayList<RssModel> rssModels;
    private Context app;
    private String screenId;
    ImageView rssAvatar;

    public ViewPagerAdapter(Activity mContext, List<Object> rssModelList,String screenId) {
        this.mContext = mContext;
        this.rssModelList = rssModelList;
//        this.rssModels = rssModels;
        this.screenId = screenId;
        app = SharedApplication.context;
    }

    @Override
    public int getCount() {
        return rssModelList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view ==  object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.rss_header_item, container, false);
        rssAvatar = itemView.findViewById(R.id.rss_list_image);
        final TextView rssTitle = itemView.findViewById(R.id.rss_list_title);
        final RssModel rssModel = (RssModel) rssModelList.get(position);
        rssTitle.setText(Html.fromHtml(rssModel.getTitle()));
        if(rssModel.getImage()!=null && !rssModel.getImage().equalsIgnoreCase(""))
        {
            Glide.with(mContext)
                    .load(rssModel.getImage())
                    .apply(new RequestOptions().placeholder(R.drawable.no_image))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            rssAvatar.setImageResource(R.drawable.no_image);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(rssAvatar);
        }else
            Glide.with(mContext)
                    .load(R.drawable.no_image)
                    .into(rssAvatar);

        rssTitle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                rssTitle.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                float lineHeight = rssTitle.getLineHeight();
                int maxLines = (int) (rssTitle.getHeight() / lineHeight);

                if (rssTitle.getLineCount() != maxLines) {
                    rssTitle.setLines(maxLines);
                }

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

    public View getCurrentItemImageView()
    {

        return rssAvatar;
    }
}
