package com.mobiroller.core.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiroller.core.R2;
import com.mobiroller.core.activities.ActivityHandler;
import com.mobiroller.core.adapters.ContentRecyclerAdapter;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.helpers.AppSettingsHelper;
import com.mobiroller.core.helpers.JSONStorage;
import com.mobiroller.core.helpers.LegacyProgressViewHelper;
import com.mobiroller.core.coreui.models.ScreenModel;
import com.mobiroller.core.coreui.models.TableItemsModel;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.R;
import com.mobiroller.core.util.ImageManager;
import com.mobiroller.core.views.ItemClickSupport;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NewApi")
public class aveMainListViewFragment extends BaseModuleFragment {

    LegacyProgressViewHelper legacyProgressViewHelper;

    @BindView(R2.id.content_img)
    ImageView imgView;
    @BindView(R2.id.content_text)
    TextView textView;
    @BindView(R2.id.scroll_text)
    ScrollView scrollView;
    @BindView(R2.id.content_list)
    RecyclerView list;
    @BindView(R2.id.content_list_layout)
    LinearLayout contentListLayout;
    @BindView(R2.id.content_layout)
    RelativeLayout contentLayout;
    @BindView(R2.id.content_overlay)
    RelativeLayout contentOverlay;

    private ArrayList<TableItemsModel> validNavItems = new ArrayList<>();

    public static final String KEY_TITLE = "title";
    public static final String KEY_IMAGE_URL = "img_url";
    public static final String KEY_SCREEN_TYPE = "screen_type";
    public static final String KEY_SCREEN_ID = "screen_id";

