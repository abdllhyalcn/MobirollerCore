package com.mobiroller.core.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.fragments.aveMainListViewFragment;
import com.mobiroller.core.helpers.ScreenHelper;
import com.mobiroller.core.helpers.SharedPrefHelper;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.models.NavigationModel;
import com.mobiroller.core.coreui.models.ScreenModel;
import com.mobiroller.core.R;
import com.mobiroller.core.util.ImageManager;
import com.mobiroller.core.coreui.util.ScreenUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class ContentRecyclerAdapter extends RecyclerView.Adapter<ContentRecyclerAdapter.ContentViewHolder> {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private ScreenHelper screenHelper;
    private Typeface face = null;
    private int textColor = 0, device_height = 0;
    private LayoutParams avatarParams = null;
    public ScreenModel screenModel;
    private int height;
    private boolean heightFlag = false;
    private NavigationModel navigationModel;
    private String contentListIItemBackgroundURL = null;

    public ContentRecyclerAdapter(Activity activity, ArrayList<HashMap<String, String>> data, ScreenModel screenModel,ScreenHelper screenHelper,SharedPrefHelper sharedPrefHelper) {
        this.activity = activity;
        this.data = data;
        this.screenHelper = screenHelper;
        device_height = ScreenUtil.getScreenHeight();
        this.screenModel = screenModel;
        getUiPropertiesFromScreenModel();

    }

    public ContentRecyclerAdapter(Activity activity, ArrayList<HashMap<String, String>> data, NavigationModel navigationModel,ScreenHelper screenHelper) {
        this.activity = activity;
        this.data = data;
        this.screenHelper = screenHelper;
        device_height = ScreenUtil.getScreenHeight();
        this.navigationModel = navigationModel;
        getUiPropertiesFromNavigationModel();

    }

    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_list_item, parent, false);
        ImageManager.loadBackgroundImage(contentListIItemBackgroundURL,itemView);
        return new ContentRecyclerAdapter.ContentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContentViewHolder holder, int position) {
        avatarParams = holder.contentAvatar.getLayoutParams();
        if (heightFlag) {
            avatarParams.height = Math.round((45 * (device_height)) / (Constants.MobiRoller_Preferences_StandardHeight)) - 5;
            avatarParams.width = Math.round((45 * (device_height)) / (Constants.MobiRoller_Preferences_StandardHeight)) - 5;
        } else {
            avatarParams.height = height - 5;
            avatarParams.width = height - 5;
        }
        holder.view.setMinimumHeight(height);
        loadItemUi(holder, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView contentTitle;
        ImageView contentAvatar;
        ImageView arrow;
        int position;
        View view;

        ContentViewHolder(View v) {
            super(v);
            view = v;
            contentTitle = v.findViewById(R.id.content_list_title); // title
            contentAvatar = v.findViewById(R.id.content_list_image); // thumb image
            arrow = v.findViewById(R.id.arrow_image); // arrow image
        }
    }

    private void loadItemUi(ContentViewHolder holder, int position) {
        holder.position = position;

        if (face != null)
            holder.contentTitle.setTypeface(face);

        if (textColor != 0)
            holder.contentTitle.setTextColor(textColor);


        HashMap<String, String> list_item = data.get(position);
        if (Integer.parseInt(list_item.get("screen_id")) == -1) {
            holder.arrow.setVisibility(View.GONE);
        } else {
            holder.arrow.setVisibility(View.VISIBLE);
        }
        final String img_url = list_item.get(aveMainListViewFragment.KEY_IMAGE_URL);
        if (img_url == null || img_url.equalsIgnoreCase("")) {
            holder.contentAvatar.setVisibility(View.GONE);
        } else {
            holder.contentAvatar.setLayoutParams(avatarParams);
            holder.contentAvatar.setVisibility(View.VISIBLE);
        }

        // Setting all values in listview
        holder.contentTitle.setText(list_item.get(aveMainListViewFragment.KEY_TITLE));
        holder.contentTitle.setAutoLinkMask(Linkify.WEB_URLS);

        if (img_url != null && !img_url.equalsIgnoreCase(""))
            ImageManager.loadImageView(activity,img_url,holder.contentAvatar);

    }

    private void getUiPropertiesFromScreenModel() {

        if (screenModel.getTableCellBackground() != null) {
            contentListIItemBackgroundURL = screenModel.getTableCellBackground().getImageURL();
        }
        if (screenModel.getTableTextColor() != null) {
            textColor = screenHelper.setColorUnselected(screenModel.getTableTextColor());
        }
        if(screenModel.getTableFontName() != null) {
            try {
                face = screenHelper.getFontFromAsset(screenModel.getTableFontName());
            }catch (Exception e) {
                // font error
            }
        }

        if (screenModel.getTableRowHeight() != 0) {
            if (UtilManager.sharedPrefHelper().getTabActive())
                height = Math.round((screenModel.getTableRowHeight() * (device_height - UtilManager.sharedPrefHelper().getTabHeight())) / (Constants.MobiRoller_Preferences_StandardHeight));
            else
                height = Math.round((screenModel.getTableRowHeight() * (device_height)) / (Constants.MobiRoller_Preferences_StandardHeight));
            heightFlag = true;
        }

    }


    private void getUiPropertiesFromNavigationModel() {

        if (navigationModel.getTableCellBackground() != null) {
            contentListIItemBackgroundURL = navigationModel.getTableCellBackground().getImageURL();
        }
        if (navigationModel.getMenuTextColor() != null) {
            textColor = screenHelper.setColorUnselected(navigationModel.getMenuTextColor());
        }

        height = Math.round((45 * (device_height)) / (Constants.MobiRoller_Preferences_StandardHeight));
        heightFlag = false;

    }
}