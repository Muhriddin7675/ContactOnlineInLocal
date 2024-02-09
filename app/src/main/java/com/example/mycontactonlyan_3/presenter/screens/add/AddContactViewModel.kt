package com.example.mycontactonlyan_3.presenter.screens.add

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AddContactViewModel {
    val progressLiveData: StateFlow<Boolean>
    val errorMessageLiveData : Flow<String>

    fun closeScreen()
    fun addContact(firstName: String, lastName: String, phone:String)
}