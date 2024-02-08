package com.example.mycontactonlyan_3.presenter.screens.contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycontactonlyan_3.data.ResultData
import com.example.mycontactonlyan_3.data.model.ContactUIData
import com.example.mycontactonlyan_3.domain.ContactRepository
import com.example.mycontactonlyan_3.domain.ContactRepositoryImpl
import com.example.mycontactonlyan_3.utils.MyEventBus
import com.example.mycontactonlyan_3.utils.NetworkStatusValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ContactViewModelImpl @Inject constructor(
    private val repositoryImpl: ContactRepository,
    private val networkStatusValidator: NetworkStatusValidator
) : ContactViewModel,
    ViewModel() {
    override val progressLiveData = MutableLiveData<Boolean>()
    override val contactsLiveData = MutableLiveData<List<ContactUIData>>()
    override val errorMessageLiveData = MutableLiveData<String>()
    override val notConnectionLiveData = MutableLiveData<Unit>()
    override val emptyStateLiveData = MutableLiveData<Unit>()
    override val openAddContactScreenLiveData = MutableLiveData<Unit>()
    override val openEditContactScreenLiveData = MutableLiveData<ContactUIData>()
    override val openBottomSheetDialog = MutableLiveData<ContactUIData>()
    override val openLoginScreenLiveData = MutableLiveData<Unit>()

    init {
        MyEventBus.reloadEvent = {
            loadContacts()
        }
    }

    override fun loadContacts() {
        progressLiveData.value = true
        repositoryImpl.getAllContact().onEach {
            it.onSuccess {
                progressLiveData.value = false
                if (it.isEmpty()) emptyStateLiveData.value = Unit
                else contactsLiveData.value = it
            }
                .onFailure {
                    progressLiveData.value = false
                    errorMessageLiveData.value = it
                }
        }.launchIn(viewModelScope)
    }

    fun openLoginScreen() {
        openLoginScreenLiveData.value = Unit
    }

    override fun openAddContactScreen() {
        openAddContactScreenLiveData.value = Unit
    }

    override fun openEditContactScreen(data: ContactUIData) {
        openEditContactScreenLiveData.value = data
    }

    override fun openBottomSheetDialog(data: ContactUIData) {
        openBottomSheetDialog.value = data
    }

    override fun deleteClick(data: ContactUIData) {
        progressLiveData.value = true
        repositoryImpl.deleteContact(data.id, data.firstName, data.lastName, data.phone, data.status.statusCode)
            .onEach {
                it.onSuccess {
                    progressLiveData.value = false
                    loadContacts()
                }.onFailure {
                        progressLiveData.value = false
                        errorMessageLiveData.value = it
                    }
            }.launchIn(viewModelScope)
    }

    override fun editClick(data: ContactUIData) {
        openEditContactScreenLiveData.value = data
    }
}