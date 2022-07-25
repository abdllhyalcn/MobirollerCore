package com.mobiroller.core.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.models.Audio;
import com.mobiroller.core.coreui.models.ColorModel;
import com.mobiroller.core.coreui.models.ImageModel;
import com.mobiroller.core.R;

import java.util.ArrayList;

public class MP3ListAdapter extends RecyclerView.Adapter<MP3ListAdapter.Mp3ContentViewHolder> {

    private ArrayList<Audio> data;
    public Context context;
    private ImageModel image;
    private ColorModel colorModel;

    public MP3ListAdapter(ArrayList<Audio> data, Context context, ImageModel imageModel, ColorModel colorModel) {
        this.data = data;
        this.context = context;
        this.image = imageModel;
        this.colorModel = colorModel;
    }

    public int getCount() {
        return data.size();
    }


    public Audio getItemByPosition(int position) {
        return data.get(position);
    }

    @Override
    public Mp3ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mp3_list_item, parent, false);
        return new MP3ListAdapter.Mp3ContentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Mp3ContentViewHolder holder, int position) {
        Audio audio = data.get(position);

        if (audio.isSelected()) {
            holder.setSelected();
        } else {
            holder.setUnselected();
        }
        holder.text.setText(audio.getTitle());

    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class Mp3ContentViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        LottieAnimationView arrow;
        View view;
        View dividerView;

        Mp3ContentViewHolder(View v) {
            super(v);
            view = v;
            if(image!=null && image.getImageURL()!=null)
            text = v.findViewById(R.id.content_list_title);    // title
            if(colorModel!=null && colorModel.getColor()!=0)
                text.setTextColor(colorModel.getColor());
            arrow = v.findViewById(R.id.arrow_image);    // arrow image
            dividerView = v.findViewById(R.id.divider);
            dividerView.setBackgroundColor(UtilManager.sharedPrefHelper().getActionBarColor());
        }

        void setSelected() {
            arrow.setVisibility(View.VISIBLE);
            arrow.setMinAndMaxProgress(0, 0.03f);
            arrow.loop(true);
            if (!arrow.isAnimating())
                arrow.playAnimation();
        }

        void setUnselected() {
            arrow.setVisibility(View.GONE);
            arrow.cancelAnimation();
        }

        public void pauseAnimation() {
            arrow.pauseAnimation();
        }

        public void playAnimation() {
            arrow.playAnimation();
        }
    }


}