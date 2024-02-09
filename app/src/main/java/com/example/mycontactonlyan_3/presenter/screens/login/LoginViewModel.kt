package com.example.mycontactonlyan_3.presenter.screens.login

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface LoginViewModel {
    val progressLiveData:StateFlow<Boolean>
    val refreshInternetConnection:StateFlow<Boolean>
    val errorMessage:Flow<String>

    fun login(phone:String,password:String)
    fun networkConnect()
    fun openRegisterScreen()
}