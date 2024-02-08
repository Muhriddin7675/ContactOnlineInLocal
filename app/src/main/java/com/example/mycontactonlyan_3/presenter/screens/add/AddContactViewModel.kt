package com.example.mycontactonlyan_3.presenter.screens.add

import androidx.lifecycle.LiveData

interface AddContactViewModel {
    val closeScreenLiveData : LiveData<Unit>
    val progressLiveData: LiveData<Boolean>
    val messageLiveData: LiveData<String>
    val errorMessageLiveData : LiveData<String>

    fun closeScreen()
    fun addContact(firstName: String, lastName: String, phone:String)
}