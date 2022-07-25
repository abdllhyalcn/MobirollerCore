package com.mobiroller.core.helpers;

import android.content.Context;
import android.os.Build;

import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.serviceinterfaces.MobirollerStatsInterface;

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
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

//import com.facebook.stetho.okhttp3.StethoInterceptor;

/**
 * Created by OKN on 22.02.2017.
 */

public class RxJavaRequestStatsHelper {

    private MobirollerStatsInterface mobirollerStatsInterface;
    private SharedPrefHelper sharedPrefHelper;

    /**
     * Request meta tags
     **/
    private static final String REQUEST_META_APP_VERSION = "app_version";
    private static final String REQUEST_META_OS_TYPE = "os_type";
    private static final String REQUEST_META_OS_VERSION = "os_version";
    private static final String REQUEST_META_LANGUAGE = "language";

    /**
     * Client request timeout values
     */
    private static final int REQ_CONNECTION_TIMEOUT = 15;
    private static final int REQ_WRITE_TIMEOUT = 15;
    private static final int REQ_READ_TIMEOUT = 30;

    /**
     * Meta request values
     **/
    private static final String META_VAL_OS_TYPE = "1";


    public RxJavaRequestStatsHelper(final Context context, final SharedPrefHelper sharedPrefHelper) {
        this.sharedPrefHelper = sharedPrefHelper;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
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
        clientBuilder.addInterceptor(logging);
        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
//                        .addHeader(REQUEST_META_APP_VERSION, sharedPrefHelper.getAppVersionName(context))
//                        .addHeader(REQUEST_META_OS_TYPE, META_VAL_OS_TYPE)
//                        .addHeader(REQUEST_META_OS_VERSION, Build.VERSION.RELEASE)
//                        .addHeader(REQUEST_META_LANGUAGE, sharedPrefHelper.getDeviceLang())
                        .build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client = clientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.MobiRoller_BaseURL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mobirollerStatsInterface = retrofit.create(MobirollerStatsInterface.class);
    }

    public MobirollerStatsInterface getAPIService() {
        return mobirollerStatsInterface;
    }

}
