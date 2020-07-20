package com.nike.trackit.api

import com.nike.trackit.model.RevereCodingModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("reverse.php")
    suspend fun ReverseCoding(@Query("key")key:String,@Query("lat")latitude:String,@Query("lon")
    logitude:String,@Query("format")format:String="json"):Response<RevereCodingModel>
}