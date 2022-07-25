package com.mobiroller.core.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.mobiroller.core.helpers.JSONStorage;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.helpers.SharedPrefHelper;
import com.mobiroller.core.coreui.models.TableItemsModel;
import com.mobiroller.core.R;
import com.mobiroller.core.viewholders.forms.PhoneViewHolder;

import java.util.ArrayList;

/**
 * Created by ealtaca on 17.05.2017.
 */

public class EmergencyCallAdapter extends RecyclerView.Adapter<PhoneViewHolder> {

    private Activity activity;
    private ArrayList<TableItemsModel> dataList;
    private SharedPrefHelper sharedPrefHelper;
    private LocalizationHelper localizationHelper;
    private int textColor = -1;

    public EmergencyCallAdapter(Activity activity, ArrayList<TableItemsModel> dataList,  SharedPrefHelper sharedPrefHelper, LocalizationHelper localizationHelper) {
        this.activity = activity;
        this.dataList = dataList;
        this.sharedPrefHelper = sharedPrefHelper;
        this.localizationHelper = localizationHelper;
        if(JSONStorage.getNavigationModel()!= null && JSONStorage.getNavigationModel().getMenuTextColor()!=null)
            textColor = JSONStorage.getNavigationModel().getMenuTextColor().getColor();
    }

    @Override
    public PhoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View phoneView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_form_phone, parent, false);
        return new PhoneViewHolder(phoneView,sharedPrefHelper.getActionBarColor());
    }

    @Override
    public void onBindViewHolder(PhoneViewHolder holder, int position) {
        holder.bindEmergency(dataList.get(position),localizationHelper,activity,textColor);
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public ArrayList<TableItemsModel> getItems()
    {
        return dataList;
    }

}
