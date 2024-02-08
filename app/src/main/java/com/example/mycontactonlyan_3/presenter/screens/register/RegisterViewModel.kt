package com.example.mycontactonlyan_3.presenter.screens.register

import android.health.connect.datatypes.BloodPressureRecord.BodyPosition
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

interface RegisterViewModel {
    val openVerifyScreen : LiveData<Unit>
    val messageError:LiveData<String>
    val noInternetConnection:LiveData<Boolean>
    val progressLiveData :LiveData<Boolean>

    fun register(firstName:String,lastName:String,phone:String,password:String)
}