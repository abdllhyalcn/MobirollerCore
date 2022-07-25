package com.mobiroller.core.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.mobiroller.core.fragments.aveFormViewFragment;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.coreui.models.TableItemsModel;
import com.mobiroller.core.R;
import com.mobiroller.core.viewholders.forms.AddressViewHolder;
import com.mobiroller.core.viewholders.forms.CheckViewHolder;
import com.mobiroller.core.viewholders.forms.DateViewHolder;
import com.mobiroller.core.viewholders.forms.EmailViewHolder;
import com.mobiroller.core.viewholders.forms.FormBaseViewHolder;
import com.mobiroller.core.viewholders.forms.ImagePickerViewHolder;
import com.mobiroller.core.viewholders.forms.LabelViewHolder;
import com.mobiroller.core.viewholders.forms.PhoneViewHolder;
import com.mobiroller.core.viewholders.forms.SelectionViewHolder;
import com.mobiroller.core.viewholders.forms.StartRatingViewHolder;
import com.mobiroller.core.viewholders.forms.SubmitViewHolder;
import com.mobiroller.core.viewholders.forms.TextAreaViewHolder;
import com.mobiroller.core.viewholders.forms.TimeViewHolder;

import java.util.ArrayList;

/**
 * Created by ealtaca on 6/14/17.
 */

public class FormAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<Object> tableItemsModels;
    private int color;
    private int actionBarColor;
    private aveFormViewFragment fragment;

    private static final int TYPE_TEXT_AREA = 0;
    private static final int TYPE_CHECK = 1;
    private static final int TYPE_SELECTION = 2;
    private static final int TYPE_STAR_RATING = 3;
    private static final int TYPE_DATE = 4;
    private static final int TYPE_TIME = 5;
    private static final int TYPE_LABEL = 6;
    private static final int TYPE_PHONE = 7;
    private static final int TYPE_EMAIL = 8;
    private static final int TYPE_ADDRESS = 9;
    private static final int TYPE_SUBMIT = 10;
    private static final int TYPE_IMAGE = 11;

    public FormAdapter(Activity activity, ArrayList<Object> tableItemsModels, int color, int actionBarColor, aveFormViewFragment fragment) {
        this.activity = activity;
        this.tableItemsModels = tableItemsModels;
        this.color = color;
        this.actionBarColor = actionBarColor;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TEXT_AREA:
                final View textAreaView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_form_text_area, parent, false);
                return new TextAreaViewHolder(textAreaView);
            case TYPE_CHECK:
                final View checkView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_form_check, parent, false);
                return new CheckViewHolder(checkView);
            case TYPE_SELECTION:
                final View selectionView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_form_selection, parent, false);
                return new SelectionViewHolder(selectionView);
            case TYPE_STAR_RATING:
                final View starRatingView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_form_star_rating, parent, false);
                return new StartRatingViewHolder(starRatingView);
            case TYPE_DATE:
                final View dateView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_form_date, parent, false);
                return new DateViewHolder(dateView);
            case TYPE_TIME:
                final View timeView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_form_time, parent, false);
                return new TimeViewHolder(timeView);
            case TYPE_LABEL:
                final View labelView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_form_label, parent, false);
                return new LabelViewHolder(labelView,actionBarColor);
            case TYPE_PHONE:
                final View phoneView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_form_phone, parent, false);
                return new PhoneViewHolder(phoneView,actionBarColor);
            case TYPE_EMAIL:
                final View emailView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_form_email, parent, false);
                return new EmailViewHolder(emailView,actionBarColor);
            case TYPE_ADDRESS:
                final View addressView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_form_address, parent, false);
                return new AddressViewHolder(addressView,actionBarColor);
            case TYPE_SUBMIT:
                final View submitView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_form_submit, parent, false);
                return new SubmitViewHolder(submitView,color);
            case TYPE_IMAGE:
                final View imageView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_form_image_picker, parent, false);
                return new ImagePickerViewHolder(imageView,color,fragment);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (tableItemsModels.get(position) instanceof TableItemsModel) {
            ((FormBaseViewHolder) holder)
                    .bind((TableItemsModel) tableItemsModels.get(position),
                            UtilManager.localizationHelper(),
                            activity,color);
            if(holder instanceof ImagePickerViewHolder)
                ((ImagePickerViewHolder)holder).setOrder(position);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(tableItemsModels.get(position) instanceof TableItemsModel) {
            switch (((TableItemsModel) tableItemsModels.get(position)).getType()) {
                case "textArea":
                    return TYPE_TEXT_AREA;
                case "check":
                    return TYPE_CHECK;
                case "selection":
                    return TYPE_SELECTION;
                case "starRating":
                    return TYPE_STAR_RATING;
                case "date":
                    return TYPE_DATE;
                case "time":
                    return TYPE_TIME;
                case "phone":
                    return TYPE_PHONE;
                case "email":
                    return TYPE_EMAIL;
                case "label":
                    return TYPE_LABEL;
                case "address":
                    return TYPE_ADDRESS;
                case "formImage":
                    return TYPE_IMAGE;
            }
        }else
            return TYPE_SUBMIT;

        return -1;
    }

    @Override
    public int getItemCount() {
        return tableItemsModels.size();
    }


    public ArrayList<Object> getItems(){
        return tableItemsModels;
    }

}
