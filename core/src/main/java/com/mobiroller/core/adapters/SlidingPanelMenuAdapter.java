package com.mobiroller.core.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.mobiroller.core.models.NavigationItemModel;
import com.mobiroller.core.R;
import com.mobiroller.core.util.ImageManager;
import com.mobiroller.core.views.CircleImageView;

import java.util.List;

public class SlidingPanelMenuAdapter extends ArrayAdapter {
    private Context mContext;
    private List<NavigationItemModel> mList;
    private int selectedPosition = -1;

    private LayoutInflater inflater;
    private int mSelectedColor;

    public SlidingPanelMenuAdapter(Context c, List<NavigationItemModel> list, int selectedColor) {
        super(c, R.layout.layout_sliding_panel_menu_item);
        mContext = c;
        mList = list;
        mSelectedColor = selectedColor;
        inflater = LayoutInflater.from(c);
    }

    public int getCount() {
        return mList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            convertView = inflater.inflate(R.layout.layout_sliding_panel_menu_item, parent, false);
        }
        CircleImageView border = convertView.findViewById(R.id.image);
        ImageView icon = convertView.findViewById(R.id.icon);
        if(mList.get(position).getIconImage()!=null)
            ImageManager.loadImageView(mContext,mList.get(position).getIconImage().getImageURL(),icon);
        if (position == selectedPosition) {
            border.setBorderColor(Color.WHITE);
            border.setFillColor(mSelectedColor);
            border.setBorderWidth(3);
        } else {
            border.setBorderColor(Color.WHITE);
            border.setFillColor(Color.TRANSPARENT);
            border.setBorderWidth(0);
        }
        return convertView;
    }

    public void setSelected(int position) {
        selectedPosition = position;
    }
}