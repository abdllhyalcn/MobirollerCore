package com.mobiroller.core.coreui.models;


import androidx.fragment.app.Fragment;

import com.mobiroller.core.coreui.helpers.LocalizationHelper;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ealtaca on 07.03.2017.
 */

public class ScreenModel implements Serializable {

    private transient Fragment fragment;
    private String title;
    private String updateDate;
    private ImageModel backgroundImageName;
    private String URL;

    //StandartView start
    private ImageModel mainImageName;
    private int mainImageHeight;
    private ColorModel contentTextColor;
    private float contentFontSize;
    private String contentFontName;
    private int contentTextHeight;
    private String localizedURL;
    private ColorModel contentTextBackColor;
    //StandartView end

    //StandartView , RssView ,YoutubeView,Mp3View shares
    private ColorModel tableTextColor;
    private int tableFontSize;
    private int tableRowHeight;
    private ImageModel tableCellBackground;
    private ArrayList<TableItemsModel> tableItems;
    //End
    private ArrayList<String> requiredFields;
    private String contentText;//ShareView , StandartView,RadioView

    //HtmlView start
    private String contentHtml;
    //HtmlView end

    //ShareView Start
    private String appStoreLink;
    private String googlePlayLink;
    //ShareView End

    //RssView, GalleryView, IptvView shares
    private String type;
    //end

    //RssView start
    private String rssLink;
    private String localizedRssLink;
    //RssView end

    //GalleryView start
    private boolean isDownloadable;
    private ArrayList<GalleryModel> images;
    //GalleryView end

    //WeatherView start
    private String city;
    private String screenType;
    //WeatherView end

    //TwitterView start
    private String userName;
    private String pageNo;
    private String tweetCount;
    private String apiDomain;
    private String accountName;
    //TwitterView end

    //CallNowView start
    private String phoneNumber;
    //CallNowView end

    //TvBroadCastLink start
    private String tvBroadcastLink;
    //TvBroadCastLink end

    //RadioView start
    private String radioBroadcastLink;
    //RadioView end

    //FormView start
    private String aveAccountModule;
    private int cellTitleWidth;
    private int cellControlWidth;
    private String submitAvailable;
    //FormView end

    //MapView start
    private String mapType;
    private String showUserLocation;
    private String lattitude;
    private String longitude;
    private int viewAreaInMeters;
    private ArrayList<FlagModel> flags;
    //MapView end

    //WebView start
    private String scriptPath;
    public Boolean isCacheEnabled;
    //WebView end

    //Mp3View start
    private ImageModel tableCellBackground1;
    private ImageModel tableCellBackground2;
    //Mp3View end

    //AboutView start
    private String contentHeader;
    private String about;
    private String description;
    private String twitter;
    private String facebook;
    private String linkedin;
    private String website;
    private String email;
    private String googleplus;
    //AboutView end

    //YoutubeView start
    private Boolean assignLinksManually;
    private String youtubeChannelId;
    public String youtubeChannelID;
    public String youtubeApiKey = "";
    public String youtubeProApiKey = "";
    //YoutubeView end

    //IptvView start
    public String streamLink;
    //IptvView end

    //Catalog
    private ImageModel catalogImageName;
    private String subTitle;
    private ArrayList<CategoryModel> tableCategories;

    private boolean isRssContent;

    private String tableFontName;

    private int layoutType;

    private Boolean showProgress;

    public Boolean showToolbar;

    //Short description for ECommerce Module
    public String sd;

    public String agencyCode;
    public String cdnURL;
    public String serviceBaseUrl;
    public String applicationToken;
    public String businessType;
    public String userPassword;

    public String logoImage;
    public String backgroundImage;
    public String appKey;
    public String androidChannelID;

    public String shopDomain;
    public String apiKey;
    public String companyName;
    public String address;
    // also email, phoneNumber


    public boolean isHideToolbar() {
        if(showToolbar==null)
            return false;
        return !showToolbar;
    }

    public void setHideToolbar(boolean showToolbar) {
        this.showToolbar = showToolbar;
    }

    public String getLocalizedURL() {
        return localizedURL;
    }

    public void setLocalizedURL(String localizedURL) {
        this.localizedURL = localizedURL;
    }

    public Boolean getShowProgress() {
        if (showProgress == null)
            return true;
        return showProgress;
    }

