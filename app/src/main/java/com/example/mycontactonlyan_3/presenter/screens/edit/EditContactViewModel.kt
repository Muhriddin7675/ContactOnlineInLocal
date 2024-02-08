package com.example.mycontactonlyan_3.presenter.screens.edit

import androidx.lifecycle.LiveData
import com.example.mycontactonlyan_3.data.model.ContactUIData

interface EditContactViewModel {
    val closeScreenLiveData :LiveData<Unit>
    val progressLiveData:LiveData<Boolean>
    val messageLiveData:LiveData<String>
    val errorMessageLiveData:LiveData<String>

    fun closeScreen()
    fun editContact(data:ContactUIData)
}