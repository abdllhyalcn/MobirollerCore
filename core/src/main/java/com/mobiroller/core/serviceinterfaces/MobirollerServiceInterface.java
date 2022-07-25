package com.mobiroller.core.serviceinterfaces;


import com.mobiroller.core.models.AccountInfoModel;
import com.mobiroller.core.models.InAppPurchaseModel;
import com.mobiroller.core.models.IntroModel;
import com.mobiroller.core.models.MainModel;
import com.mobiroller.core.models.MobirollerBadgeModel;
import com.mobiroller.core.models.NavigationModel;
import com.mobiroller.core.coreui.models.ScreenModel;
import com.mobiroller.core.models.TwitterModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


/**
 * Created by ealtaca on 17.01.2017.
 */

public interface MobirollerServiceInterface {

    @GET("JSON/GetJSON")
    Call<MainModel> getMainJSON(@Query("accountScreenID") String url);

    @GET("JSON/GetJSON")
    Call<ScreenModel> getScreenJSON(@Query("accountScreenID") String url);

    @GET("JSON/GetJSON")
    Call<NavigationModel> getNavigationJSON(@Query("accountScreenID") String url);

    @GET("JSON/GetJSON")
    Call<IntroModel> getIntroJSON(@Query("accountScreenID") String url);

    @GET("aveRateApp/SendFeedBack")
    Call<ResponseBody> sendFeedback(@Query("accountName") String accountName, @Query("message") String message, @Query("rating") int rating);

    @GET("JSON/GetAveTweetUserTimeline")
    Call<ArrayList<TwitterModel>> getTwitterModel(@Query("userName") String username, @Query("tweetCount") String tweetCount, @Query("accountName") String accountName);

    @GET("MobiRoller/GetIpAddress")
    Call<String> getIpAddress();


    @FormUrlEncoded
    @POST("FormPost/aveFormViewPost")
    Call<ResponseBody> sendForm(@FieldMap HashMap<String, String> data);

    @Multipart
    @POST("FormPost/aveFormViewPost")
    Call<ResponseBody> sendFormWithImages(@PartMap() HashMap<String, RequestBody> data,@Part List<MultipartBody.Part> files);

    @GET
    Call<String> getScript(@Url String url);

    @POST("ProfilePost/GetOrphanAccountInfo")
    Call<AccountInfoModel> getUserLoginKey(@QueryMap Map<String, String> options);

    @GET("JSON/GetJSON")
    Call<InAppPurchaseModel> getInAppPurchaseProducts(@Query("accountScreenID") String url);

    @GET
    Call<MobirollerBadgeModel> getBadgeInfo(@Url String url);

}