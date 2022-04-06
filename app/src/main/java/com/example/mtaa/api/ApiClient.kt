package com.example.mtaa.api

import android.content.Context
import com.example.mtaa.utilities.Utils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    //    private const val BASE_URL = "http://147.175.176.72:8000"
//    private const val BASE_URL = "http://147.175.176.85:8000"
    private val BASE_URL = Utils.env?.get("BASE_URL")!!
    private var retrofit: Retrofit? = null

    private fun getClient(context: Context): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient(context))
                .build()
        }
        return retrofit!!
    }

    fun getApiService(context: Context): ApiInterface {
        return getClient(context).create(ApiInterface::class.java)
    }

    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()
    }

}
