package com.example.mycontactonlyan_3.presenter.screens.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycontactonlyan_3.data.model.ContactUIData
import com.example.mycontactonlyan_3.domain.ContactRepository
import com.example.mycontactonlyan_3.navigatiion.AppNavigator
import com.example.mycontactonlyan_3.utils.MyEventBus
import com.example.mycontactonlyan_3.utils.NetworkStatusValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModelImpl @Inject constructor(
    private val repositoryImpl: ContactRepository,
    private val networkStatusValidator: NetworkStatusValidator,
    private val navigator: AppNavigator
) : ContactViewModel,
    ViewModel() {


    init {
        MyEventBus.reloadEvent = {
            loadContacts()
        }
    }

    override val progressLiveData = MutableStateFlow<Boolean>(false)
    override val contactsStateFlow = MutableStateFlow<List<ContactUIData>>(emptyList())
    override val emptyStateStateFlow = MutableStateFlow<Unit>(Unit)
    override var openBottomSheetDialog: ((ContactUIData) -> Unit)? = null
    override val errorMessageLiveData = MutableSharedFlow<String>()

    override fun loadContacts() {
        progressLiveData.value = true
        repositoryImpl.getAllContact().onEach {
            it.onSuccess {
                viewModelScope.launch {
                    progressLiveData.value = false
                    if (it.isEmpty()) emptyStateStateFlow.value = Unit
                    else contactsStateFlow.value = it
                }
            }
                .onFailure {
                    viewModelScope.launch {
                        progressLiveData.value = false
                        errorMessageLiveData.emit(it)
                    }
                }
        }.launchIn(viewModelScope)
    }

    override fun openLoginScreen() {
        viewModelScope.launch {
            navigator.navigateTo(ContactScreenDirections.actionContactScreenToLoginScreen())
        }
    }

    override fun openAddContactScreen() {
        viewModelScope.launch {
            navigator.navigateTo(ContactScreenDirections.actionContactScreenToAddScreen())
        }
    }

    override fun openEditContactScreen(data: ContactUIData) {
        viewModelScope.launch {
            navigator.navigateTo(
                ContactScreenDirections.actionContactScreenToEditScreen(
                    data.firstName,
                    data.lastName,
                    data.phone,
                    data.id
                )
            )
        }
    }

    override fun openBottomSheetDialog(data: ContactUIData) {
      openBottomSheetDialog?.invoke(data)
    }

    override fun deleteClick(data: ContactUIData) {
        progressLiveData.value = true
        repositoryImpl.deleteContact(
            data.id,
            data.firstName,
            data.lastName,
            data.phone,
            data.status.statusCode
        )
            .onEach { it ->
                it.onSuccess {
                    viewModelScope.launch {
                        progressLiveData.value = false
                        loadContacts()
                    }
                }.onFailure {
                    viewModelScope.launch {
                        progressLiveData.value = false
                        errorMessageLiveData.emit(it)
                    }
                }
            }.launchIn(viewModelScope)
    }


}