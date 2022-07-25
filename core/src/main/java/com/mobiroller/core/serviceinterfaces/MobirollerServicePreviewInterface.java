package com.mobiroller.core.serviceinterfaces;


import com.mobiroller.core.models.MainModel;
import com.mobiroller.core.models.NavigationModel;
import com.mobiroller.core.coreui.models.ScreenModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by ealtaca on 17.01.2017.
 */

public interface MobirollerServicePreviewInterface {

    @GET("JSON/GetJSONForPreview/")
    Call<MainModel> getMainJSON(@Query("accountScreenID") String url);

    @GET("JSON/GetJSONForPreview/")
    Call<ScreenModel> getScreenJSON(@Query("accountScreenID") String url);

    @GET("JSON/GetJSONForPreview/")
    Call<NavigationModel> getNavigationJSON(@Query("accountScreenID") String url);

    @POST("Account/ForgotPassword")
    Call<Boolean> forgotPassword(@Query("email") String email);

}