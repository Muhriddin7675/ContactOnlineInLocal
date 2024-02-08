package com.example.mycontactonlyan_3.presenter.screens.verify

import android.provider.ContactsContract.CommonDataKinds.Phone
import androidx.lifecycle.LiveData

interface VerifyViewModel {
    val progressLiveData :LiveData<Boolean>
    val errorMessage:LiveData<String>
    val noConnection:LiveData<Boolean>
    val openContactScreen:LiveData<Unit>

    fun verify(phone: String,code:Int)
}