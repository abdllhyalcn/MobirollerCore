package com.mobiroller.core.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import com.mobiroller.core.SharedApplication;
import com.rometools.rome.feed.synd.SyndEntry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ealtaca on 19.12.2016.
 * <p>
 * Parse util for RSS feed to get image,
 * description, ...
 */

public class RssUtil {

    /**
     * Every RSS standart can differ to define image parameter or can use it inside another object
     * So we search image element according to basic rss standarts with related order below
     *
     * @param syndEntry rome object to get parsed data
     * @return
     */
    public static String rssGetImageUrl(SyndEntry syndEntry) {

        String picUrl = ""; // if no image found, return empty string to display error/placeholder image

        // Search image inside enclosure object
        if (syndEntry.getEnclosures() != null && syndEntry.getEnclosures().size() != 0)
            if (syndEntry.getEnclosures().get(0).getType().startsWith("image") || syndEntry.getEnclosures().get(0).getType().startsWith("img")) {
                picUrl = syndEntry.getEnclosures().get(0).getUrl();
            }

        // Search image inside foreign markup object
        if (picUrl.equals("") && syndEntry.getForeignMarkup() != null && syndEntry.getForeignMarkup().size() != 0) {
            for (int i = 0; i < syndEntry.getForeignMarkup().size(); i++)
                if (syndEntry.getForeignMarkup().get(i).getAttributes() != null && syndEntry.getForeignMarkup().get(i).getAttributes().size() != 0)
                    for (int j = 0; j < syndEntry.getForeignMarkup().get(i).getAttributes().size(); j++)
                        if (syndEntry.getForeignMarkup().get(i).getAttributes().get(j).getName().equalsIgnoreCase("url"))
                            picUrl = syndEntry.getForeignMarkup().get(i).getAttributes().get(j).getValue();
        }
        // If we can't find any image above, search image inside content object
        if (picUrl.equals("") && syndEntry.getContents() != null && syndEntry.getContents().size() != 0) {
            Pattern pattern1 = Pattern.compile("src=\"(.*?)\"");
            Pattern pattern2 = Pattern.compile("src='(.*?)'");
            Matcher matcher1 = pattern1.matcher(syndEntry.getContents().get(0).getValue());
            Matcher matcher2 = pattern2.matcher(syndEntry.getContents().get(0).getValue());
            if (matcher1.find() && matcher1.groupCount() == 1) {
                picUrl = matcher1.group(1);
                syndEntry.getContents().get(0).setValue(syndEntry.getContents().get(0).getValue().replaceFirst("<img.+?>", ""));
            } else if (matcher2.find() && matcher2.groupCount() == 1) {
                picUrl = matcher2.group(1);
                syndEntry.getContents().get(0).setValue(syndEntry.getContents().get(0).getValue().replaceFirst("<img.+?>", ""));
            }
        }
        // Search image inside description object
        if (picUrl.equals("") && syndEntry.getDescription() != null && syndEntry.getDescription().getValue() != null) {
            Pattern pattern1 = Pattern.compile("src=\"(.*?)\"");
            Pattern pattern2 = Pattern.compile("src='(.*?)'");
            Matcher matcher1 = pattern1.matcher(syndEntry.getDescription().getValue());
            Matcher matcher2 = pattern2.matcher(syndEntry.getDescription().getValue());
            if (matcher1.find() && matcher1.groupCount() == 1) {
                picUrl = matcher1.group(1);
                syndEntry.getDescription().setValue(syndEntry.getDescription().getValue().replaceFirst("<img.+?>", ""));
            } else if (matcher2.find() && matcher2.groupCount() == 1) {
                picUrl = matcher2.group(1);
                syndEntry.getDescription().setValue(syndEntry.getDescription().getValue().replaceFirst("<img.+?>", ""));
            }
        }




        //picUrl = picUrl.split("\\?")[0];
        if (picUrl.startsWith("http://") || picUrl.startsWith("https://"))
            return picUrl;
        /*else if(picUrl.startsWith("https://"))
            return picUrl.replaceFirst("https", "http");*/
        else if (picUrl.startsWith("file:"))
            return picUrl.replaceFirst("file:", "http:");
        else if (picUrl.startsWith("//"))
            return picUrl.replaceFirst("//", "http://");
        else
            return picUrl;
    }

    /**
     * Get author of related rss content
     *
     * @return
     */
    public static String rssGetAuthor() {
        return "";
    }

    /**
     * If RSS object has content object, than return content
     * else searcj for description object if none appears return empty string
     *
     * @param syndEntry
     * @return
     */
    public static String rssGetDescription(SyndEntry syndEntry) {
        if (syndEntry.getContents() != null && syndEntry.getContents().size() != 0)
            return syndEntry.getContents().get(0).getValue();
        else if (syndEntry.getDescription() != null && syndEntry.getDescription().getValue() != null)
            return getDescription(syndEntry.getDescription().getValue());

        return "";
    }

    /**
     * Remove all image tags to display plain text only
     *
     * @param desc
     * @return
     */
    public static String getDescription(String desc) {
        return desc.replaceFirst("<img.+?>", "");
    }

    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics()));
    }
    public static int convertDpToPixel(float dp) {
        return convertDpToPixel(dp, SharedApplication.context);
    }
}
