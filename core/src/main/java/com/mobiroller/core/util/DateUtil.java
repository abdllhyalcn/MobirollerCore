package com.mobiroller.core.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static boolean dateControlString(String localDate, String liveDate) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            if (localDate != null && liveDate != null) {
                Date dateLocal = sdf.parse(localDate);
                Date dateLive = sdf.parse(liveDate);
                return dateLocal.compareTo(dateLive) == 0;
            } else {
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
