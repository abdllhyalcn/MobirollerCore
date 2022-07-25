package com.mobiroller.core.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mobiroller.core.R2;
import com.mobiroller.core.SharedApplication;
import com.mobiroller.core.adapters.FormAdapter;
import com.mobiroller.core.helpers.NetworkHelper;
import com.mobiroller.core.helpers.LegacyProgressViewHelper;
import com.mobiroller.core.helpers.RxJavaRequestHelper;
import com.mobiroller.core.models.SubmitModel;
import com.mobiroller.core.coreui.models.TableItemsModel;
import com.mobiroller.core.models.events.FormImageEvent;
import com.mobiroller.core.R;
import com.mobiroller.core.util.DialogUtil;
import com.mobiroller.core.util.ImageManager;
import com.mobiroller.core.viewholders.forms.FormBaseViewHolder;
import com.mobiroller.core.viewholders.forms.SubmitViewHolder;
import com.mobiroller.core.views.CustomHorizontalScrollView;
import com.canhub.cropper.CropImage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class aveFormViewFragment extends BaseModuleFragment {

    LegacyProgressViewHelper legacyProgressViewHelper;

    @BindView(R2.id.form_img)
    ImageView imgView;
    @BindView(R2.id.form_text)
    TextView mFormText;
    @BindView(R2.id.form_scroll_text)
    CustomHorizontalScrollView scrollView;
    @BindView(R2.id.form_layout)
    RelativeLayout formLayout;
    @BindView(R2.id.form_layout_overlay)
    RelativeLayout formLayoutOverlay;
    @BindView(R2.id.form_main_layout)
    RelativeLayout formMainLayout;

    @BindView(R2.id.form_list)
    RecyclerView formList;

    private boolean isSubmitAvailable;

    Unbinder unbinder;

    private FormAdapter adapter;
    private ArrayList<Object> dataList = new ArrayList<>();
    private Parcelable mListState;
    private boolean hasImage = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_form, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        legacyProgressViewHelper = new LegacyProgressViewHelper(getActivity());
        return view;
    }

    private void runThread() {

        for (TableItemsModel model : screenModel.getTableItems()) {
            if (model.getType().equalsIgnoreCase("formImage")) {
                hasImage = true;
            }
        }
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (adapter == null) {
                    dataList.addAll(screenModel.getTableItems());
                    setFormAdapter(dataList, false);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (formLayout != null) {
            bannerHelper.addBannerAd(formMainLayout, formLayoutOverlay);
        }
        runThread();

    }

    @Subscribe
    public void subscribeSubmitModel(SubmitModel submitModel) {
        submitForm();
    }

    private void loadUi() {
        try {
            if (isSubmitAvailable) {
                ImageManager.loadBackgroundImageFromImageModel(formLayoutOverlay, screenModel.getBackgroundImageName());
                scrollView.setBackgroundColor(Color.TRANSPARENT);
                int px = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        13,
                        getResources().getDisplayMetrics()
                );
                ViewGroup.MarginLayoutParams marginLayoutParams =
                        (ViewGroup.MarginLayoutParams) formList.getLayoutParams();
                marginLayoutParams.setMargins(px, px, px, 0);
                formList.setLayoutParams(marginLayoutParams);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) scrollView
                        .getLayoutParams();

                layoutParams.setMargins(0, 0, 0, px);
                scrollView.setLayoutParams(layoutParams);
            } else
                scrollView.setBackgroundColor(Color.WHITE);
            if (screenModel.getMainImageName() != null) {
                componentHelper.setMainImage(getActivity(), imgView, screenModel);
                imgView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void submitForm() {

        if(!NetworkHelper.isConnected(SharedApplication.context)) {
            DialogUtil.showNoConnectionInfo(getActivity());
            return;
        }

        boolean isValid = true;
        for (int i = 0; i < adapter.getItemCount() - 1; i++) {
            if (!((FormBaseViewHolder) formList.findViewHolderForAdapterPosition(i)).isValid())
                isValid = false;
        }
        closeKeyboard();
        final CircularProgressButton mSubmitButton = ((SubmitViewHolder) formList.findViewHolderForAdapterPosition(adapter.getItemCount() - 1)).getSubmitButton();
        if (isValid && !hasImage) {
            mSubmitButton.startAnimation();
            HashMap<String, String> map = new HashMap<>();
            for (int i = 0; i < adapter.getItemCount() - 1; i++) {
                map.put(((FormBaseViewHolder) formList.findViewHolderForAdapterPosition(i)).getId(), ((FormBaseViewHolder) formList.findViewHolderForAdapterPosition(i)).getValue());
            }
            RxJavaRequestHelper rxJavaRequestHelper = new RxJavaRequestHelper(getActivity(), sharedPrefHelper);
            rxJavaRequestHelper.getAPIService().sendForm(map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    mSubmitButton.revertAnimation();
                    new MaterialDialog.Builder(getActivity())
                            .title(R.string.success)
                            .content(R.string.form_success)
                            .positiveText(getString(R.string.OK))
                            .show();

                    //clear all fields
                    for (int i = 0; i < adapter.getItemCount() - 1; i++) {
                        ((FormBaseViewHolder) formList.findViewHolderForAdapterPosition(i)).clear();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    mSubmitButton.revertAnimation();
                    new MaterialDialog.Builder(getActivity())
                            .title(R.string.failed)
                            .content(R.string.form_failed)
                            .positiveText(R.string.OK)
                            .negativeText(R.string.try_again)
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                                    submitForm();
                                }
                            })
                            .show();
                }
            });

        } else if (isValid) {
            mSubmitButton.startAnimation();
            HashMap<String, RequestBody> map = new HashMap<>();
            List<MultipartBody.Part> list = new ArrayList<>();
            for (int i = 0; i < adapter.getItemCount() - 1; i++) {
                if (((FormBaseViewHolder) formList.findViewHolderForAdapterPosition(i)).isImage()) {
                    if (((FormBaseViewHolder) formList.findViewHolderForAdapterPosition(i)).getImage() != null)
                        list.add(MultipartBody.Part.createFormData(((FormBaseViewHolder) formList.findViewHolderForAdapterPosition(i)).getId(), "photo.png",
                                RequestBody.create(MediaType.parse("image/*"), ((FormBaseViewHolder) formList.findViewHolderForAdapterPosition(i)).getImage())));
                } else
                    map.put(((FormBaseViewHolder) formList.findViewHolderForAdapterPosition(i)).getId(), RequestBody.create(MediaType.parse("multipart/form-data"), ((FormBaseViewHolder) formList.findViewHolderForAdapterPosition(i)).getValue()));

            }
            RxJavaRequestHelper rxJavaRequestHelper = new RxJavaRequestHelper(getActivity(), sharedPrefHelper);
            rxJavaRequestHelper.getAPIService().sendFormWithImages(map, list).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    mSubmitButton.revertAnimation();
                    new MaterialDialog.Builder(getActivity())
                            .title(R.string.success)
                            .content(R.string.form_success)
                            .positiveText(getString(R.string.OK))
                            .show();

                    //clear all fields
                    for (int i = 0; i < adapter.getItemCount() - 1; i++) {
                        ((FormBaseViewHolder) formList.findViewHolderForAdapterPosition(i)).clear();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    mSubmitButton.revertAnimation();
                    new MaterialDialog.Builder(getActivity())
                            .title(R.string.failed)
                            .content(R.string.form_failed)
                            .positiveText(R.string.OK)
                            .negativeText(R.string.try_again)
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                                    submitForm();
                                }
                            })
                            .show();
                }
            });

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(screenId + "list", adapter.getItems());
        outState.putBoolean(screenId + "isSubmitAvailable", isSubmitAvailable);
        mListState = formList.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(String.valueOf(screenId), mListState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        // Retrieve list state and list/item positions
        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(String.valueOf(screenId));
            isSubmitAvailable = savedInstanceState.getBoolean(String.valueOf(screenId) + "isSubmitAvailable");
            ArrayList<Object> objectArrayList = (ArrayList<Object>) savedInstanceState.getSerializable(String.valueOf(screenId) + "list");
            setFormAdapter(objectArrayList, true);
            formList.getLayoutManager().onRestoreInstanceState(mListState);
        }
    }

    private void setFormAdapter(ArrayList<Object> objectArrayList, boolean isRestored) {
        validateFormFields();
        if (isRestored)
            dataList.addAll(objectArrayList);
        if (!isRestored && screenModel.getSubmitAvailable().equalsIgnoreCase("YES")) {
            isSubmitAvailable = true;
            dataList.add(new SubmitModel());
        }

        if (!screenModel.getContentText().isEmpty()) {
            if (!isSubmitAvailable && !isRestored) {
                TableItemsModel tableItemsModel = new TableItemsModel();
                tableItemsModel.setType("address");
                tableItemsModel.setValue(screenModel.getContentText());
                dataList.add(0, tableItemsModel);
            } else {
                mFormText.setText(localizationHelper.getLocalizedTitle(screenModel.getContentText()));
                mFormText.setVisibility(View.VISIBLE);
            }
        }
        loadUi();
        if (isSubmitAvailable)
            adapter = new FormAdapter(getActivity(),
                    dataList,
                    Color.parseColor("#505050"),
                    Color.parseColor("#505050"), this);
        else
            adapter = new FormAdapter(getActivity(),
                    dataList,
                    sharedPrefHelper.getActionBarColor(),
                    sharedPrefHelper.getActionBarColor(), this);
        formList.setLayoutManager(new LinearLayoutManager(getActivity()));
        formList.setAdapter(adapter);
        formList.setNestedScrollingEnabled(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data!=null ) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (result != null && resultCode == RESULT_OK) {
                Uri imageResultUri = result.getUriContent();
                EventBus.getDefault().post(new FormImageEvent(requestCode, imageResultUri));
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void validateFormFields() {
        ArrayList<String> formValidationFields = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.form_fields_validation)));
        if (screenModel.getTableItems() != null && screenModel.getTableItems().size() != 0) {
            for (TableItemsModel item :
                    screenModel.getTableItems()) {
                if (!formValidationFields.contains(item.getType()))
                    screenModel.getTableItems().remove(item);
            }
        }
    }
}
