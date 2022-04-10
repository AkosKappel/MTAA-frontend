package com.example.mtaa.api

import android.content.Context
import com.example.mtaa.utilities.Settings
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

object ApiClient {

    private val BASE_URL = Settings.env?.get("BASE_URL")!!
    private var retrofit: Retrofit? = null

    private fun getClient(context: Context): Retrofit {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create()

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(nullOnEmptyConverterFactory)
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

    private val nullOnEmptyConverterFactory = object : Converter.Factory() {
        fun converterFactory() = this
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<out Annotation>,
            retrofit: Retrofit
        ) = object : Converter<ResponseBody, Any?> {
            val nextResponseBodyConverter =
                retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)

            override fun convert(value: ResponseBody) =
                if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
        }
    }

}
