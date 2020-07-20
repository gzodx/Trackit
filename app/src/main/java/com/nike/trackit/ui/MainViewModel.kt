package com.nike.trackit.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.nike.trackit.model.RevereCodingModel
import com.nike.trackit.repository.repository

class MainViewModel :ViewModel(){
    var trigger= MutableLiveData<String>()
    private val _trigger: LiveData<String> = trigger


    val otpRequest: LiveData<RevereCodingModel> = Transformations
        .switchMap(_trigger){value->
            if (value.isNullOrBlank()){
                AbsentLiveData.create()
            }else{
                repository.getLocationAddress("","","json")

            }

        }
}