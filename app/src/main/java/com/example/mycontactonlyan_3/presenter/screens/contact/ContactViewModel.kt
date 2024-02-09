package com.example.mycontactonlyan_3.presenter.screens.contact

import com.example.mycontactonlyan_3.data.model.ContactUIData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ContactViewModel {
    val progressLiveData: StateFlow<Boolean>
    val contactsStateFlow: StateFlow<List<ContactUIData>>
    val emptyStateStateFlow: StateFlow<Unit>
    var openBottomSheetDialog : ((ContactUIData) -> Unit)?
    val errorMessageLiveData: Flow<String>

    fun loadContacts()
    fun openAddContactScreen()
    fun openEditContactScreen(data: ContactUIData)
    fun openBottomSheetDialog(data:ContactUIData)
    fun deleteClick(data: ContactUIData)
    fun openLoginScreen()

}