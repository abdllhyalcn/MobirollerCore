package com.mobiroller.core.serviceinterfaces;


import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * Created by ealtaca on 17.01.2017.
 */

public interface MobirollerStatsInterface {


    @FormUrlEncoded
    @POST("AveAnalytics/UserToken")
    Call<Void> serverRegister(@FieldMap HashMap<String, String> data);


    @FormUrlEncoded
    @POST("MobiRoller/LogStageLogin")
    Call<String> sendUserData(@FieldMap HashMap<String, String> data);

}