package com.mobiroller.core.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mobiroller.core.R2;
import com.mobiroller.core.activities.aveRssContentViewPager;
import com.mobiroller.core.adapters.RssAdapter;
import com.mobiroller.core.coreui.views.MProgressView;
import com.mobiroller.core.helpers.LegacyProgressViewHelper;
import com.mobiroller.core.models.AdMobModel;
import com.mobiroller.core.models.RssFeaturedHeaderModel;
import com.mobiroller.core.models.RssModel;
import com.mobiroller.core.models.RssSliderHeaderModel;
import com.mobiroller.core.R;
import com.mobiroller.core.util.ImageManager;
import com.mobiroller.core.coreui.helpers.EndlessRecyclerViewScrollListener;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import static com.mobiroller.core.constants.Constants.KEY_SCREEN_RSS_PUSH_TITLE;
import static com.mobiroller.core.util.RssUtil.rssGetAuthor;
import static com.mobiroller.core.util.RssUtil.rssGetDescription;
import static com.mobiroller.core.util.RssUtil.rssGetImageUrl;

/**
 * Fill and display rss list
 * (https://github.com/rometools/rome to parse rss)
 */

public class aveRSSViewFragment extends BaseModuleFragment implements SwipeRefreshLayout.OnRefreshListener {

    LegacyProgressViewHelper legacyProgressViewHelper;

    @BindView(R2.id.rss_main_img)
    ImageView rssMainImg;
    @BindView(R2.id.postListView)
    RecyclerView recyclerView;
    @BindView(R2.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R2.id.rss_list_layout)
    LinearLayout rssListLayout;
    @BindView(R2.id.rss_layout)
    RelativeLayout rssLayout;
    @BindView(R2.id.refresh_button)
    Button refreshButton;
    @BindView(R2.id.overlay_layout)
    RelativeLayout rssLayoutOverlay;
    @BindView(R2.id.main_layout)
    RelativeLayout mainLayout;
    Unbinder unbinder;

    private String urlString;
    private ArrayList<Object> listData;
    private ArrayList<Object> listDataAdapter = new ArrayList<>();
    private MProgressView loadMoreProgress;
    private RssAdapter rssListAdapter;
    private ArrayList<Object> postDataList = new ArrayList<>();
    private int pagination = 1;
    private int layoutType = 9;
    private boolean isRefreshLoading = false, isLoadMore = false, isFirst = true, isTaskCancelled = false;
    private Parcelable mListState;
    // Dynamic layout types
    private List<Integer> layoutList = Arrays.asList(0,
            R.layout.rss_list_item_full_image,
            R.layout.rss_list_item_square_image,
            R.layout.rss_list_item_full_image_above_title,
            R.layout.rss_list_item_square_featured,
            R.layout.rss_list_item_square_featured,
            R.layout.rss_list_item_full_image_above_title,
            R.layout.rss_list_item_classic,
            R.layout.rss_list_item_classic_no_image,
            R.layout.rss_list_item_full_image,
            R.layout.rss_list_item_no_image_stragged
    );

    // Native ads height for each layout type
    public static List<Integer> layoutRssListHeight;

    /**
     *
     */
    private String paginationParameter = "?paged=";

    // A Native Express ad is placed in every nth position in the RecyclerView.
    private static int ITEMS_PER_AD = 7;
    private static int START_AT = 3;
    private RssDataController mTask;

