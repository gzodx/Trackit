package com.nike.trackit.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.nike.trackit.api.RetrofitBuilder
import com.nike.trackit.model.RevereCodingModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

object repository{
    fun getLocationAddress(lat:String,lon:String,format:String): LiveData<RevereCodingModel> {
        return object :LiveData<RevereCodingModel>() {
            override fun onActive() {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response= RetrofitBuilder.apiService.ReverseCoding(lat,lon,format)
                        if (response.isSuccessful){
                            withContext(Dispatchers.Main){
                                value=response.body()
                            }
                            return@launch
                        }
                        Log.d("Debug error","respones code is ${response.code()} ")
                    }catch (e:Exception){
                        Log.d("Debug error","an error occured ${e.localizedMessage} ")
                    }catch (https: HttpException){
                        Log.d("Https: error","an error occured ${https.message()}")
                    }


                }
            }
        }
    }
}