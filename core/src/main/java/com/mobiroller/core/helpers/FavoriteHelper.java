package com.mobiroller.core.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobiroller.core.models.FavoriteModel;
import com.mobiroller.core.coreui.models.GalleryModel;
import com.mobiroller.core.models.RssModel;
import com.mobiroller.core.coreui.models.ScreenModel;
import com.mobiroller.core.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.mobiroller.core.helpers.StringSimilarityHelper.editDistance;

/**
 * Created by ealtaca on 6/30/18.
 */

public class FavoriteHelper {

    private Context mContext;
    private SharedPreferences mSharedPref;
    private final String FAVORITE_LIST_PREF = "FAVORITE_LIST_PREF";
    private final String FAVORITE_LIST = "FAVORITE_LIST";

    public FavoriteHelper(Context context) {
        mContext = context;
        mSharedPref = mContext.getSharedPreferences(FAVORITE_LIST_PREF, Context.MODE_PRIVATE);
    }

    public void addScreenToList(ScreenModel screenModel,String screenType,String subScreenType ,String screenId) {
        ArrayList<FavoriteModel> list = getDb();
        if (list == null)
            list = new ArrayList<>();
        list.add(0, new FavoriteModel(screenModel,screenType,subScreenType,screenId));
        updateDb(list);
    }

    public void addRssContentToList(RssModel rssModel) {
        ArrayList<FavoriteModel> list = getDb();
        list.add(0, new FavoriteModel(rssModel));
        updateDb(list);
    }

    public void addImageContentToList(GalleryModel galleryModel) {
        ArrayList<FavoriteModel> list = getDb();
        list.add(0, new FavoriteModel(galleryModel));
        updateDb(list);
    }

    public void removeRssContent(RssModel rssModel) {
        ArrayList<FavoriteModel> list = getDb();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getContentType() == FavoriteModel.ContentTypes.TYPE_RSS &&
                    checkSimilarity(rssModel.getTitle(), list.get(i).getRssModel().getTitle())) {
                list.remove(i);
                break;
            }
        }
        updateDb(list);
    }
    public void removeGalleryContent(GalleryModel galleryModel) {
        ArrayList<FavoriteModel> list = getDb();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getContentType() == FavoriteModel.ContentTypes.TYPE_GALLERY &&
                    checkSimilarityURI(galleryModel.getURL(), list.get(i).getGalleryModel().getURL())) {
                list.remove(i);
                break;
            }
        }
        updateDb(list);
    }

    public void removeScreenContent(String screenId) {
        ArrayList<FavoriteModel> list = getDb();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).IsScreen() && list.get(i).getScreenId().equalsIgnoreCase(screenId)) {
                list.remove(i);
                break;
            }
        }
        updateDb(list);
    }

    public boolean isRssContentAddedToList(RssModel rssModel) {
        ArrayList<FavoriteModel> list = getDb();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getContentType() == FavoriteModel.ContentTypes.TYPE_RSS &&
                    checkSimilarity(rssModel.getTitle(), list.get(i).getRssModel().getTitle())) {
                return true;
            }
        }
        return false;
    }
    public boolean isImageContentAddedToList(GalleryModel galleryModel) {
        ArrayList<FavoriteModel> list = getDb();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getContentType() == FavoriteModel.ContentTypes.TYPE_GALLERY &&
                    checkSimilarityURI(galleryModel.getURL(), list.get(i).getGalleryModel().getURL())) {
                return true;
            }
        }
        return false;
    }

    public boolean isScreenAddedToList(String screenId) {
        ArrayList<FavoriteModel> list = getDb();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).IsScreen() && list.get(i).getScreenId().equalsIgnoreCase(screenId)) {
                return true;
            }
        }
        return false;
    }

    private void updateDb(ArrayList<FavoriteModel> listData) {
        String serializedData = new Gson().toJson(listData, getTypeToken());
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(FAVORITE_LIST, serializedData);
        editor.apply();
    }

    public void removeDb() {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.remove(FAVORITE_LIST);
        editor.apply();
    }

    public ArrayList<FavoriteModel> getDb() {
        String serializedDataFromPreference = mSharedPref.getString(FAVORITE_LIST, null);
        if (serializedDataFromPreference == null)
            return new ArrayList<>();
        try {
            ArrayList<FavoriteModel> list = new Gson().fromJson(serializedDataFromPreference, getTypeToken());
            if (list == null)
                return new ArrayList<>();
            return list;
        } catch (Exception e) {
            mSharedPref.edit().remove(FAVORITE_LIST).apply();
            return new ArrayList<>();
        }
    }

    private Type getTypeToken() {
        return new TypeToken<ArrayList<FavoriteModel>>() {
        }.getType();
    }


    private boolean checkSimilarity(String s1, String s2) {
        return similarity(s1, s2) > 0.75;
    }

    private boolean checkSimilarityURI(String s1, String s2) {
        String idStr = s1.substring(s1.lastIndexOf('/') + 1);
        String idStrSecond = s2.substring(s2.lastIndexOf('/') + 1);
        return idStr.equalsIgnoreCase(idStrSecond);
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

    public boolean isScreenAvailable(String screenType)
    {
       String[] array =  mContext.getResources().getStringArray(R.array.favorite_available_modules);
        for (String moduleName :
                array) {
            if(moduleName.equalsIgnoreCase(screenType))
                return true;

        }
        return false;
    }

}
