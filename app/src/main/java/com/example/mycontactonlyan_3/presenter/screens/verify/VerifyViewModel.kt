package com.example.mycontactonlyan_3.presenter.screens.verify

import android.provider.ContactsContract.CommonDataKinds.Phone
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface VerifyViewModel {
    val progressLiveData :StateFlow<Boolean>
    val noConnection:StateFlow<Boolean>
    val errorMessage:Flow<String>

    fun verify(phone: String,code:Int)
    fun noConnection()
}