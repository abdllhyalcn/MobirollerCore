package com.mobiroller.core.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiroller.core.R2;
import com.mobiroller.core.activities.ImagePagerActivity;
import com.mobiroller.core.adapters.ImageAdapter;
import com.mobiroller.core.coreui.models.GalleryModel;
import com.mobiroller.core.R;
import com.mobiroller.core.util.InterstitialAdsUtil;
import com.mobiroller.core.views.ItemClickSupport;
import com.mobiroller.core.views.twowayview.GridLayoutManager;
import com.mobiroller.core.views.twowayview.TwoWayLayoutManager;
import com.mobiroller.core.views.twowayview.TwoWayView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mobiroller.core.activities.ImagePagerActivity.TOOLBAR_TITLE;

@SuppressLint("NewApi")
public class avePhotoGalleryViewFragment extends BaseModuleFragment {

    @BindView(R2.id.gallery_grid_view)
    TwoWayView recyclerView;
    @BindView(R2.id.gallery_layout)
    RelativeLayout galleryRelativeLayout;

    private ArrayList<GalleryModel> jsonArray;

    private boolean isDownloadable = false;
    ImageAdapter imageAdapter = null;
    private InterstitialAdsUtil interstitialAdsUtil;

    /*
       If layoutType is 1 ,two image for each row
       If layoutType is 2 three image for each row
       If layoutType is 3 one image for each row
       If layoutType is 4 one image with caption for each row
       If layoutType is 5 staggered
        */
    private int layoutType = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery, container, false);
        unbinder = ButterKnife.bind(this, view);
        interstitialAdsUtil = new InterstitialAdsUtil((AppCompatActivity) getActivity());
        hideToolbar(view.findViewById(R.id.toolbar_top));
        loadUI();

        return view;
    }

    private void startImagePagerActivity(int position) {
        Intent intent = new Intent(getActivity(), ImagePagerActivity.class);
        intent.putExtra(TOOLBAR_TITLE, localizationHelper.getLocalizedTitle(screenModel.getTitle()));
        intent.putExtra("imageList", jsonArray);
        intent.putExtra("fromGalleryView", true);
        intent.putExtra("isDownloadable", isDownloadable);
        intent.putExtra("position", position);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getActivity(), ((ImageAdapter.ImageViewHolder) recyclerView.findViewHolderForLayoutPosition(position)).imageView, "galleryImage");
            interstitialAdsUtil.checkInterstitialAds(intent, options.toBundle());
        } else
            interstitialAdsUtil.checkInterstitialAds(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (galleryRelativeLayout != null) {
            bannerHelper.addBannerAd(galleryRelativeLayout, recyclerView);
        }
    }

    public void loadUI() {

        if (networkHelper.isConnected()) {
            jsonArray = screenModel.getImages();

            // if request object has type value, than set the layout type for given value
            if (screenModel.getType() != null) {
                try {
                    layoutType = Integer.parseInt(screenModel.getType());
                } catch (Exception e) {
                    // type error
                }
            }
            isDownloadable = screenModel.isDownloadable();

            recyclerView.setItemAnimator(null);
            recyclerView.setHasFixedSize(true);

            double padding_in_dp = 1.35;  // 6 dps
            final float scale = getActivity().getResources().getDisplayMetrics().density;
            int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
            switch (layoutType) {
                case 1:
                    recyclerView.setLayoutManager(new GridLayoutManager(TwoWayLayoutManager.Orientation.VERTICAL, 2, 1));
                    imageAdapter = new ImageAdapter(getActivity(), jsonArray, recyclerView, layoutType, localizationHelper);
                    break;
                case 2:
                    recyclerView.setLayoutManager(new GridLayoutManager(TwoWayLayoutManager.Orientation.VERTICAL, 3, 1));
                    recyclerView.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
                    imageAdapter = new ImageAdapter(getActivity(), jsonArray, recyclerView, layoutType, localizationHelper);
                    break;
                case 3:
                    recyclerView.setLayoutManager(new GridLayoutManager(TwoWayLayoutManager.Orientation.VERTICAL, 1, 1));
                    imageAdapter = new ImageAdapter(getActivity(), jsonArray, recyclerView, layoutType, localizationHelper);
                    break;
                case 4:
                    recyclerView.setLayoutManager(new GridLayoutManager(TwoWayLayoutManager.Orientation.VERTICAL, 1, 1));
                    imageAdapter = new ImageAdapter(getActivity(), jsonArray, recyclerView, layoutType, localizationHelper);
                    break;
                case 5:
                    recyclerView.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
                    imageAdapter = new ImageAdapter(getActivity(), jsonArray, recyclerView, layoutType, localizationHelper);
                    break;
            }
            recyclerView.setAdapter(imageAdapter);
            ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    startImagePagerActivity(position);
                }
            });

        } else {
            Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.please_check_your_internet_connection), Toast.LENGTH_LONG).show();
        }

    }
}
