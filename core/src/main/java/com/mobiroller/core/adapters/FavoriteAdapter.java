package com.mobiroller.core.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobiroller.core.activities.ActivityHandler;
import com.mobiroller.core.activities.ImagePagerActivity;
import com.mobiroller.core.activities.aveRssContentViewPager;
import com.mobiroller.core.helpers.FavoriteHelper;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.models.FavoriteModel;
import com.mobiroller.core.coreui.models.GalleryModel;
import com.mobiroller.core.R;

import java.util.ArrayList;

import static com.mobiroller.core.activities.ImagePagerActivity.TOOLBAR_TITLE;
import static com.mobiroller.core.constants.Constants.KEY_RSS_TITLE;

/**
 * Created by ealtaca on 6/30/18.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private Activity activity;
    private ArrayList<FavoriteModel> dataList;
    private ArrayList<FavoriteModel> dataListFiltered;
    private FavoriteHelper mFavoriteHelper;


    public FavoriteAdapter(Activity activity) {
        this.activity = activity;
        mFavoriteHelper = new FavoriteHelper(activity);
        dataList = mFavoriteHelper.getDb();
        dataListFiltered = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_favorite_item, parent, false);
        return new FavoriteViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final FavoriteModel favoriteModel = dataListFiltered.get(position);
        if (favoriteModel.IsScreen()) {
            ((FavoriteViewHolder) holder).title.setText(UtilManager.localizationHelper().getLocalizedTitle(favoriteModel.getScreenTitle()));
            if (favoriteModel.getScreenImage() != null)
                Glide.with(activity).load(favoriteModel.getScreenImage()).into(((FavoriteViewHolder) holder).image);
            ((FavoriteViewHolder) holder).mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityHandler.startActivity(activity, favoriteModel.getScreenId(), favoriteModel.getScreenType(), favoriteModel.getmSubScreenType(), null, null);
                }
            });
        } else if (favoriteModel.getContentType() == FavoriteModel.ContentTypes.TYPE_RSS) {
            ((FavoriteViewHolder) holder).title.setText(favoriteModel.getRssModel().getTitle());
            if (favoriteModel.getRssModel().getImage() != null)
                Glide.with(activity).load(favoriteModel.getRssModel().getImage()).into(((FavoriteViewHolder) holder).image);
            ((FavoriteViewHolder) holder).mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, aveRssContentViewPager.class);
                    intent.putExtra(KEY_RSS_TITLE,favoriteModel.getRssModel().getTitle());
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(favoriteModel.getRssModel());
                    activity.startActivity(intent);
                    aveRssContentViewPager.setRssModelList(arrayList);
                    aveRssContentViewPager.notifyAdapter();
                }
            });
        } else if (favoriteModel.getContentType() == FavoriteModel.ContentTypes.TYPE_GALLERY) {
            ((FavoriteViewHolder) holder).title.setText(UtilManager.localizationHelper().getLocalizedTitle(favoriteModel.getGalleryModel().getCaption()));
            if(((FavoriteViewHolder) holder).title.getText().toString().isEmpty())
                ((FavoriteViewHolder) holder).title.setVisibility(View.GONE);
            if (favoriteModel.getGalleryModel().getURL() != null)
                Glide.with(activity).load(favoriteModel.getGalleryModel().getURL()).into(((FavoriteViewHolder) holder).image);
            ((FavoriteViewHolder) holder).mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ImagePagerActivity.class);
                    intent.putExtra(TOOLBAR_TITLE, "title");
                    ArrayList<GalleryModel> galleryModels = new ArrayList<>();
                    galleryModels.add(favoriteModel.getGalleryModel());
                    intent.putExtra("imageList", galleryModels);
                    intent.putExtra("position", 0);
                    intent.putExtra("isDownloadable", false);
                    intent.putExtra("fromGalleryView", true);
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(activity, ((FavoriteViewHolder) holder).image, "galleryImage");
                        activity.startActivity(intent,options.toBundle());
                    }else
                        activity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dataListFiltered = dataList;
                } else {
                    ArrayList<FavoriteModel> filteredList = new ArrayList<>();
                    for (FavoriteModel row : dataList) {
                        if(row.IsScreen() && UtilManager.localizationHelper().getLocalizedTitle(row.getScreenModel().getTitle()).toLowerCase().contains(charSequence))
                        {
                            filteredList.add(row);
                        }else if(row.getContentType() == FavoriteModel.ContentTypes.TYPE_RSS && row.getRssModel().getTitle().toLowerCase().contains(charSequence))
                        {
                            filteredList.add(row);
                        }else if(row.getContentType() == FavoriteModel.ContentTypes.TYPE_GALLERY && UtilManager.localizationHelper().getLocalizedTitle(row.getGalleryModel().getCaption()).toLowerCase().contains(charSequence))
                        {
                            filteredList.add(row);
                        }
                    }

                    dataListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataListFiltered = (ArrayList<FavoriteModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    private class FavoriteViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView image;
        CardView mainLayout;

        FavoriteViewHolder(View v) {
            super(v);
            mainLayout = v.findViewById(R.id.favorite_item_main_layout); // main layout
            title = v.findViewById(R.id.favorite_item_title); // title
            image = v.findViewById(R.id.favorite_item_image); // image
        }
    }




}
