package com.mobiroller.core.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.mobiroller.core.activities.aveRssContentViewPager;
import com.mobiroller.core.containers.ClickableViewPager;
import com.mobiroller.core.helpers.SharedPrefHelper;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.models.AdMobModel;
import com.mobiroller.core.models.RssFeaturedHeaderModel;
import com.mobiroller.core.models.RssModel;
import com.mobiroller.core.models.RssSliderHeaderModel;
import com.mobiroller.core.coreui.models.ScreenModel;
import com.mobiroller.core.R;
import com.mobiroller.core.util.InterstitialAdsUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.mobiroller.core.constants.Constants.KEY_RSS_LAYOUT_TYPE;
import static com.mobiroller.core.constants.Constants.KEY_RSS_POSITION;
import static com.mobiroller.core.constants.Constants.KEY_RSS_TITLE;
import static com.mobiroller.core.constants.Constants.KEY_SCREEN_ID;
import static com.mobiroller.core.helpers.StringSimilarityHelper.editDistance;

/**
 * Created by ealtaca on 20.12.2016.
 */

public class RssAdapter extends NativeAdsAdapter {

    private String screenId;
    private int type = 0;
    private int layoutType;
    private boolean staggered;
    private Activity activity;
    private final int AD_TYPE = 1;
    private final int RSS_TYPE = 0;
    private final int RSS_FEATURED_HEADER_TYPE = 2;
    private final int RSS_SLIDER_HEADER_TYPE = 3;
    public ScreenModel screenModel;
    private ArrayList<Object> dataList;
    private DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
    private SharedPrefHelper sharedPrefHelper;
    private InterstitialAdsUtil interstitialAdsUtil;


