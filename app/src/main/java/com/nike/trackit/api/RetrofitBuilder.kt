package com.nike.trackit.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    const val BASE_URL="https://us1.locationiq.com/v1/"

    /**Singleton retrofit builder**/
    val retofitBuilder : Retrofit.Builder by lazy {
        Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    private val interceptor= HttpLoggingInterceptor()
        .apply { level=HttpLoggingInterceptor.Level.BODY }

    val httpClient= OkHttpClient.Builder().addInterceptor(interceptor).build()

    /** Singleton Api service **/
    val apiService:ApiServices by lazy {
        retofitBuilder.build().create(ApiServices::class.java)
    }

}
