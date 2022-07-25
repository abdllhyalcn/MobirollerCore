package com.mobiroller.core.helpers;

import android.content.Context;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobiroller.core.SharedApplication;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.coreui.helpers.LocaleHelper;
import com.mobiroller.core.serviceinterfaces.MobirollerServiceInterface;
import com.mobiroller.core.serviceinterfaces.MobirollerServicePreviewInterface;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import com.facebook.stetho.okhttp3.StethoInterceptor;

/**
 * Created by OKN on 22.02.2017.
 */

public class RequestHelper {

    /**
     * Request meta tags
     **/
    private static final String REQUEST_META_APP_VERSION = "app_version";
    private static final String REQUEST_META_OS_TYPE = "os_type";
    private static final String REQUEST_META_OS_VERSION = "os_version";
    private static final String REQUEST_META_LANGUAGE = "language";
    private static final String REQUEST_HEADER_LANGUAGE = "Accept-Language";
    private static final String REQUEST_HEADER_ACCEPT = "Accept";
    private static final String REQUEST_HEADER_APPLYZE_API_KEY = "Api-key";

    /**
     * Client request timeout values
     */
    private static final int REQ_CONNECTION_TIMEOUT = 10;
    private static final int REQ_WRITE_TIMEOUT = 10;
    private static final int REQ_READ_TIMEOUT = 10;

    /**
     * Meta request values
     **/
    private static final String META_VAL_OS_TYPE = "1";


    public RequestHelper ()
    {
    }

    @Deprecated
    public RequestHelper(final Context context, final SharedPrefHelper sharedPrefHelper) {
    }

    public <T> T getAPIService(Class<T> serviceInterface, String baseUrl) {
        Retrofit.Builder builder = getRetrofitBuilder();
        Retrofit retrofit = builder
                .baseUrl(baseUrl)
                .build();
        return retrofit.create(serviceInterface);
    }

    public MobirollerServiceInterface getAPIService() {
        return getAPIService(MobirollerServiceInterface.class, Constants.MobiRoller_BaseURL);
    }

    public MobirollerServicePreviewInterface getPreviewAPIService() {
        return getAPIService(MobirollerServicePreviewInterface.class, Constants.MobiRoller_BaseURL);
    }

    private Retrofit.Builder getRetrofitBuilder() {

        OkHttpClient.Builder clientBuilder =
                new OkHttpClient.Builder()
                        .connectTimeout(REQ_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(REQ_WRITE_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(REQ_READ_TIMEOUT, TimeUnit.SECONDS);

        if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_0)
                    .allEnabledCipherSuites()
                    .build();
            clientBuilder
                    .connectionSpecs(Collections.singletonList(spec));
        }

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        clientBuilder.addInterceptor(interceptor);
        String headerLang = "EN-us";
        if (LocaleHelper.getLocale(SharedApplication.context).toUpperCase().equalsIgnoreCase("TR") || LocaleHelper.getLocale(SharedApplication.context).toUpperCase().contains("TR"))
            headerLang = "TR-tr";
        final String finalHeaderLang = headerLang;
        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader(REQUEST_META_APP_VERSION, UtilManager.sharedPrefHelper().getAppVersionName())
                        .addHeader(REQUEST_META_OS_TYPE, META_VAL_OS_TYPE)
                        .addHeader(REQUEST_META_OS_VERSION, Build.VERSION.RELEASE)
                        .addHeader(REQUEST_META_LANGUAGE, LocaleHelper.getLocale(SharedApplication.context).toUpperCase())
                        .addHeader(REQUEST_HEADER_LANGUAGE, finalHeaderLang)
                        .addHeader(REQUEST_HEADER_ACCEPT, "application/json")
                        .addHeader(REQUEST_HEADER_APPLYZE_API_KEY, AppConfigurations.Companion.getApiKey())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = clientBuilder.build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson));
    }
}
