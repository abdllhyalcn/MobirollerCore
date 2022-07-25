package com.mobiroller.core.serviceinterfaces;

import com.mobiroller.core.models.youtube.YoutubeDetailModel;
import com.mobiroller.core.models.youtube.YoutubeSearchModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ealtaca on 11.05.2017.
 */

public interface YoutubeServiceInterface {

    @GET("search?")
    Observable<YoutubeSearchModel> getYoutubeSearchData(@Query("part") String part,
                                                        @Query("order") String order,
                                                        @Query("channelId") String channelId,
                                                        @Query("type") String type,
                                                        @Query("key") String key,
                                                        @Query("maxResults") String maxResults,
                                                        @Query("pageToken") String pageToken);

    @GET("videos?")
    Observable<YoutubeDetailModel> getYoutubeListDetails(@Query("part") String part,
                                                         @Query("key") String key,
                                                         @Query("id") String ids);

}
