package com.mobiroller.core.models;

import com.mobiroller.core.coreui.models.GalleryModel;
import com.mobiroller.core.coreui.models.ScreenModel;

/**
 * Created by ealtaca on 6/30/18.
 */

public class FavoriteModel {

    // if false it should be content(rss,image etc..)
    private boolean mIsScreen;
    private int mContentType;
    private int mScreenId;
    private String mScreenIdString;
    private ScreenModel mScreenModel;
    private String mScreenType;
    private String mSubScreenType;
    private String mScreenTitle;
    private String mScreenImage;
    private RssModel mRssModel;
    private GalleryModel mGalleryModel;

    public FavoriteModel(ScreenModel mScreenModel, String screenType,String subScreenType, String screenId) {
        this.mScreenIdString = screenId;
        this.mScreenModel = mScreenModel;
        this.mScreenType = screenType;
        this.mSubScreenType = subScreenType;
        this.mScreenTitle = mScreenModel.getTitle();
        if (mScreenModel.getMainImageName() != null)
            this.mScreenImage = mScreenModel.getMainImageName().getImageURL();
        mIsScreen = true;
    }

    public String getScreenId() {
        if(mScreenIdString!=null)
            return mScreenIdString;
        return String.valueOf(mScreenId);
    }

    public FavoriteModel(RssModel mRssModel) {
        this.mRssModel = mRssModel;
        mContentType = ContentTypes.TYPE_RSS;
    }

    public FavoriteModel(GalleryModel galleryModel) {
        this.mGalleryModel = galleryModel;
        mContentType = ContentTypes.TYPE_GALLERY;
    }

    public String getmSubScreenType() {
        return mSubScreenType;
    }

    public void setmSubScreenType(String mSubScreenType) {
        this.mSubScreenType = mSubScreenType;
    }

    public boolean IsScreen() {
        return mIsScreen;
    }

    public int getContentType() {
        return mContentType;
    }

    public ScreenModel getScreenModel() {
        return mScreenModel;
    }

    public RssModel getRssModel() {
        return mRssModel;
    }

    public GalleryModel getGalleryModel() {
        return mGalleryModel;
    }

    public String getScreenType() {
        return mScreenType;
    }

    public String getScreenTitle() {
        return mScreenTitle;
    }

    public String getScreenImage() {
        return mScreenImage;
    }

    public final class ContentTypes {
        public static final int TYPE_RSS = 1;
        public static final int TYPE_GALLERY = 2;
    }
}
