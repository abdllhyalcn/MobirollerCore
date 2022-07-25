package com.mobiroller.core.adapters.bottomsheet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiroller.core.models.bottomsheet.ActionModel;
import com.mobiroller.core.R;
import com.mobiroller.core.viewholders.bottomsheet.ActionViewHolder;

import java.util.List;

public class BottomSheetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private List<ActionModel> dataList;

    public BottomSheetAdapter(List<ActionModel> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_bottom_action_item, parent, false);
        return new ActionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ActionViewHolder)viewHolder).bind(dataList.get(i));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}