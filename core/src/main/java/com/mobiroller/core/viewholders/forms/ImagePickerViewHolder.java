package com.mobiroller.core.viewholders.forms;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobiroller.core.R2;
import com.mobiroller.core.fragments.aveFormViewFragment;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.coreui.models.TableItemsModel;
import com.mobiroller.core.models.events.FormImageEvent;
import com.mobiroller.core.models.response.UserProfileElement;
import com.mobiroller.core.R;
import com.mobiroller.core.util.ImageManager;
import com.mobiroller.core.views.SquareImageView;
import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qosmio on 03.04.2018.
 */

public class ImagePickerViewHolder extends FormBaseViewHolder {
    private TableItemsModel tableItemsModel;

    private UserProfileElement userProfileItem;

    @BindView(R2.id.form_item_title)
    TextView title;

    @BindView(R2.id.form_image_main_layout)
    RelativeLayout imageMainLayout;

    @BindView(R2.id.form_image)
    SquareImageView image;

    @BindView(R2.id.form_image_remove)
    ImageView removeImageView;

    private LocalizationHelper localizationHelper;

    private Activity activity;

    private int actionBarColor;
    private aveFormViewFragment fragment;
    private int order;
    private ImageView imageView;

    public ImagePickerViewHolder(View itemView, int actionBarColor, aveFormViewFragment fragment) {
        super(itemView);
        this.fragment = fragment;
        this.actionBarColor = actionBarColor;
        ButterKnife.bind(this, itemView);
        EventBus.getDefault().register(this);
    }

    @Override
    public void bind(TableItemsModel tableItemsModel, LocalizationHelper localizationHelper, final Activity activity, int color) {
        this.tableItemsModel = tableItemsModel;
        this.localizationHelper = localizationHelper;
        this.activity = activity;
        this.tableItemsModel = tableItemsModel;
        title.setText(localizationHelper.getLocalizedTitle(tableItemsModel.getTitle()));
        imageMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        removeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });
        RelativeLayout.LayoutParams imParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView = new ImageView(activity);
        imageView.setLayoutParams(imParams);
        imageView.setPadding(8, 8, 8, 8);
        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        imageView.setLayoutParams(layoutParams);
        imageView.setBackgroundColor(Color.parseColor("#A6AFB8"));
        imageView.setImageResource(R.drawable.icon_add);
        imageMainLayout.addView(imageView);
    }

    @Override
    public void bind(UserProfileElement userProfileItem, LocalizationHelper localizationHelper, Activity activity, int color) {

        this.userProfileItem = userProfileItem;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String getId() {
        if (tableItemsModel != null)
            return tableItemsModel.getId();
        return userProfileItem.id;
    }

    @Override
    public boolean isImage() {
        return true;
    }

    @Override
    public byte[] getImage() {
        if (image.getDrawable() != null) {
            Drawable d = image.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
            if (bitmap.getHeight() > 1024 || bitmap.getWidth() > 1024)
                bitmap = ImageManager.resize(bitmap, 1024, 1024);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            return stream.toByteArray();
        } else
            return null;
    }

    @Override
    public boolean isValid() {
        if (tableItemsModel.getMandatory().equalsIgnoreCase("yes"))
            if (image.getDrawable() == null) {
                title.setText(Html.fromHtml(localizationHelper.getLocalizedTitle(tableItemsModel.getTitle()) + " - " + "<font color='red'>" + activity.getResources().getString(R.string.required_field) + "</font>"));
                return false;
            }
        title.setText(localizationHelper.getLocalizedTitle(tableItemsModel.getTitle()));
        return true;
    }

    @Override
    public void clear() {
        image.setImageDrawable(null);
        imageView.setVisibility(View.VISIBLE);
        removeImageView.setVisibility(View.GONE);
    }

    @Override
    public void setValue(String value) {

    }

    private void openGallery() {
        Intent intent = CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .getIntent(activity);
        fragment.getActivity().startActivityForResult(intent, order);
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Subscribe
    public void onFormImageEvent(FormImageEvent event) {
        if (event.getId() == order) {
            if (event.getImagePath() != null) {
                File file = new File(event.getImagePath().getPath());
                int file_size = Integer.parseInt(String.valueOf(file.length() / 2592));
                if (file_size > 2592)
                    Toast.makeText(activity, activity.getResources().getString(R.string.image_size_validation), Toast.LENGTH_SHORT).show();
                else {
                    image.setImageURI(event.getImagePath());
                    imageView.setVisibility(View.GONE);
                    removeImageView.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
