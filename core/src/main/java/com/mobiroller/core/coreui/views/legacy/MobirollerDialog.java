package com.mobiroller.core.coreui.views.legacy;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Spannable;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.mobiroller.core.R;
import com.mobiroller.core.coreui.enums.MobirollerDialogType;
import com.mobiroller.core.coreui.helpers.ItemClickSupport;

public class MobirollerDialog {

    public final static class Builder {

        private Context mContext;
        private int mIconResource;
        private int mColor;
        private String mTitle;
        private String mDescription;
        private Spannable mSpannable;
        private String mButtonText;
        private String mNegativeButtonText;
        private MobirollerDialogType mType;
        private DialogButtonCallback mCallback;
        private DialogListCallback mListCallback;
        private RecyclerView.Adapter mAdapter;
        private boolean mHasDivider;
        private boolean mAutoDismiss = true;

        /**
         * Set the Context used to instantiate the SharedPreferences
         *
         * @param context the application context
         */
        public MobirollerDialog.Builder setContext(final Context context) {
            mContext = context;
            return this;
        }

        public MobirollerDialog.Builder setType(final MobirollerDialogType type) {
            mType = type;
            return this;
        }

        public MobirollerDialog.Builder setIconResource(final int iconResource) {
            mIconResource = iconResource;
            return this;
        }

        public MobirollerDialog.Builder setColor(final int color) {
            mColor = color;
            return this;
        }

        public MobirollerDialog.Builder setTitle(final String title) {
            mTitle = title;
            return this;
        }

        public MobirollerDialog.Builder setDescription(final String description) {
            mDescription = description;
            return this;
        }

        public MobirollerDialog.Builder setDescription(final Spannable description) {
            mSpannable = description;
            return this;
        }

        public MobirollerDialog.Builder setButtonText(final String buttonText) {
            mButtonText = buttonText;
            return this;
        }

        public MobirollerDialog.Builder setNegativeButtonText(final String buttonText) {
            mNegativeButtonText = buttonText;
            return this;
        }

        public MobirollerDialog.Builder setListener(final DialogButtonCallback callback) {
            mCallback = callback;
            return this;
        }

        public MobirollerDialog.Builder setListSelectionListener(final DialogListCallback callback) {
            mListCallback = callback;
            return this;
        }

        public MobirollerDialog.Builder setAdapter(final RecyclerView.Adapter adapter) {
            mAdapter = adapter;
            return this;
        }

        public MobirollerDialog.Builder setHasDivider(final boolean hasDivider) {
            mHasDivider = hasDivider;
            return this;
        }

        public MobirollerDialog.Builder setAutoDismiss(final boolean autoDismiss) {
            mAutoDismiss = autoDismiss;
            return this;
        }

        /**
         * Initialize the SharedPreference instance to used in the application.
         *
         * @throws RuntimeException if Context has not been set.
         */
        public MaterialDialog show() {
            if (mContext == null) {
                throw new RuntimeException("Context not set, please set context before building the Prefs instance.");
            }
            MaterialDialog materialDialog;

            if (mType == MobirollerDialogType.BASIC) {
                materialDialog = new MaterialDialog.Builder(mContext)
                        .customView(R.layout.mobiroller_dialog_basic, false)
                        .build();
                setBasic(materialDialog);
            } else if (mType == MobirollerDialogType.BUTTON) {
                materialDialog = new MaterialDialog.Builder(mContext)
                        .customView(R.layout.mobiroller_dialog_button, false)
                        .build();
                setBasic(materialDialog);
            } else {
                materialDialog = new MaterialDialog.Builder(mContext)
                        .customView(R.layout.mobiroller_dialog_list, false)
                        .stackingBehavior(StackingBehavior.NEVER)
                        .build();
                setList(materialDialog);
            }

            materialDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            ImageView imageView = materialDialog.getCustomView().findViewById(R.id.icon_image_view);
            RelativeLayout iconLayout = materialDialog.getCustomView().findViewById(R.id.icon_layout);
            TextView titleTextView = materialDialog.getCustomView().findViewById(R.id.title_text_view);
            TextView descriptionTextView = materialDialog.getCustomView().findViewById(R.id.description_text_view);
            titleTextView.setText(mTitle);
            if (mDescription != null)
                descriptionTextView.setText(mDescription);
            else if (mSpannable != null)
                descriptionTextView.setText(mSpannable);
            else
                descriptionTextView.setVisibility(View.GONE);

            if (mIconResource != 0) {
                imageView.setImageResource(mIconResource);
            }

            if (mType == MobirollerDialogType.LIST_WITH_BUTTON) {
                MobirollerButton confirmButton = materialDialog.getCustomView().findViewById(R.id.confirm_button);
                confirmButton.setText(mButtonText);
                confirmButton.setVisibility(View.VISIBLE);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCallback != null)
                            mCallback.onClickButton();
                        if (mAutoDismiss)
                            materialDialog.dismiss();
                    }
                });
            } else {
                MobirollerButton confirmButton = materialDialog.getCustomView().findViewById(R.id.confirm_button);
                if (confirmButton != null)
                    confirmButton.setVisibility(View.GONE);
            }

            if (mColor != 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    iconLayout.setBackgroundTintList(ColorStateList.valueOf(mColor));
                }
            }

            if (mType == MobirollerDialogType.BUTTON) {
                MobirollerButton confirmButton = materialDialog.getCustomView().findViewById(R.id.negative_button);
                confirmButton.setVisibility(View.VISIBLE);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialDialog.dismiss();
                    }
                });
            }
            descriptionTextView.setMovementMethod(new ScrollingMovementMethod());
            materialDialog.show();
            return materialDialog;

        }

        private void setBasic(MaterialDialog materialDialog) {
            MobirollerButton button = materialDialog.getCustomView().findViewById(R.id.button);

            button.setText(mButtonText);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.onClickButton();
                    }
                    materialDialog.dismiss();
                }
            });
        }

        private void setList(MaterialDialog materialDialog) {
            RecyclerView recyclerView = materialDialog.getCustomView().findViewById(R.id.list);
            if (mAdapter != null) {
                recyclerView.setAdapter(mAdapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        if (mListCallback != null)
                            mListCallback.onSelect(position);
                        materialDialog.dismiss();
                    }
                });
                if (mHasDivider) {
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                            linearLayoutManager.getOrientation());
                    recyclerView.addItemDecoration(dividerItemDecoration);
                }
            }
        }

    }

    public interface DialogButtonCallback {

        void onClickButton();

    }

    public interface DialogListCallback {

        void onSelect(int position);

    }

}
