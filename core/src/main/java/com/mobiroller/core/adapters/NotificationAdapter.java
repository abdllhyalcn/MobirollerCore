package com.mobiroller.core.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiroller.core.models.NotificationModel;
import com.mobiroller.core.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ealtaca on 06.01.2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private SparseBooleanArray selectedItems;
    private List<NotificationModel> notificationModels;
    private Context context;


    public NotificationAdapter(List<NotificationModel> notificationModels, Context context) {
        this.notificationModels = notificationModels;
        this.context = context;
        selectedItems = new SparseBooleanArray();
    }


    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_list_item, parent, false);

        return new NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NotificationViewHolder holder, final int position) {
        final NotificationModel notificationModel = notificationModels.get(position);
        holder.message.setText(notificationModel.getMessage());
        holder.date.setText(getFormattedDate(notificationModel.getDate()));
        if (notificationModel.isRead()) {
            holder.date.setCompoundDrawablesWithIntrinsicBounds(R.drawable.forma_1, 0, 0, 0);
            holder.message.setTextColor(Color.parseColor("#838486"));
            holder.message.setTypeface(null, Typeface.NORMAL);
        } else {
            holder.date.setCompoundDrawablesWithIntrinsicBounds(R.drawable.forma_1_yellow, 0, 0, 0);
            holder.message.setTextColor(Color.BLACK);
            holder.message.setTypeface(null, Typeface.BOLD);
        }
        if (selectedItems.get(position, false)) {
            holder.foreground.setBackgroundColor(Color.parseColor("#FFEEB8"));
            holder.notificationCirle.setBackground(ContextCompat.getDrawable(context
                    , R.drawable.notification_cirle_selected));
            holder.isRead.setVisibility(View.VISIBLE);
        } else {
            holder.foreground.setBackgroundColor(Color.WHITE);
            holder.notificationCirle.setBackground(ContextCompat.getDrawable(context,
                    R.drawable.notification_cirle_unselected));
            holder.isRead.setVisibility(View.GONE);
        }
        holder.notificationCirle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView message, date;
        ImageView isRead;
        ImageButton deleteButton;
        RelativeLayout foreground;
        RelativeLayout notificationCirle;

        NotificationViewHolder(View view) {
            super(view);
            deleteButton = view.findViewById(R.id.backgroundView);
            foreground = view.findViewById(R.id.foregroundView);
            notificationCirle = view.findViewById(R.id.notification_cirle);
            message = view.findViewById(R.id.notification_message);
            date = view.findViewById(R.id.notification_date);
            isRead = view.findViewById(R.id.notification_is_read);
        }

    }

    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);
        }
        notifyDataSetChanged();
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public NotificationModel getItem(int position) {
        return notificationModels.get(position);
    }

    public void setRead(int position)
    {
        notificationModels.get(position).setRead();
        notifyItemChanged(position);
    }

    public void removeData(int position) {
        notificationModels.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public List<NotificationModel> getAllData() {
        return notificationModels;
    }


    @SuppressLint("WrongConstant")
    private String getFormattedDate(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "HH:mm";
        final String dateTimeFormatString = "EEEE, MMMM d, h:mm";
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) ) {
            return context.getString(R.string.date_today)+"  " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1  ){
            return context.getString(R.string.date_yesterday)+ "  " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("MMMM dd yyyy, h:mm", smsTime).toString();
        }
    }

}
