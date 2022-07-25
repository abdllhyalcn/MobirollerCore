package com.mobiroller.core.viewholders.forms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;

import androidx.annotation.ColorInt;

import com.mobiroller.core.R2;
import com.mobiroller.core.helpers.EditTextHelper;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.helpers.TextHelper;
import com.mobiroller.core.coreui.models.TableItemsModel;
import com.mobiroller.core.models.response.UserProfileElement;
import com.mobiroller.core.R;
import com.mobiroller.core.coreui.util.ColorUtil;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ealtaca on 6/14/17.
 */

public class DateViewHolder extends FormBaseViewHolder {

    private TableItemsModel tableItemsModel;

    private UserProfileElement userProfileItem;
    @BindView(R2.id.form_item_date)
    MaterialEditText date;
    Activity activity;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
    private int lineCount;

    public DateViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(TableItemsModel tableItemsModel, LocalizationHelper localizationHelper, final Activity activity, @ColorInt int color) {
        this.activity = activity;
        this.tableItemsModel = tableItemsModel;

        date.setBaseColor(color);
        date.setPrimaryColor(color);
        date.setUnderlineColor(color);
        date.setFloatingLabelTextColor(color);
        date.setMetTextColor(color);
        date.setMetHintTextColor(ColorUtil.getLighterColor(color, 0.3f));
        EditTextHelper.setCursorColor(date, color);
        date.setContentDescription(localizationHelper.getLocalizedTitle(tableItemsModel.getTitle()));
        Paint paint = new Paint();
        paint.setTextSize(TextHelper.convertSpToPixels(14.7f, activity));
        paint.setTypeface(date.getTypeface());
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        float textViewWidthPx = width - TextHelper.convertDpToPixels(64, activity);

        List<String> strings = TextHelper.splitWordsIntoStringsThatFit(localizationHelper.getLocalizedTitle(tableItemsModel.getTitle()), textViewWidthPx, paint);
        date.setHint(TextUtils.join("\n", strings));
        date.setFloatingLabelText(TextUtils.join("\n", strings));

        lineCount = strings.size();
        date.setLines(lineCount);
        Calendar now = Calendar.getInstance();

        date.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_date_range_black_24dp, 0);
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(i, i1, i2);
                date.setText(dateFormatter.format(newDate.getTime()));
                date.setLines(1);
            }
        };
        @SuppressLint("WrongConstant") final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(dateSetListener,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.setAccentColor(color);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(activity.getFragmentManager(), "datePicker");
            }
        });
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePickerDialog.show(activity.getFragmentManager(), "datePicker");
                }
            }
        });
    }

    @SuppressLint("WrongConstant")
    @Override
    public void bind(UserProfileElement userProfileItem, LocalizationHelper localizationHelper, final Activity activity, int color) {
        this.activity = activity;
        this.userProfileItem = userProfileItem;


        date.setBaseColor(color);
        date.setPrimaryColor(color);
        date.setUnderlineColor(color);
        date.setFloatingLabelTextColor(color);
        date.setMetTextColor(color);
        date.setMetHintTextColor(color);
        EditTextHelper.setCursorColor(date, color);
        date.setContentDescription(localizationHelper.getLocalizedTitle(userProfileItem.title));
        date.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_date_range_black_24dp, 0);
        Calendar now = Calendar.getInstance();
        Paint paint = new Paint();
        paint.setTextSize(TextHelper.convertSpToPixels(14.7f, activity));
        paint.setTypeface(date.getTypeface());
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        float textViewWidthPx = width - TextHelper.convertDpToPixels(64, activity);

        List<String> strings = TextHelper.splitWordsIntoStringsThatFit(localizationHelper.getLocalizedTitle(userProfileItem.title), textViewWidthPx, paint);
        date.setHint(TextUtils.join("\n", strings));
        date.setFloatingLabelText(TextUtils.join("\n", strings));

        lineCount = strings.size();
        date.setLines(lineCount);
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(i, i1, i2);
                date.setText(dateFormatter.format(newDate.getTime()));
                date.setLines(1);
            }
        };
        @SuppressLint("WrongConstant") final DatePickerDialog datePickerDialog;
        if (userProfileItem.type.equalsIgnoreCase("birthday")) {
            datePickerDialog = DatePickerDialog.newInstance(dateSetListener,
                    now.get(Calendar.YEAR) - 18,
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            now.add(Calendar.MONTH, -1);
            datePickerDialog.setMaxDate(now);
        } else
            datePickerDialog = DatePickerDialog.newInstance(dateSetListener,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
        datePickerDialog.setAccentColor(color);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(activity.getFragmentManager(), "datePicker");
            }
        });
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePickerDialog.show(activity.getFragmentManager(), "datePicker");
                }
            }
        });

    }

    @Override
    public String getValue() {
        return date.getText().toString();
    }

    @Override
    public String getId() {
        if (tableItemsModel != null)
            return tableItemsModel.getId();
        return userProfileItem.id;
    }

    @Override
    public byte[] getImage() {
        return null;
    }
    @Override
    public boolean isImage() {
        return false;
    }

    @Override
    public boolean isValid() {
        if (tableItemsModel != null) {
            if (tableItemsModel.getMandatory().equalsIgnoreCase("YES")) {
                if (date.getText().toString().equalsIgnoreCase("")) {
                    date.setErrorColor(Color.RED);
                    date.setError(activity.getString(R.string.select_a_date));
                    return false;
                } else
                    return true;
            } else
                return true;
        } else {
            if (userProfileItem.mandotory) {
                if (date.getText().toString().equalsIgnoreCase("")) {
                    date.setErrorColor(Color.RED);
                    date.setError(activity.getString(R.string.select_a_date));
                    return false;
                } else
                    return true;
            } else
                return true;
        }
    }

    @Override
    public void clear() {
        date.setText("");
        date.setLines(lineCount);
    }


    @Override
    public void setValue(String value) {
        date.setText(value);
        date.setLines(1);
    }


}
