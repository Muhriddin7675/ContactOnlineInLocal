package com.example.mycontactonlyan_3.presenter.screens.register

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface RegisterViewModel {
    val messageError:Flow<String>
    val noInternetConnection:StateFlow<Boolean>
    val progressLiveData :StateFlow<Boolean>

    fun register(firstName:String,lastName:String,phone:String,password:String)
    fun internetConnection()
}