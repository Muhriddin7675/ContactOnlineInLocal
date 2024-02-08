package com.example.mycontactonlyan_3.presenter.screens.add

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycontactonlyan_3.data.sourse.remote.request.CreateContactRequest
import com.example.mycontactonlyan_3.domain.ContactRepositoryImpl
import com.example.mycontactonlyan_3.utils.MyEventBus
import com.example.mycontactonlyan_3.utils.NetworkStatusValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddContactViewModelImpl
@Inject constructor(
    private val repositoryImpl: ContactRepositoryImpl,
    private val networkStatusValidator: NetworkStatusValidator
) :
    AddContactViewModel, ViewModel() {
    override val closeScreenLiveData = MutableLiveData<Unit>()
    override val progressLiveData = MutableLiveData<Boolean>()
    override val messageLiveData = MutableLiveData<String>()
    override val errorMessageLiveData = MutableLiveData<String>()

    override fun closeScreen() {
        closeScreenLiveData.value = Unit
    }

    override fun addContact(firstName: String, lastName: String, phone: String) {
        progressLiveData.value = true

        repositoryImpl.addContact(CreateContactRequest(firstName,lastName,phone)).onEach {
            progressLiveData.value = true
            it.onSuccess {
                progressLiveData.value = false
                if (networkStatusValidator.hasNetwork) messageLiveData.value = "Success!"
                else messageLiveData.value = "Save in local"

                closeScreenLiveData.value = Unit
                MyEventBus.reloadEvent?.invoke()
            }.onFailure {
                progressLiveData.value = false
                errorMessageLiveData.value = it
            }
        }.launchIn(viewModelScope)
    }

}