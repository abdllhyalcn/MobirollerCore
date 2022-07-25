package com.mobiroller.core.constants;

/**
 * Created by ealtaca on 11.05.2017.
 */

public class YoutubeConstants {

    // Youtube service api key
    public static String YOUTUBE_KEY;


    // Youtube service api key
    public static String YOUTUBE_PRO_KEY;

    //Youtube service api url
    public final static String YOUTUBE_API_URL ="https://www.googleapis.com/youtube/v3/";


    public static final String YOUTUBE_SCOPE = "https://www.googleapis.com/auth/youtube.readonly";
    public static final String YOUTUBE_SCOPE_SSL = "https://www.googleapis.com/auth/youtube.force-ssl";

    // Search & detail static parameters
    public interface YoutubeRequestParams{
        String req_search_parts = "snippet";
        String req_search_order = "date";
        String req_search_type = "video";
        String req_search_max_results = "20";
        String req_detail_parts = "snippet,contentDetails";
    }

    public static final String INTENT_EXTRA_CHANNEL_ID = "channelId";

}
