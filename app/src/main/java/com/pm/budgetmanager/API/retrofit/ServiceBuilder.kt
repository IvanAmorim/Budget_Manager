package com.pm.budgetmanager.API.retrofit

import com.pm.budgetmanager.API.utils.Constants.Companion.API_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private  val client = OkHttpClient.Builder().build()

    private  val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun<T> buildService(service: Class<T>) : T {
        return  retrofit.create(service)
    }
}
