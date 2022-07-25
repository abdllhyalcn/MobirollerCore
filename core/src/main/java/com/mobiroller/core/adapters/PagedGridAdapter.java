package com.mobiroller.core.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobiroller.core.activities.ActivityHandler;
import com.mobiroller.core.helpers.FontSizeHelper;
import com.mobiroller.core.helpers.JSONParser;
import com.mobiroller.core.helpers.LegacyProgressViewHelper;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.helpers.ScreenHelper;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.models.Item;
import com.mobiroller.core.models.NavigationItemModel;
import com.mobiroller.core.models.Page;
import com.mobiroller.core.models.PagedGridModel;
import com.mobiroller.core.R;
import com.mobiroller.core.util.ImageManager;
import com.mobiroller.core.views.PagedDragDropGrid;
import com.mobiroller.core.views.PageIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PagedGridAdapter extends PagedDragDropGridAdapter {

    public LegacyProgressViewHelper legacyProgressViewHelper;
    private Activity context;
    private PagedDragDropGrid gridView;
    public ScreenHelper screenHelper;
    public JSONParser jParserNew;
    public LocalizationHelper localizationHelper;
    private String backgroundImgUrl = null;
    private PageIndicator mPager;
    public int counter;
    private PagedGridModel pagedGridModel;
    private List<Page> pages = new ArrayList<>();
    private int fontSizeOrder;

    public PagedGridAdapter(Activity context, PagedDragDropGrid gridView,
                            PagedGridModel pagedGridModel, PageIndicator pager,
                            String backgroundImage,
                            ScreenHelper screenHelper, LegacyProgressViewHelper legacyProgressViewHelper,
                            JSONParser jsonParser) {
        super();
        this.context = context;
        fontSizeOrder = new FontSizeHelper(context).getFontOrder();
        this.gridView = gridView;
        this.mPager = pager;

        if(context.getResources().getInteger(R.integer.locale_mirror_flip) == 180) {
            gridView.setRotation(180);
            gridView.setRotationX(180);
            mPager.setRotationY(180);
        }

        this.pagedGridModel = pagedGridModel;
        this.backgroundImgUrl = backgroundImage;
        this.screenHelper = screenHelper;
        this.localizationHelper = UtilManager.localizationHelper();
        this.legacyProgressViewHelper = legacyProgressViewHelper;
        this.jParserNew = jsonParser;
        legacyProgressViewHelper.dismiss();
        String tabImgURL, title, imgName, imgPath = null;
        int counter = 0;
//        Drawable icon = null;
        int totalElements = pagedGridModel.getNavItems().size();
        List<Item> items = new ArrayList<Item>();

        for (int i = 0; i < pagedGridModel.getNavItems().size(); i++) {
            NavigationItemModel row = pagedGridModel.getNavItems().get(i);
            title = localizationHelper.getLocalizedTitle(row.getTitle());
            if (row.getIconImage() != null) {
                tabImgURL = row.getIconImage().getImageURL();
            } else {
                tabImgURL = "null";
            }

            Item newItem = new Item(i + 1, title, tabImgURL);
            items.add(newItem);
        }
        while (totalElements > 0) {
            Page page = new Page();
            List<Item> menuItems = new ArrayList<Item>();
            if (pagedGridModel.getElementPerPage() < totalElements) {
                for (int i = 0; i < pagedGridModel.getElementPerPage(); i++) {
                    menuItems.add(items.get(counter));
                    counter++;
                }
            } else {
                for (int i = 0; i < totalElements; i++) {
                    menuItems.add(items.get(counter));
                    counter++;
                }
            }
            page.setItems(menuItems);
            pages.add(page);
            totalElements -= pagedGridModel.getElementPerPage();

        }

        mPager.setDotCount(pageCount());

    }

    @Override
    public int pageCount() {
        return pages.size();
    }

    private List<Item> itemsInPage(int page) {
        if (pages.size() > page) {
            return pages.get(page).getItems();
        }
        return Collections.emptyList();
    }

    @SuppressWarnings("deprecation")
    @Override
    public View view(final int page, final int index) {

        final RelativeLayout layout = new RelativeLayout(context);
        final ImageView icon = new ImageView(context);
        Item item = getItem(page, index);
        ImageManager.loadImageView(context,item.getImageUrl(),icon);
        icon.setPadding(0, Math.round(10 * pagedGridModel.getStParam() / 100), 0, 0);
        RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(pagedGridModel.getStParam(), Math.round((pagedGridModel.getStParam() * 2) / 3));
        lay.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layout.addView(icon, lay);
        TextView label = new TextView(context);
        label.setTag("text");
        label.setText(item.getTitle());
        label.setTextColor(pagedGridModel.getColor());

        if (fontSizeOrder == 0) {
            label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        } else if (fontSizeOrder == 1) {
            label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        } else if (fontSizeOrder == 2) {
            label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        } else if (fontSizeOrder == 3) {
            label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        }

        label.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL | Gravity.CENTER);
        label.setPadding(0, 0, 0, (Math.round(pagedGridModel.getStParam() / 10)));
        label.setMaxWidth(pagedGridModel.getStParam() - 30);
        label.setEllipsize(TruncateAt.END);
        label.setSingleLine();

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        p.addRule(RelativeLayout.CENTER_HORIZONTAL);

        layout.setMinimumHeight(pagedGridModel.getStParam());
        layout.setMinimumWidth(pagedGridModel.getStParam());
        layout.setLayoutParams(new RelativeLayout.LayoutParams(pagedGridModel.getStParam(), pagedGridModel.getStParam()));
        if (backgroundImgUrl != null) {
            ImageManager.loadBackgroundImage(backgroundImgUrl,layout);
        }else
            layout.setBackgroundColor(Color.TRANSPARENT);


        if(context.getResources().getInteger(R.integer.locale_mirror_flip) == 180)
            layout.setRotationY(180);

        // only set selector on every other page for demo purposes
        // if you do not wish to use the selector functionality, simply disregard this code
        if (page % 2 == 0) {
            setViewBackground(layout);
            layout.setClickable(true);
            layout.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return gridView.onLongClick(v);
                }
            });
        }

        layout.addView(label, p);
        ImageView overlayImg = new ImageView(context);
        overlayImg.setLayoutParams(new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        overlayImg.setBackgroundResource(R.drawable.list_selector);
        overlayImg.setClickable(true);
        //overlayImg.setFocusable(true);
        overlayImg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Let's get the page numbered index value
                counter = (rowCount() * columnCount()) * page;
                if (Integer.valueOf(pagedGridModel.getNavItems().get(index + counter).getAccountScreenID()) != -1) {
                    context.startActivity(new Intent(context,ActivityHandler.class).putExtra(ActivityHandler.INTENT_EXTRA_NAVIGATION_MODEL,pagedGridModel.getNavItems().get(index + counter)));
                } else {
                    v.setEnabled(false);
                }
            }
        });
        layout.addView(overlayImg);

        return layout;
    }

    private void setViewBackground(final RelativeLayout layout) {
        if (backgroundImgUrl != null) {
            ImageManager.loadBackgroundImage(backgroundImgUrl,layout);
        }

    }

    private Item getItem(int page, int index) {
        List<Item> items = itemsInPage(page);
        return items.get(index);
    }

    @Override
    public int rowCount() {
        return pagedGridModel.getNumOfRows();
    }

    @Override
    public int columnCount() {
        return pagedGridModel.getNumOfColumns();
    }

    @Override
    public int itemCountInPage(int page) {
        return itemsInPage(page).size();
    }

    private Page getPage(int pageIndex) {
        mPager.setActiveDot(pageIndex);
        return pages.get(pageIndex);
    }

    @Override
    public void swapItems(int pageIndex, int itemIndexA, int itemIndexB) {
        mPager.setActiveDot(pageIndex);
        getPage(pageIndex).swapItems(itemIndexA, itemIndexB);
    }

    @Override
    public void moveItemToPreviousPage(int pageIndex, int itemIndex) {
        if (pageIndex > 0) {
            int leftPageIndex = pageIndex - 1;
            mPager.setActiveDot(leftPageIndex);
        }
    }

    @Override
    public void moveItemToNextPage(int pageIndex, int itemIndex) {
        int rightPageIndex = pageIndex + 1;
        if (rightPageIndex < pageCount()) {
            mPager.setActiveDot(rightPageIndex);
        }
    }

    @Override
    public void deleteItem(int pageIndex, int itemIndex) {
        getPage(pageIndex).deleteItem(itemIndex);
    }

    @Override
    public int deleteDropZoneLocation() {
        return BOTTOM;
    }

    @Override
    public boolean showRemoveDropZone() {
        return false;
    }

}