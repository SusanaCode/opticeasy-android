package com.opticeasy.app.core.network

import com.opticeasy.app.core.constants.ApiConstants
import com.opticeasy.app.data.remote.api.OpticEasyApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: OpticEasyApi by lazy {
        retrofit.create(OpticEasyApi::class.java)
    }
}
