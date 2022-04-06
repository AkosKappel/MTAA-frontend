package com.example.mtaa.api

import android.content.Context
import com.example.mtaa.utilities.Utils
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val BASE_URL = Utils.env?.get("BASE_URL")!!
    private var retrofit: Retrofit? = null

    private fun getClient(context: Context): Retrofit {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create()

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
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
