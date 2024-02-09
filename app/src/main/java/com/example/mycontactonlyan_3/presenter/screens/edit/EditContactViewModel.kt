package com.example.mycontactonlyan_3.presenter.screens.edit

import androidx.lifecycle.LiveData
import com.example.mycontactonlyan_3.data.model.ContactUIData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface EditContactViewModel {

    val progressLiveData:StateFlow<Boolean>
    val errorMessageLiveData:Flow<String>

    fun closeScreen()
    fun editContact(data:ContactUIData)
}