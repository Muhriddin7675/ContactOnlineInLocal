package com.example.mycontactonlyan_3.presenter.screens.login

import androidx.lifecycle.LiveData

interface LoginViewModel {
    val openRegisterScreenLiveData:LiveData<Unit>
    val openContactScreenLiveData:LiveData<Unit>
    val progressLiveData:LiveData<Boolean>
    val refreshInternetConnection:LiveData<Boolean>
    val loginBool:LiveData<Boolean>
    val errorMessage:LiveData<String>

    fun login(phone:String,password:String)
}