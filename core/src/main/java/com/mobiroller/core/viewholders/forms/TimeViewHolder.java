package com.mobiroller.core.viewholders.forms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.text.InputType;
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
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ealtaca on 6/14/17.
 */

public class TimeViewHolder extends FormBaseViewHolder {

    private TableItemsModel tableItemsModel;

    private UserProfileElement userProfileItem;
    @BindView(R2.id.form_item_time)
    MaterialEditText time;

    private Activity activity;
    private int lineCount;

    public TimeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(TableItemsModel tableItemsModel, LocalizationHelper localizationHelper, final Activity activity, @ColorInt int color) {
        this.tableItemsModel = tableItemsModel;
        this.activity = activity;
        Calendar now = Calendar.getInstance();
        time.setBaseColor(color);
        time.setPrimaryColor(color);
        time.setUnderlineColor(color);
        time.setFloatingLabelTextColor(color);
        time.setMetTextColor(color);
        Paint paint = new Paint();
        paint.setTextSize(TextHelper.convertSpToPixels(14.7f, activity));
        paint.setTypeface(time.getTypeface());
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        float textViewWidthPx = width - TextHelper.convertDpToPixels(64, activity);

        List<String> strings = TextHelper.splitWordsIntoStringsThatFit(localizationHelper.getLocalizedTitle(tableItemsModel.getTitle()), textViewWidthPx, paint);
        time.setHint(TextUtils.join("\n", strings));
        time.setFloatingLabelText(TextUtils.join("\n", strings));

        lineCount = strings.size();
        time.setLines(lineCount);
//        time.setSingleLineEllipsis(true);
        EditTextHelper.setCursorColor(time, color);
        time.setMetHintTextColor(ColorUtil.getLighterColor(color, 0.3f));
        time.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_av_timer_black_24dp, 0);
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog timePickerDialog, int i, int i1, int i2) {
                time.setText(i + ":" + i1);
                time.setLines(1);
            }
        };
        @SuppressLint("WrongConstant") final TimePickerDialog tpd = TimePickerDialog.newInstance(
                onTimeSetListener,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.setAccentColor(color);
//        time.setInputType(InputType.TYPE_NULL);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tpd.show(activity.getFragmentManager(), "timePicker");
            }
        });
        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    tpd.show(activity.getFragmentManager(), "timePicker");
            }
        });
    }

    @Override
    public void bind(UserProfileElement userProfileItem, LocalizationHelper localizationHelper, final Activity activity, int color) {
        this.userProfileItem = userProfileItem;
        Calendar now = Calendar.getInstance();

        this.activity = activity;
        time.setBaseColor(color);
        time.setPrimaryColor(color);
        time.setUnderlineColor(color);
        time.setFloatingLabelTextColor(color);
        time.setMetTextColor(color);
        EditTextHelper.setCursorColor(time, color);
        time.setMetHintTextColor(ColorUtil.getLighterColor(color, 0.3f));
        time.setHint(localizationHelper.getLocalizedTitle(userProfileItem.title));
        time.setFloatingLabelText(localizationHelper.getLocalizedTitle(userProfileItem.title));
        Paint paint = new Paint();
        paint.setTextSize(TextHelper.convertSpToPixels(14.7f, activity));
        paint.setTypeface(time.getTypeface());
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        float textViewWidthPx = width - TextHelper.convertDpToPixels(64, activity);

        List<String> strings = TextHelper.splitWordsIntoStringsThatFit(localizationHelper.getLocalizedTitle(userProfileItem.title), textViewWidthPx, paint);
        time.setHint(TextUtils.join("\n", strings));
        time.setFloatingLabelText(TextUtils.join("\n", strings));

        lineCount = strings.size();
        time.setLines(lineCount);
        time.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_av_timer_black_24dp, 0);
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog timePickerDialog, int i, int i1, int i2) {
                time.setText(i + ":" + i1);
                time.setLines(1);
            }
        };
        @SuppressLint("WrongConstant") final TimePickerDialog tpd = TimePickerDialog.newInstance(
                onTimeSetListener,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.setAccentColor(color);
        time.setInputType(InputType.TYPE_NULL);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tpd.show(activity.getFragmentManager(), "timePicker");
            }
        });
        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    tpd.show(activity.getFragmentManager(), "timePicker");
            }
        });
    }

    @Override
    public String getValue() {
        return time.getText().toString();
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
                if (time.getText().toString().equalsIgnoreCase("")) {
                    time.setErrorColor(Color.RED);
                    time.setError(activity.getString(R.string.select_a_time));
                    return false;
                } else
                    return true;
            } else
                return true;
        } else {
            if (userProfileItem.mandotory) {
                if (time.getText().toString().equalsIgnoreCase("")) {
                    time.setErrorColor(Color.RED);
                    time.setError(activity.getString(R.string.select_a_time));
                    return false;
                } else
                    return true;
            } else
                return true;
        }
    }

    @Override
    public void clear() {
        time.setText("");
        time.setLines(lineCount);
    }


    @Override
    public void setValue(String value) {
        time.setText(value);
        time.setLines(1);
    }

}