    public void setShowProgress(Boolean showProgress) {
        this.showProgress = showProgress;
    }

    /**
     * youtube layout type
     * @return layout type int
     */
    public int getLayoutType() {
        if (layoutType == 0 || layoutType > 6)
            return 1;
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }
    public ArrayList<String> getRequiredFields() {
        return requiredFields;
    }

    public void setRequiredFields(ArrayList<String> requiredFields) {
        this.requiredFields = requiredFields;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public Boolean isAssignLinksManually() {
        return assignLinksManually;
    }

    public void setAssignLinksManually(boolean assignLinksManually) {
        this.assignLinksManually = assignLinksManually;
    }

    public String getYoutubeChannelId() {
        return youtubeChannelId;
    }

    public void setYoutubeChannelId(String youtubeChannelId) {
        this.youtubeChannelId = youtubeChannelId;
    }

    public String getTableFontName() {
        return tableFontName;
    }

    public void setTableFontName(String tableFontName) {
        this.tableFontName = tableFontName;
    }

    public boolean isRssContent() {
        return isRssContent;
    }

    public void setRssContent(boolean rssContent) {
        isRssContent = rssContent;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public ImageModel getCatalogImageName() {
        return catalogImageName;
    }

    public void setCatalogImageName(ImageModel catalogImageName) {
        this.catalogImageName = catalogImageName;
    }

    public ArrayList<CategoryModel> getTableCategories() {
        return tableCategories;
    }

    public void setTableCategories(ArrayList<CategoryModel> tableCategories) {
        this.tableCategories = tableCategories;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public ImageModel getBackgroundImageName() {
        return backgroundImageName;
    }

    public void setBackgroundImageName(ImageModel backgroundImageName) {
        this.backgroundImageName = backgroundImageName;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public ImageModel getMainImageName() {
        return mainImageName;
    }

    public void setMainImageName(ImageModel mainImageName) {
        this.mainImageName = mainImageName;
    }

    public int getMainImageHeight() {
        return mainImageHeight;
    }

    public void setMainImageHeight(int mainImageHeight) {
        this.mainImageHeight = mainImageHeight;
    }

    public ColorModel getContentTextColor() {
        return contentTextColor;
    }

    public void setContentTextColor(ColorModel contentTextColor) {
        this.contentTextColor = contentTextColor;
    }

    public float getContentFontSize() {
        return contentFontSize;
    }

    public void setContentFontSize(float contentFontSize) {
        this.contentFontSize = contentFontSize;
    }

    public String getContentFontName() {
        return contentFontName;
    }

    public void setContentFontName(String contentFontName) {
        this.contentFontName = contentFontName;
    }

    public int getContentTextHeight() {
        return contentTextHeight;
    }

    public void setContentTextHeight(int contentTextHeight) {
        this.contentTextHeight = contentTextHeight;
    }

    public ColorModel getContentTextBackColor() {
        return contentTextBackColor;
    }

    public void setContentTextBackColor(ColorModel contentTextBackColor) {
        this.contentTextBackColor = contentTextBackColor;
    }

    public ColorModel getTableTextColor() {
        return tableTextColor;
    }

    public void setTableTextColor(ColorModel tableTextColor) {
        this.tableTextColor = tableTextColor;
    }

    public int getTableFontSize() {
        return tableFontSize;
    }

    public void setTableFontSize(int tableFontSize) {
        this.tableFontSize = tableFontSize;
    }

    public int getTableRowHeight() {
        return tableRowHeight;
    }

    public void setTableRowHeight(int tableRowHeight) {
        this.tableRowHeight = tableRowHeight;
    }

    public ImageModel getTableCellBackground() {
        return tableCellBackground;
    }

    public void setTableCellBackground(ImageModel tableCellBackground) {
        this.tableCellBackground = tableCellBackground;
    }

    public ArrayList<TableItemsModel> getTableItems() {
        return tableItems;
    }

    public void setTableItems(ArrayList<TableItemsModel> tableItems) {
        this.tableItems = tableItems;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getContentHtml() {
        return contentHtml;
    }

    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }

    public String getAppStoreLink() {
        return appStoreLink;
    }

    public void setAppStoreLink(String appStoreLink) {
        this.appStoreLink = appStoreLink;
    }

    public String getGooglePlayLink() {
        return googlePlayLink;
    }

    public void setGooglePlayLink(String googlePlayLink) {
        this.googlePlayLink = googlePlayLink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRssLink() {
        String rssUrl;
        if (getLocalizedRssLink() == null)
            rssUrl = rssLink;
        else {
            rssUrl = LocalizationHelper.getLocalizedTitle(localizedRssLink);
            if (rssUrl == null || rssUrl.isEmpty())
                rssUrl = rssLink;
        }
        return rssUrl;
    }

    public void setRssLink(String rssLink) {
        this.rssLink = rssLink;
    }

    public boolean isDownloadable() {
        return isDownloadable;
    }

    public void setDownloadable(boolean downloadable) {
        isDownloadable = downloadable;
    }

    public ArrayList<GalleryModel> getImages() {
        return images;
    }

    public void setImages(ArrayList<GalleryModel> images) {
        this.images = images;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getTweetCount() {
        return tweetCount;
    }

    public void setTweetCount(String tweetCount) {
        this.tweetCount = tweetCount;
    }

    public String getApiDomain() {
        return apiDomain;
    }

    public void setApiDomain(String apiDomain) {
        this.apiDomain = apiDomain;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTvBroadcastLink() {
        return tvBroadcastLink;
    }

    public void setTvBroadcastLink(String tvBroadcastLink) {
        this.tvBroadcastLink = tvBroadcastLink;
    }

    public String getRadioBroadcastLink() {
        return radioBroadcastLink;
    }

    public void setRadioBroadcastLink(String radioBroadcastLink) {
        this.radioBroadcastLink = radioBroadcastLink;
    }

    public String getAveAccountModule() {
        return aveAccountModule;
    }

    public void setAveAccountModule(String aveAccountModule) {
        this.aveAccountModule = aveAccountModule;
    }

    public int getCellTitleWidth() {
        return cellTitleWidth;
    }

    public void setCellTitleWidth(int cellTitleWidth) {
        this.cellTitleWidth = cellTitleWidth;
    }

    public int getCellControlWidth() {
        return cellControlWidth;
    }

    public void setCellControlWidth(int cellControlWidth) {
        this.cellControlWidth = cellControlWidth;
    }

    public String getSubmitAvailable() {
        return submitAvailable;
    }

    public void setSubmitAvailable(String submitAvailable) {
        this.submitAvailable = submitAvailable;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    public String getShowUserLocation() {
        return showUserLocation;
    }

    public void setShowUserLocation(String showUserLocation) {
        this.showUserLocation = showUserLocation;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getViewAreaInMeters() {
        return viewAreaInMeters;
    }

    public void setViewAreaInMeters(int viewAreaInMeters) {
        this.viewAreaInMeters = viewAreaInMeters;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<FlagModel> getFlags() {
        return flags;
    }

    public void setFlags(ArrayList<FlagModel> flags) {
        this.flags = flags;
    }

    public String getScriptPath() {
        return scriptPath;
    }

    public void setScriptPath(String scriptPath) {
        this.scriptPath = scriptPath;
    }

    public ImageModel getTableCellBackground1() {
        return tableCellBackground1;
    }

    public void setTableCellBackground1(ImageModel tableCellBackground1) {
        this.tableCellBackground1 = tableCellBackground1;
    }

    public ImageModel getTableCellBackground2() {
        return tableCellBackground2;
    }

    public void setTableCellBackground2(ImageModel tableCellBackground2) {
        this.tableCellBackground2 = tableCellBackground2;
    }

    public String getContentHeader() {
        return contentHeader;
    }

    public void setContentHeader(String contentHeader) {
        this.contentHeader = contentHeader;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGoogleplus() {
        return googleplus;
    }

    public void setGoogleplus(String googleplus) {
        this.googleplus = googleplus;
    }


    public void localizeCategoryModels(LocalizationHelper localizationHelper)
    {
        if(tableCategories!=null)
        {
            for (CategoryModel model :
                    tableCategories) {
                model.setCategoryTitle(localizationHelper.getLocalizedTitle(model.getCategoryTitle()));
                model.setCategorySubTitle(localizationHelper.getLocalizedTitle(model.getCategorySubTitle()));
            }
        }
    }

    public String getLocalizedRssLink() {
        return localizedRssLink;
    }

    public void setLocalizedRssLink(String localizedRssLink) {
        this.localizedRssLink = localizedRssLink;
    }
}
