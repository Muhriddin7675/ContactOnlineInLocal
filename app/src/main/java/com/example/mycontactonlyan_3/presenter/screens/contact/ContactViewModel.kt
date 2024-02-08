package com.example.mycontactonlyan_3.presenter.screens.contact

import androidx.lifecycle.LiveData
import com.example.mycontactonlyan_3.data.model.ContactUIData

interface ContactViewModel {
    val progressLiveData: LiveData<Boolean>
    val contactsLiveData: LiveData<List<ContactUIData>>
    val errorMessageLiveData: LiveData<String>
    val notConnectionLiveData: LiveData<Unit>
    val emptyStateLiveData: LiveData<Unit>
    val openAddContactScreenLiveData : LiveData<Unit>
    val openEditContactScreenLiveData : LiveData<ContactUIData>
    val openBottomSheetDialog : LiveData<ContactUIData>
    val openLoginScreenLiveData:LiveData<Unit>

    fun loadContacts()
    fun openAddContactScreen()
    fun openEditContactScreen(data: ContactUIData)
    fun openBottomSheetDialog(data:ContactUIData)
    fun deleteClick(data: ContactUIData)
    fun editClick(data: ContactUIData)

}