package com.opticeasy.app.core.network

import android.content.Context
import com.opticeasy.app.core.constants.ApiConstants
import com.opticeasy.app.data.remote.api.OpticEasyApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    @Volatile
    private var apiInstance: OpticEasyApi? = null

    fun getApi(context: Context): OpticEasyApi {
        return apiInstance ?: synchronized(this) {
            apiInstance ?: buildApi(context).also { apiInstance = it }
        }
    }

    private fun buildApi(context: Context): OpticEasyApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context.applicationContext))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(OpticEasyApi::class.java)
    }
}