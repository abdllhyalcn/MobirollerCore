package com.mobiroller.core.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobiroller.core.R;
import com.mobiroller.core.util.ImageManager;
import com.mobiroller.core.views.NavDrawerItem;

import java.util.ArrayList;

/**
 * Created by ealtaca on 25.01.2017.
 */

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ContentViewHolder> {


    private ArrayList<NavDrawerItem> navDrawerItems;
    private Context context;
    private int selectedPos = 0;
    private String selectedPosColor = "#92CCCCCC"; // Light gray with alpha

    public MainListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_list_item, parent, false);

        return new MainListAdapter.ContentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContentViewHolder holder, int position) {
        NavDrawerItem item = navDrawerItems.get(position);
        if (item.getImageUrl() != null && !item.getImageUrl().equalsIgnoreCase("null")) {
            ImageManager.loadImageView(context,item.getImageUrl(),holder.imgIcon);
        }
        holder.txtTitle.setTextColor(item.textColor);
        holder.txtTitle.setText(item.getTitle());
        if (selectedPos == position)
            holder.view.setBackgroundColor(Color.parseColor(selectedPosColor));
        else
            holder.view.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return navDrawerItems.size();
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView txtTitle;
        View view;

        ContentViewHolder(View v) {
            super(v);
            view = v;
            imgIcon = v.findViewById(R.id.slide_icon);
            txtTitle = v.findViewById(R.id.slide_title);
        }
    }


    public void setSelectedPos(int position) {
        int temp  = selectedPos;
        selectedPos = position;
        notifyItemChanged(selectedPos);
        notifyItemChanged(temp);
    }
}