    private String rssPushTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_postlist, container, false);
        unbinder = ButterKnife.bind(this, view);
        Bundle bundle = this.getArguments();
        layoutRssListHeight = Arrays.asList(0,
                getResources().getInteger(R.integer.rss_layout_1),
                getResources().getInteger(R.integer.rss_layout_2),
                getResources().getInteger(R.integer.rss_layout_2),
                getResources().getInteger(R.integer.rss_layout_4),
                getResources().getInteger(R.integer.rss_layout_5),
                getResources().getInteger(R.integer.rss_layout_2),
                getResources().getInteger(R.integer.rss_layout_2),
                getResources().getInteger(R.integer.rss_layout_2),
                getResources().getInteger(R.integer.rss_layout_1),
                getResources().getInteger(R.integer.rss_layout_2)
        );
        legacyProgressViewHelper = new LegacyProgressViewHelper(getActivity());
        loadMoreProgress = view.findViewById(R.id.load_more_progress_view);
        loadMoreProgress.setColor(sharedPrefHelper.getActionBarColor());
        loadMoreProgress.setVisibility(View.GONE);
        rssLayout = view.findViewById(R.id.rss_layout);

        if (bundle.containsKey(KEY_SCREEN_RSS_PUSH_TITLE)) {
            rssPushTitle = bundle.getString(KEY_SCREEN_RSS_PUSH_TITLE);
        }
        listData = new ArrayList<>();
        loadUi();
        calculateAdCount();
        return view;
    }

    private void loadUi() {

        ImageManager.loadBackgroundImageFromImageModel(rssLayoutOverlay, screenModel.getBackgroundImageName());
        urlString = screenModel.getRssLink().trim();
        if (screenModel.getType() != null) {
            try {
                layoutType = Integer.parseInt(screenModel.getType());
            } catch (Exception e) {
                // type error
            }
        }
        // if request object has type value, than set the layout type for given value

        mSwipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setVisibility(View.VISIBLE);
        setLayoutType();
    }

    @Override
    public void onRefresh() {
        isRefreshLoading = true;
        mTask = new RssDataController();
        mTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, urlString);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListState == null && (rssListAdapter == null || rssListAdapter.getItemCount() == 0)) {
            onRefresh();
        }
        if (mTask != null && mTask.isCancelled() && isTaskCancelled) {
            mTask = new RssDataController();
            mTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, urlString);
        }

        if (!sharedPrefHelper.getIsNativeAdEnabled() && mainLayout != null) {
            bannerHelper.addBannerAd(mainLayout, rssLayoutOverlay);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTask != null) {
            if (mTask.getStatus() == AsyncTask.Status.RUNNING || mTask.getStatus() == AsyncTask.Status.PENDING) {
                mTask.cancel(true);
                isTaskCancelled = true;
            }
        }
    }

    private class RssDataController extends
            AsyncTask<String, Integer, ArrayList<Object>> {
        protected void onPreExecute() {
            if ((!isRefreshLoading && !isLoadMore) || isFirst) {
                if (legacyProgressViewHelper == null)
                    legacyProgressViewHelper = new LegacyProgressViewHelper(getActivity());
                if (getUserVisibleHint())
                    legacyProgressViewHelper.show();
            }
        }

        @Override
        protected ArrayList<Object> doInBackground(String... params) {
            String urlStr = params[0];
            postDataList = new ArrayList<>();
            try {
                String result = "";
                //Get rss file and parse to SyndFeed model
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(urlStr)
                        .get()
                        .build();
                OkHttpClient client = new OkHttpClient();

                try {
                    Response response = client.newCall(request).execute();
                    result = response.body().string();

                } catch (IOException e) {
                    Log.e("FeedFetcher", "something went wrong on ");
                }
                SyndFeed feed = new SyndFeedInput().build(new StringReader(result));
//                XmlReader xmlReader = new XmlReader(new URL(urlStr));
//                SyndFeed feed = new SyndFeedInput().build(xmlReader);
                for (SyndEntry item : feed.getEntries()) {
                    setRssModel(item);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return postDataList;
        }

        @Override
        protected void onPostExecute(final ArrayList<Object> result) {
            onPostExecuted(result, false);
        }
    }

    private void onPostExecuted(final ArrayList<Object> result, final boolean isRestore) {
        try {
            if (isRefreshLoading || isFirst) {
                listData = new ArrayList<>();
                listDataAdapter = new ArrayList<>();
                if (rssListAdapter != null)
                    rssListAdapter.notifyDataSetChanged();
                for (int i = 0; i < result.size(); i++) {
                    listData.add(result.get(i));
                }
                mSwipeRefreshLayout.setRefreshing(false);
                isRefreshLoading = false;
                setLayoutType();
                recyclerView.smoothScrollToPosition(0);
            } else if (isLoadMore) {
                if (getActivity() == null)
                    return;
                recyclerView.setItemAnimator(null);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < result.size(); i++) {
                            if (!isContains((RssModel) result.get(i))) {
                                listData.add(result.get(i));
                                listDataAdapter.add(result.get(i));
                                rssListAdapter.notifyItemInserted(listDataAdapter.size());
                                if (!isRestore)
                                    addAdMobModel(listDataAdapter.size());
                            }
                        }
                        isLoadMore = false;
                        loadMoreProgress.setVisibility(View.GONE);
                    }
                });
            }
            if ((!isRefreshLoading && !isLoadMore) || isFirst) {
                legacyProgressViewHelper.dismiss();
                isFirst = false;
            }
            legacyProgressViewHelper.dismiss();
            aveRssContentViewPager.setRssModelList(rssListAdapter.getAllData());
            aveRssContentViewPager.notifyAdapter();
        } catch (Exception ignore) {
            if (legacyProgressViewHelper != null)
                legacyProgressViewHelper.dismiss();
            ignore.printStackTrace();
            // ignored exception
        }
    }

    /**
     * Check if the post is already in the list,
     * if it contains the same element than do
     * not include this object
     *
     * @param rssModel
     * @return
     */
    private boolean isContains(RssModel rssModel) {
        if (listData.contains(rssModel))
            return true;
        for (int j = 0; j < listData.size(); j++) {
            if ((listData.get(j) instanceof RssModel) && ((RssModel) listData.get(j)).getTitle().equalsIgnoreCase((rssModel.getTitle())))
                return true;
            if (listData.get(j) instanceof RssSliderHeaderModel) {
                for (int i = 0; i < ((RssSliderHeaderModel) listData.get(j)).getDataList().size(); i++) {
                    if (((RssSliderHeaderModel) listData.get(j)).getDataList().get(i) instanceof RssModel && ((RssModel) ((RssSliderHeaderModel) listData.get(j)).getDataList().get(i)).getTitle().equalsIgnoreCase((rssModel.getTitle())))
                        return true;
                }
            }
            if (listData.get(j) instanceof RssFeaturedHeaderModel) {
                if (((RssFeaturedHeaderModel) listData.get(j)).getFeaturedHeader().getTitle().equalsIgnoreCase((rssModel.getTitle())))
                    return true;
            }
        }
        return false;
    }

    private void setRssModel(SyndEntry syndEntry) {

        postDataList.add(new RssModel(syndEntry.getTitle(),
                rssGetImageUrl(syndEntry),
                rssGetDescription(syndEntry),
                syndEntry.getLink(),
                rssGetAuthor(),
                syndEntry.getPublishedDate()));
    }

    /**
     * Set layout type according to dynamic type
     * parameter for related rss screen
     */
    private void setLayoutType() {
        int LINEAR_MANAGER_TYPE = 1; // Determines which type of layout set to related recycle view
        int GRID_MANAGER_TYPE = 2;
        int managerType = LINEAR_MANAGER_TYPE;
        boolean staggered = false;
        if (layoutType == 1 || layoutType == 9)
            staggered = true;

        rssListAdapter = new RssAdapter(getActivity(), listDataAdapter, screenModel, layoutList.get(layoutType), screenId, staggered, layoutType);
        recyclerView.setAdapter(rssListAdapter);

        if (layoutType == 1 || layoutType == 5 || layoutType == 9 || layoutType == 10)
            managerType = GRID_MANAGER_TYPE;

        if (layoutType == 1 || layoutType == 2 || layoutType == 3) {
            if (listData.size() >= 5)
                setSlider();
            else
                setListAdapter(0);
        } else if (layoutType == 5) {
            if (listData.size() != 0)
                setFeaturedHeader();
        } else
            setListAdapter(0);

        if (layoutType == 6 || layoutType == 7)
            rssLayout.setPadding(0, screenHelper.getHeightForDevice(5, getActivity()), 0, 0);

        StaggeredGridLayoutManager staggeredGridLayoutManager;
        if (managerType == LINEAR_MANAGER_TYPE)
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        else
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                isRefreshLoading = false;
                isLoadMore = true;
                loadMoreProgress.setVisibility(View.VISIBLE);
                mTask = new RssDataController();
                mTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, urlString + paginationParameter + (++pagination));
            }
        });

        recyclerView.setHasFixedSize(false);
    }

    private void setSlider() {

        if (listData.size() != 0) {
            listData.add(0, new RssSliderHeaderModel(new ArrayList<>(listData.subList(0, 5))));
            listData.remove(1);
            listData.remove(1);
            listData.remove(1);
            listData.remove(1);
            listData.remove(1);
        }
        setListAdapter(0);
    }

    /**
     * Featured item to display on top of screen bigger and stick (the latest element of rss)
     */
    private void setFeaturedHeader() {
        listData.add(0, new RssFeaturedHeaderModel((RssModel) listData.get(0)));
        listData.remove(1);
        setListAdapter(0);
    }

    /**
     * Add parsed list items to adapter also
     * check & add native ads if it's enabled
     *
     * @param start
     */
    private void setListAdapter(int start) {

        for (int i = start; i < listData.size(); i++) {
            listDataAdapter.add(listData.get(i));
            rssListAdapter.notifyItemInserted(listDataAdapter.size() - 1);
            addAdMobModel(listDataAdapter.size());
        }
        if (listData.size() > START_AT && sharedPrefHelper.getIsNativeAdEnabled()) {
            if (layoutType == 1 || layoutType == 5) {
                listDataAdapter.add(START_AT, new AdMobModel());
                rssListAdapter.notifyItemInserted(START_AT);
            } else {
                listDataAdapter.add(START_AT - 1, new AdMobModel());
                rssListAdapter.notifyItemInserted(START_AT - 1);
            }
        }

        if (rssPushTitle != null && isFirst)
            rssListAdapter.findAndOpenContent(rssPushTitle);
    }

    /**
     * If ads enabled add related ad object in right place
     * depending of which layout type is active
     *
     * @param size
     */
    private void addAdMobModel(int size) {
        if (sharedPrefHelper.getIsNativeAdEnabled()) {
            if (layoutType == 1 || layoutType == 5) {
                if (size - 1 > ITEMS_PER_AD && (size - 1) % ITEMS_PER_AD == START_AT) {
                    listDataAdapter.add(new AdMobModel());
                    rssListAdapter.notifyItemInserted(listDataAdapter.size() - 1);
                }
            } else if (size % ITEMS_PER_AD == START_AT && size > ITEMS_PER_AD) {
                listDataAdapter.add(new AdMobModel());
                rssListAdapter.notifyItemInserted(listDataAdapter.size() - 1);
            }
        }
    }

    /**
     * Calculates the number of ads to avoid
     * displaying more then one ad on list screen
     */
    private void calculateAdCount() {
        float viewHeight = layoutRssListHeight.get(layoutType);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = getResources().getDisplayMetrics().density;
        float height = outMetrics.heightPixels / density;
        ITEMS_PER_AD = (int) (height / viewHeight);
        ITEMS_PER_AD++;
        if (layoutType == 1 || layoutType == 5 || layoutType == 9 || layoutType == 10)
            ITEMS_PER_AD = ITEMS_PER_AD * 2;

        if (ITEMS_PER_AD < 5)
            ITEMS_PER_AD = 6;

        if (ITEMS_PER_AD % 2 == 1)
            ITEMS_PER_AD++;

    }
    
}