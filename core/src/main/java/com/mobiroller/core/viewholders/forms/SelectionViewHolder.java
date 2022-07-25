package com.mobiroller.core.viewholders.forms;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.ColorInt;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mobiroller.core.R2;
import com.mobiroller.core.helpers.EditTextHelper;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.helpers.TextHelper;
import com.mobiroller.core.coreui.models.TableItemsModel;
import com.mobiroller.core.models.response.UserProfileElement;
import com.mobiroller.core.R;
import com.mobiroller.core.coreui.util.ColorUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ealtaca on 6/14/17.
 */

public class SelectionViewHolder extends FormBaseViewHolder {

    private TableItemsModel tableItemsModel;

    private UserProfileElement userProfileItem;
    @BindView(R2.id.form_item_selection)
    MaterialEditText selection;
    private int lineCount;
    private boolean isSelected = false;
    private String selectedValue = "";
    private Activity activity;

    public SelectionViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(final TableItemsModel tableItemsModel, final LocalizationHelper localizationHelper, final Activity activity, @ColorInt final int color) {
        this.tableItemsModel = tableItemsModel;
        this.activity = activity;

        selection.setBaseColor(color);
        selection.setPrimaryColor(color);
        selection.setUnderlineColor(color);
        selection.setFloatingLabelTextColor(color);
        selection.setMetTextColor(color);

        selection.setMetHintTextColor(ColorUtil.getLighterColor(color,0.3f));
//        selection.setMetHintTextColor(color);
        EditTextHelper.setCursorColor(selection, color);
        selection.setContentDescription(localizationHelper.getLocalizedTitle(tableItemsModel.getTitle()));
//        selection.setHint(localizationHelper.getLocalizedTitle(tableItemsModel.getTitle()));
//        selection.setFloatingLabelText(localizationHelper.getLocalizedTitle(tableItemsModel.getTitle()));

        final Paint paint = new Paint();
        paint.setTextSize(TextHelper.convertSpToPixels(14.7f, activity));
        paint.setTypeface(selection.getTypeface());
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        final float textViewWidthPx = width - TextHelper.convertDpToPixels(70, activity);

        List<String> strings = TextHelper.splitWordsIntoStringsThatFit(localizationHelper.getLocalizedTitle(tableItemsModel.getTitle()), textViewWidthPx, paint);
        selection.setHint(TextUtils.join("\n", strings));
        selection.setFloatingLabelText(TextUtils.join("\n", strings));

        lineCount = strings.size();
        selection.setLines(lineCount);
        selection.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        selection.setCursorVisible(false);
        selection.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        selection.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        String itemsString = localizationHelper.getLocalizedTitle(tableItemsModel.getItems());
        final String itemArray[] = itemsString.split(",");

        selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(activity)
                        .title(selection.getHint())
                        .items(itemArray)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                isSelected = true;
//                                selection.setText(itemArray[which]);
                                List<String> stringse = TextHelper.splitWordsIntoStringsThatFit(itemArray[which], textViewWidthPx, paint);
                                selection.setLines(stringse.size());
                                selection.setText(TextUtils.join("\t", stringse));
                            }
                        })
                        .show();
            }
        });
        selection.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new MaterialDialog.Builder(activity)
                            .title(selection.getHint())
                            .items(itemArray)
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    isSelected = true;
//                                    selection.setText(itemArray[which]);
                                    List<String> stringse = TextHelper.splitWordsIntoStringsThatFit(itemArray[which], textViewWidthPx, paint);
                                    selection.setLines(stringse.size());
                                    selection.setText(TextUtils.join("\t", stringse));
                                }
                            })
                            .show();
                }
            }
        });
    }

    @Override
    public void bind(UserProfileElement userProfileItem, LocalizationHelper localizationHelper, final Activity activity, final int color) {
        this.userProfileItem = userProfileItem;

        this.activity = activity;
        selection.setBaseColor(color);
        selection.setPrimaryColor(color);
        selection.setUnderlineColor(color);
        selection.setFloatingLabelTextColor(color);
        selection.setMetTextColor(color);
        selection.setMetHintTextColor(ColorUtil.getLighterColor(color,0.3f));
        EditTextHelper.setCursorColor(selection, color);
        selection.setContentDescription(localizationHelper.getLocalizedTitle(userProfileItem.title));
//        selection.setHint(localizationHelper.getLocalizedTitle(userProfileItem.getTitle()));
//        selection.setFloatingLabelText(localizationHelper.getLocalizedTitle(userProfileItem.getTitle()));
        final Paint paint = new Paint();
        paint.setTextSize(TextHelper.convertSpToPixels(14.7f, activity));
        paint.setTypeface(selection.getTypeface());
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        final float textViewWidthPx = width - TextHelper.convertDpToPixels(64, activity);

        List<String> strings = TextHelper.splitWordsIntoStringsThatFit(localizationHelper.getLocalizedTitle(userProfileItem.title), textViewWidthPx, paint);
        selection.setHint(TextUtils.join("\n", strings));
        selection.setFloatingLabelText(TextUtils.join("\n", strings));
        selection.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        selection.setCursorVisible(false);
        selection.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        selection.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        lineCount = strings.size();
        selection.setLines(lineCount);
        String itemsString = localizationHelper.getLocalizedTitle(userProfileItem.selections);
        final String itemArray[] = itemsString.split(",");

        selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(activity)
                        .title(selection.getHint())
                        .items(itemArray)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                isSelected = true;
                                selection.setText(itemArray[which]);
                                List<String> strings = TextHelper.splitWordsIntoStringsThatFit(itemArray[which], textViewWidthPx, paint);
                                selection.setLines(strings.size());
                                selection.setText(TextUtils.join("\n", strings));
                            }
                        })
                        .show();
            }
        });
        selection.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new MaterialDialog.Builder(activity)
                            .title(selection.getHint())
                            .items(itemArray)
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    isSelected = true;
                                    selection.setText(itemArray[which]);
                                    List<String> strings = TextHelper.splitWordsIntoStringsThatFit(itemArray[which], textViewWidthPx, paint);
                                    selection.setLines(strings.size());
                                    selection.setText(TextUtils.join("\n", strings));
                                }
                            })
                            .show();
                }
            }
        });
    }

    public String getValue() {
        return selection.getText().toString();
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
            if (tableItemsModel.getMandatory().equalsIgnoreCase("Yes")) {
                if (isSelected)
                    return true;
                else {
                    selection.setErrorColor(Color.RED);
                    selection.setError(activity.getString(R.string.make_a_choice));
                    return false;
                }
            }
        } else if (userProfileItem.mandotory) {
            if (isSelected)
                return true;
            else {
                selection.setErrorColor(Color.RED);
                selection.setError(activity.getString(R.string.make_a_choice));
                return false;
            }
        }
        return true;
    }

    @Override
    public void clear() {
        selection.setText("");
        selection.setLines(lineCount);
        isSelected = false;
    }

    @Override
    public void setValue(String value) {
        selection.setText(value);
        isSelected = true;
    }
}