    public RssAdapter(Activity activity, ArrayList<Object> dataList, ScreenModel screenModel, int layoutType, String screenId, boolean staggered, int type) {
        super(activity);
        this.activity = activity;
        this.layoutType = layoutType;
        this.type = type;
        this.staggered = staggered;
        this.dataList = dataList;
        this.screenModel = screenModel;
        this.screenId = screenId;
        sharedPrefHelper = UtilManager.sharedPrefHelper();
        interstitialAdsUtil = new InterstitialAdsUtil((AppCompatActivity) activity);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        switch (viewType) {
            case RSS_TYPE:
                return new RecyclerViewRssItem(LayoutInflater.from(parent.getContext()).inflate(layoutType, parent, false));
            case AD_TYPE:
                if (!sharedPrefHelper.getNativeAddUnitID().startsWith("ca-"))
                    return new RecyclerViewRssFacebookAdItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false), activity, type, sharedPrefHelper);
                else
                    return new RecyclerViewRssAdMobAdItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false), activity, type, sharedPrefHelper);
            case RSS_FEATURED_HEADER_TYPE:
                final View itemFeaturedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_list_item_square_featured, parent, false);

                return new RecyclerViewRssFeaturedHeaderItem(itemFeaturedView);
            case RSS_SLIDER_HEADER_TYPE:
                final View itemSliderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_header_slider, parent, false);
                return new RecyclerViewRssSliderHeaderItem(itemSliderView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == RSS_TYPE) {
            RssModel rssModel = (RssModel) dataList.get(position);
            RecyclerViewRssItem recyclerViewRssItem = (RecyclerViewRssItem) holder;
            recyclerViewRssItem.bind(rssModel);
        } else if(holder.getItemViewType() == RSS_FEATURED_HEADER_TYPE || holder.getItemViewType() == RSS_SLIDER_HEADER_TYPE ) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position) instanceof RssModel)
            return RSS_TYPE;
        else if (dataList.get(position) instanceof AdMobModel)
            return AD_TYPE;
        else if (dataList.get(position) instanceof RssFeaturedHeaderModel)
            return RSS_FEATURED_HEADER_TYPE;
        else
            return RSS_SLIDER_HEADER_TYPE;
    }

    public List<Object> getAllData() {
        return dataList;
    }

    public void add(Object rssModel) {
        dataList.add(rssModel);
    }

    private class RecyclerViewRssItem extends RecyclerView.ViewHolder {

        TextView rssTitle;
        TextView rssDescription;
        TextView rssDate;
        ImageView rssAvatar;
        ViewGroup mainLayout;
        int position;
        ViewGroup parent;

        RecyclerViewRssItem(View v) {
            super(v);
            rssTitle = v.findViewById(R.id.rss_list_title); // title
            rssAvatar = v.findViewById(R.id.rss_list_image); // image
            rssDate = v.findViewById(R.id.rss_list_date); //
            rssDescription = v.findViewById(R.id.rss_list_description); //
            mainLayout = v.findViewById(R.id.main_layout);
            if (type == 5) {
                rssTitle.setTextSize(12);
                rssTitle.setMaxLines(1);
                final float scale = activity.getResources().getDisplayMetrics().density;
                int pixels = (int) (activity.getResources().getInteger(R.integer.rss_layout_5) * scale + 0.5f);
                mainLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pixels));
            }
            if (staggered) {
                StaggeredGridLayoutManager.LayoutParams relativeParams = (StaggeredGridLayoutManager.LayoutParams) mainLayout.getLayoutParams();
                relativeParams.setMargins(2, 2, 2, 2);  // left, top, right, bottom
                mainLayout.setLayoutParams(relativeParams);
            }
            if (type == 10 || type == 8)
                rssAvatar.setVisibility(View.GONE);
        }

        void bind(final RssModel rssModel) {

            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (screenId != null) {
                        Intent newIntent = new Intent(activity, aveRssContentViewPager.class);
                        int index = dataList.indexOf(rssModel);
                        if (dataList.get(0) instanceof RssSliderHeaderModel)
                            index = index + 4;
                        newIntent.putExtra(KEY_RSS_POSITION, index);
                        newIntent.putExtra(KEY_SCREEN_ID, screenId);
                        newIntent.putExtra(KEY_RSS_TITLE, rssModel.getTitle());
                        newIntent.putExtra(KEY_RSS_LAYOUT_TYPE, type);
                        if (type != 10 && type != 8
                                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                                && rssModel.getImage() != null
                                && !rssModel.getImage().equalsIgnoreCase("")) {
                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation(activity, rssAvatar, "rssImage");
                            interstitialAdsUtil.checkInterstitialAds(newIntent, options.toBundle());
                        } else
                            interstitialAdsUtil.checkInterstitialAds(newIntent);
                    }
                }
            });


            if (rssDescription.getVisibility() == View.VISIBLE)
                rssDescription.setText(Html.fromHtml(rssModel.getDescription().replaceAll("\\<[^>]*>", "").trim()));
            if (rssDate.getVisibility() == View.VISIBLE) {
                if (rssModel.getPubDate() != null) {
                    try {
                        rssDate.setText(dateFormat.format(rssModel.getPubDate()));
                    } catch (Exception e) {
                        // Date format exception (probably broken date data received)
                        rssDate.setText("-");
                    }
                } else
                    rssDate.setText("-");
            }
            rssTitle.setText(Html.fromHtml(rssModel.getTitle()));
            if (rssModel.getImage() != null && !rssModel.getImage().equalsIgnoreCase("")) {
                Glide.with(activity)
                        .load(rssModel.getImage())
                        .apply(new RequestOptions().placeholder(R.drawable.no_image))
                        .addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                rssAvatar.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.no_image));
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(rssAvatar);

            } else
                Glide.with(activity)
                        .load(R.drawable.no_image)
                        .into(rssAvatar);
            if (type != 10) {
                rssTitle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        rssTitle.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        float lineHeight = rssTitle.getLineHeight();
                        int maxLines = (int) (rssTitle.getHeight() / lineHeight);

                        if (rssTitle.getLineCount() != maxLines) {
                            rssTitle.setLines(maxLines);
                        }

                    }
                });
            }
        }

    }

    private class RecyclerViewRssFeaturedHeaderItem extends RecyclerView.ViewHolder {
        TextView rssTitle;
        TextView rssDate;
        ImageView rssAvatar;
        RelativeLayout mainLayout;
        RssModel rssModelHeader;

        RecyclerViewRssFeaturedHeaderItem(View v) {
            super(v);
            rssModelHeader = ((RssFeaturedHeaderModel) dataList.get(0)).getFeaturedHeader();
            rssTitle = v.findViewById(R.id.rss_list_title);
            mainLayout = v.findViewById(R.id.main_layout);
            rssDate = v.findViewById(R.id.rss_list_date); // date
            rssAvatar = v.findViewById(R.id.rss_list_image);
            bind(((RssFeaturedHeaderModel) dataList.get(0)));
        }

        void bind(final RssFeaturedHeaderModel rssFeaturedHeaderModel) {
            final RssModel rssModel = rssFeaturedHeaderModel.getFeaturedHeader();
            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (screenId !=null) {
                        Intent newIntent = new Intent(activity, aveRssContentViewPager.class);
                        newIntent.putExtra("position", 0);
                        newIntent.putExtra(KEY_SCREEN_ID, screenId);
                        newIntent.putExtra(KEY_RSS_LAYOUT_TYPE, layoutType);
                        if (rssModel.getTitle() != null)
                            newIntent.putExtra(KEY_RSS_TITLE, Html.fromHtml(rssModel.getTitle()).toString());
                        else
                            newIntent.putExtra(KEY_RSS_TITLE, "");
                        if (layoutType != 10 && layoutType != 8
                                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                                && rssModel.getImage() != null
                                && !rssModel.getImage().equalsIgnoreCase("")) {
                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation(activity, rssAvatar, "rssImage");
                            interstitialAdsUtil.checkInterstitialAds(newIntent, options.toBundle());
                        } else
                            interstitialAdsUtil.checkInterstitialAds(newIntent);
                    }
                }
            });
            if (rssDate.getVisibility() == View.VISIBLE) {
                if (rssModel.getPubDate() != null) {
                    try {
                        rssDate.setText(dateFormat.format(rssModel.getPubDate()));
                    } catch (Exception e) {
                        // Date format exception (probably broken date data received)
                        rssDate.setText("-");
                    }
                } else
                    rssDate.setText("-");
            }
            rssTitle.setText(Html.fromHtml(rssModel.getTitle()));
            if (rssModel.getImage() != null && !rssModel.getImage().equalsIgnoreCase("")) {
                Glide.with(activity)
                        .load(rssModel.getImage())
                        .apply(new RequestOptions().placeholder(R.drawable.no_image))
                        .addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                rssAvatar.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.no_image));
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(rssAvatar);

            } else
                Glide.with(activity)
                        .load(R.drawable.no_image)
                        .into(rssAvatar);
            rssTitle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    rssTitle.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    float lineHeight = rssTitle.getLineHeight();
                    int maxLines = (int) (rssTitle.getHeight() / lineHeight);

                    if (rssTitle.getLineCount() != maxLines) {
                        rssTitle.setLines(maxLines);
                    }

                }
            });

        }

    }

    private class RecyclerViewRssSliderHeaderItem extends RecyclerView.ViewHolder {

        private ClickableViewPager viewPager;
        private LinearLayout pager_indicator;
        private ViewPagerAdapter mAdapter;
        private int dotsCount;
        private ImageView[] dots;

        RecyclerViewRssSliderHeaderItem(View v) {
            super(v);
            viewPager = v.findViewById(R.id.viewPager);
            pager_indicator = v.findViewById(R.id.viewPagerCountDots);
            bind((RssSliderHeaderModel) dataList.get(0));
        }

        void bind(final RssSliderHeaderModel rssSliderHeaderModel) {
            mAdapter = new ViewPagerAdapter(activity, rssSliderHeaderModel.getDataList(), screenId);
            viewPager.setAdapter(mAdapter);
            viewPager.setOffscreenPageLimit(5);
            viewPager.setOnItemClickListener(new ClickableViewPager.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent newIntent = new Intent(activity, aveRssContentViewPager.class);
                    newIntent.putExtra(KEY_RSS_POSITION, position);
                    newIntent.putExtra(KEY_SCREEN_ID, screenId);
                    newIntent.putExtra(KEY_RSS_LAYOUT_TYPE, layoutType);
                    if (((RssModel) rssSliderHeaderModel.getDataList().get(position)).getTitle() != null)
                        newIntent.putExtra(KEY_RSS_TITLE, Html.fromHtml(((RssModel) rssSliderHeaderModel.getDataList().get(position)).getTitle()).toString());
                    else
                        newIntent.putExtra(KEY_RSS_TITLE, "");
                    if (layoutType != 10 && layoutType != 8
                            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                            && ((RssModel) rssSliderHeaderModel.getDataList().get(position)).getImage() != null
                            && !((RssModel) rssSliderHeaderModel.getDataList().get(position)).getImage().equalsIgnoreCase("")) {
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(activity, viewPager.findViewWithTag("viewpager" + position), "rssImage");
                        interstitialAdsUtil.checkInterstitialAds(newIntent, options.toBundle());
                    } else
                        interstitialAdsUtil.checkInterstitialAds(newIntent);
                }
            });

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    for (int i = 0; i < dotsCount; i++) {
                        Drawable dot = ContextCompat.getDrawable(activity, R.drawable.nonselecteditem_dot);
                        dots[i].setImageDrawable(dot);
                    }


                    Drawable dot = ContextCompat.getDrawable(activity, R.drawable.selecteditem_dot);
                    dot.setColorFilter(new
                            PorterDuffColorFilter(sharedPrefHelper.getActionBarColor(), PorterDuff.Mode.MULTIPLY));
                    dots[position].setImageDrawable(dot);

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            dotsCount = mAdapter.getCount();
            dots = new ImageView[dotsCount];

            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(activity);
                dots[i].setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.nonselecteditem_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(4, 0, 4, 0);

                pager_indicator.addView(dots[i], params);
            }

            Drawable dot = ContextCompat.getDrawable(activity, R.drawable.selecteditem_dot);
            dot.setColorFilter(new
                    PorterDuffColorFilter(sharedPrefHelper.getActionBarColor(), PorterDuff.Mode.MULTIPLY));
            dots[0].setImageDrawable(dot);
        }
    }

    public void findAndOpenContent(String rssPushTitle) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i) instanceof RssFeaturedHeaderModel) {
                if (checkSimilarity(rssPushTitle, ((RssFeaturedHeaderModel) dataList.get(i)).getFeaturedHeader().getTitle())) {
                    startRssContentActivity(((RssFeaturedHeaderModel) dataList.get(i)).getFeaturedHeader(), 0);
                    break;
                }
            } else if (dataList.get(i) instanceof RssSliderHeaderModel) {
                boolean isFounded = false;
                for (int j = 0; j < ((RssSliderHeaderModel) dataList.get(i)).getDataList().size(); j++) {
                    if (checkSimilarity(rssPushTitle, ((RssModel) ((RssSliderHeaderModel) dataList.get(i)).getDataList().get(j)).getTitle())) {
                        startRssContentActivity(((RssModel) ((RssSliderHeaderModel) dataList.get(i)).getDataList().get(j)), j);
                        isFounded = true;
                        break;
                    }
                }
                if (isFounded)
                    break;
            } else if (dataList.get(i) instanceof RssModel) {
                if (checkSimilarity(rssPushTitle, ((RssModel) dataList.get(i)).getTitle())) {
                    int index = dataList.indexOf((dataList.get(i)));
                    if (dataList.get(0) instanceof RssSliderHeaderModel)
                        index = index + 4;
                    startRssContentActivity(((RssModel) dataList.get(i)), index);
                    break;
                }
            }
        }
        rssPushTitle = null;
    }


    public void startRssContentActivity(RssModel rssModel, int index) {
        Intent newIntent = new Intent(activity, aveRssContentViewPager.class);
        newIntent.putExtra(KEY_RSS_POSITION, index);
        newIntent.putExtra(KEY_SCREEN_ID, screenId);
        newIntent.putExtra(KEY_RSS_TITLE, rssModel.getTitle());
        newIntent.putExtra(KEY_RSS_LAYOUT_TYPE, type);
        interstitialAdsUtil.checkInterstitialAds(newIntent);
    }


    private boolean checkSimilarity(String s1, String s2) {
        return similarity(s1, s2) > 0.75;
    }

    /**
     * Calculates the similarity (a number within 0 and 1) between two strings.
     */
    private double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0; /* both strings are zero length */
        }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }


    public ArrayList<RssModel> getAllRssModels() {
        ArrayList<RssModel> rssModels = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i) instanceof RssFeaturedHeaderModel) {
                rssModels.add(((RssFeaturedHeaderModel) dataList.get(i)).getFeaturedHeader());
            } else if (dataList.get(i) instanceof RssSliderHeaderModel) {
                for (int j = 0; j < ((RssSliderHeaderModel) dataList.get(i)).getDataList().size(); j++) {
                    rssModels.add((RssModel) ((RssSliderHeaderModel) dataList.get(i)).getDataList().get(j));
                }
            } else if (dataList.get(i) instanceof RssModel) {
                rssModels.add((RssModel) dataList.get(i));
            }
        }
        return rssModels;
    }

    public ArrayList<Object> getAllModels() {
        return dataList;
    }
}