package com.mobiroller.core.helpers.godmode

import android.content.Intent
import okhttp3.*
import java.io.IOException
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.mobiroller.core.SharedApplication
import com.mobiroller.core.activities.GodModeActivity
import com.mobiroller.core.BuildConfig
import java.io.Serializable


class GodMode {

    companion object {
        const val isActive = true
        var apps: ArrayList<SquidexAppData>? = null
    }

    fun auth() {
        if (!BuildConfig.DEBUG) {
            return
        }

        if (SharedApplication.lastActivity is GodModeActivity) {
            return
        }

        val client: OkHttpClient = OkHttpClient().newBuilder().build()
        val mediaType: MediaType? = MediaType.parse("application/x-www-form-urlencoded")
        val body: RequestBody = RequestBody.create(
            mediaType,
            "grant_type=client_credentials&client_id=6023896c4a2be2b361ff940f&client_secret=up4gkmfjfiigp0cagcgzmqdusllbmrlxzfmkphtba5sx&scope=squidex-api"
        )
        val request: Request = Request.Builder()
            .url("https://cloud.squidex.io/identity-server/connect/token")
            .method("POST", body)
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                val gson = Gson()
                val responseBody = client.newCall(request).execute().body()
                val responseModel: SquidexAuth =
                    gson.fromJson(responseBody!!.string(), SquidexAuth::class.java)
                responseModel.accessToken?.let { getApps(token = it) }
            }

        })

    }

    fun getApps(token: String) {
        val client = OkHttpClient().newBuilder().build()
        val request: Request = Request.Builder()
            .url("https://cloud.squidex.io/api/content/denememobiroller/app")
            .method("GET", null)
            .addHeader(
                "Authorization",
                "Bearer $token"
            )
            .addHeader("X-Flatten", "true")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                val gson = Gson()
                val responseBody = client.newCall(request).execute().body()
                val responseModel: SquidexAppItems =
                    gson.fromJson(responseBody!!.string(), SquidexAppItems::class.java)
                apps = responseModel.items
                val intent = Intent(SharedApplication.context, GodModeActivity::class.java)
                SharedApplication.lastActivity.startActivity(intent)
            }

        })
    }


}

class SquidexAuth: Serializable {

    @SerializedName("access_token")
    var accessToken: String? = null

}

class SquidexAppItems: Serializable {

    var items: ArrayList<SquidexAppData>? = null
}

class SquidexAppData: Serializable {

    var id: String? = null
    var data: SquidexApp? = null
}

class SquidexApp: Serializable {

    @SerializedName("Name")
    var name: String? = null

    @SerializedName("Description")
    var description: String? = null

    @SerializedName("AppKey")
    var appKey: String? = null

    @SerializedName("ApiKey")
    var apiKey: String? = null

    @SerializedName("ShopirollerApiKey")
    var shopirollerApiKey: String? = null

    @SerializedName("AccountEmail")
    var accountEmail: String? = null

    @SerializedName("MobirollerBaseURL")
    var mobirollerBaseURL: String? = null

    @SerializedName("ShopirollerBaseURL")
    var shopirollerBaseURL: String? = null

    @SerializedName("ApplyzeBaseURL")
    var applyzeBaseURL: String? = null

}
