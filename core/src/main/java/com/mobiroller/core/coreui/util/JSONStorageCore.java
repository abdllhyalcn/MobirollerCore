package com.mobiroller.core.coreui.util;

import androidx.annotation.Nullable;

import com.mobiroller.core.coreui.models.ScreenModel;

import io.paperdb.Paper;

public class JSONStorageCore {

    private final static String TAG = "JSONStorage";
    private final static String PAPER_SCREEN_BOOK_TAG = "screens";

    @Nullable
    public static ScreenModel getScreenModel(String screenId) {
        if(screenId == null)
            return null;
        if (Paper.book(PAPER_SCREEN_BOOK_TAG).contains(screenId)) {
            try {
                return Paper.book(PAPER_SCREEN_BOOK_TAG).read(screenId);
            } catch (Exception e) {
                Paper.book(PAPER_SCREEN_BOOK_TAG).delete(screenId);
            }
        }

        return null;
    }

    public static boolean containsScreen(String screenId) {
        if(screenId==null)
            return false;
        try {
            Paper.book(PAPER_SCREEN_BOOK_TAG).read(screenId);
        } catch (Exception e) {
            Paper.book(PAPER_SCREEN_BOOK_TAG).delete(screenId);
        }
        return Paper.book(PAPER_SCREEN_BOOK_TAG).contains(screenId);
    }

    public static void putScreenModel(String screenId, ScreenModel screenModel) {
        if(screenId==null)
            return ;
        putObject(PAPER_SCREEN_BOOK_TAG,screenId,screenModel);
    }

    public static void putObject(String book,String key,Object object) {
        if(object!=null)
            Paper.book(book).write(key, object);
    }

    public static Object getObject(String book,String key,Object object) {
        if(book != null && key != null) {
            try {
                return Paper.book(book).read(key, object);
            } catch (Exception e) {
                return object;
            }
        } else {
            return object;
        }
    }


}