    private ArrayList<HashMap<String, String>> contentList = new ArrayList<>();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content, container, false);
        unbinder = ButterKnife.bind(this, view);
        legacyProgressViewHelper = new LegacyProgressViewHelper(getActivity());
        hideToolbar(view.findViewById(R.id.toolbar_top));
        loadUi();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (contentLayout != null) {
            bannerHelper.addBannerAd(contentOverlay,contentLayout);
        }
    }

    private void loadUi() {
        if(screenModel==null)
            screenModel = JSONStorage.getScreenModel(screenId);
        if(screenModel==null)
            return;
        try {
            ImageManager.loadBackgroundImageFromImageModel(contentOverlay, screenModel.getBackgroundImageName());
            setMainImageView();
            setContentText();
            setDataList();
            setContentList();
            setListComponent();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private ScreenModel checkTableItems(ScreenModel itemsObj) {
        ArrayList<TableItemsModel> jsonArray = new ArrayList<>();
        String[] activities = getResources().getStringArray(R.array.activities);
        ArrayList<TableItemsModel> navItems = itemsObj.getTableItems();
        for (int i = 0; i < navItems.size(); i++) {
            boolean flag = false;
            for (String activity : activities) {
                if (navItems.get(i).getScreenType().equalsIgnoreCase(activity)) {
                    if(navItems.get(i).getScreenType().equalsIgnoreCase("aveHtmlView") && navItems.get(i).screenSubType.equalsIgnoreCase("aveWeatherView"))
                    {
                        flag = false;
                        break;
                    }else {
                        flag = true;
                        break;
                    }
                }
            }
            if (flag)
                jsonArray.add(navItems.get(i));
        }
        itemsObj.setTableItems(jsonArray);
        return itemsObj;
    }

    private ArrayList<TableItemsModel> getValidItems(ArrayList<TableItemsModel> navigationItemModels) {
        String userRole = sharedPrefHelper.getUserRole();
        ArrayList<TableItemsModel> validItems = new ArrayList<>();
        try {
            if (navigationItemModels.size() > 0) {
                for (int i = 0; i < navigationItemModels.size(); i++) {

                    if (navigationItemModels.get(i).getScreenType().equalsIgnoreCase(Constants.MODULE_ECOMMERCE_VIEW) && !AppSettingsHelper.isECommerceActive())
                        continue;
                    if (navigationItemModels.get(i).getScreenType().equalsIgnoreCase(Constants.MODULE_ECOMMERCE_PRO_VIEW) && !AppSettingsHelper.isECommerceActive())
                        continue;
                    if (navigationItemModels.get(i).getScreenType().equalsIgnoreCase(Constants.MODULE_HOTEL_VIEW) && !AppSettingsHelper.isTourVisioActive())
                        continue;
                    if (navigationItemModels.get(i).getScreenType().equalsIgnoreCase("aveChatView") && !UtilManager.sharedPrefHelper().getIsChatActive())
                        continue;

                    if (!navigationItemModels.get(i).isLoginActive())
                        validItems.add(navigationItemModels.get(i));
                    else {
                        if(sharedPrefHelper.getUserLoginStatus()) {
                            if(navigationItemModels.get(i).getRoles().size() > 0) {
                                for (int j = 0; j < navigationItemModels.get(i).getRoles().size(); j++) {
                                    if (navigationItemModels.get(i).getRoles().get(j).equalsIgnoreCase(userRole)) {
                                        validItems.add(navigationItemModels.get(i));
                                        break;
                                    }
                                }
                            }else {
                                validItems.add(navigationItemModels.get(i));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return validItems;
    }

    private void setContentList()
    {
        for (int i = 0; i < validNavItems.size(); i++) {
            TableItemsModel jsonObject = validNavItems.get(i);
            String title = localizationHelper.getLocalizedTitle(jsonObject.getTitle());
            String icon = null;
            if (jsonObject.getImageName() != null) {
                icon = jsonObject.getImageName().getImageURL();
            }

            String screenType = jsonObject.getScreenType();
            String accountScreenID = jsonObject.getAccountScreenID();

            HashMap<String, String> map = new HashMap<String, String>();

            map.put(KEY_TITLE, title);
            map.put(KEY_IMAGE_URL, icon);
            map.put(KEY_SCREEN_TYPE, screenType);
            map.put(KEY_SCREEN_ID, accountScreenID);

            contentList.add(map);
        }
    }

    private void setListComponent(){
        ContentRecyclerAdapter adapter = new ContentRecyclerAdapter(getActivity(), contentList, screenModel, screenHelper, sharedPrefHelper);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setVisibility(View.VISIBLE);

        ItemClickSupport.addTo(list).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, final int position, View v) {
                if (validNavItems.get(position).getAccountScreenID()!=null && Integer.valueOf(validNavItems.get(position).getAccountScreenID()) != -1) {
                    ActivityHandler.startActivity(getActivity(),validNavItems.get(position));
                } else {
                    v.setEnabled(false);
                }
            }
        });
    }

    private void setMainImageView()
    {
        if (screenModel.getMainImageName() != null && screenModel.getMainImageName().getImageURL() != null) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(imgView.getLayoutParams());
            lp.setMargins(imgView.getPaddingLeft(), 0, imgView.getPaddingRight(), 0);
            imgView.setLayoutParams(lp);
            componentHelper.setMainImage(getActivity(), imgView, screenModel);
            imgView.setScaleType(ScaleType.FIT_XY);
            imgView.setVisibility(View.VISIBLE);
        } else {
            imgView.setVisibility(View.GONE);
        }
    }

    private void setContentText()
    {
        textView.setMovementMethod(new ScrollingMovementMethod());
        if (screenModel.getContentText() != null && !screenModel.getContentText().equalsIgnoreCase("")) {
            componentHelper.setMainTextView(getActivity(), textView, screenModel);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    private void setDataList()
    {
        ArrayList<TableItemsModel> jsonArray = checkTableItems(screenModel).getTableItems(); // checkTableItems to include only supported modules

        if (jsonArray.size() > 0) {
            validNavItems = getValidItems(jsonArray);
        } else {
            RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
            relativeParams.setMargins(0, 0, 0, screenHelper.getHeightForDevice(5, getActivity()));  // left, top, right, bottom
            scrollView.setLayoutParams(relativeParams);
        }
    }
}
