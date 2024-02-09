package com.example.mycontactonlyan_3.presenter.screens.add

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycontactonlyan_3.data.sourse.remote.request.CreateContactRequest
import com.example.mycontactonlyan_3.domain.ContactRepositoryImpl
import com.example.mycontactonlyan_3.navigatiion.AppNavigator
import com.example.mycontactonlyan_3.utils.MyEventBus
import com.example.mycontactonlyan_3.utils.NetworkStatusValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddContactViewModelImpl
@Inject constructor(
    private val repositoryImpl: ContactRepositoryImpl,
    private val networkStatusValidator: NetworkStatusValidator,
    private val  navigator: AppNavigator
) :
    AddContactViewModel, ViewModel() {
    override val progressLiveData = MutableStateFlow<Boolean>(false)
    override val errorMessageLiveData = MutableSharedFlow<String>()

    override fun closeScreen() {
        viewModelScope.launch {
            navigator.back()
        }
    }
    override fun addContact(firstName: String, lastName: String, phone: String) {
        progressLiveData.value = true

        repositoryImpl.addContact(CreateContactRequest(firstName,lastName,phone)).onEach {
            progressLiveData.value = true
            it.onSuccess {
                viewModelScope.launch {
                    progressLiveData.value = false
                    if (networkStatusValidator.hasNetwork) errorMessageLiveData.emit("Success!")
                    else errorMessageLiveData.emit("Save in local")
                    closeScreen()
                }
                MyEventBus.reloadEvent?.invoke()

            }.onFailure {
                viewModelScope.launch {
                    progressLiveData.value = false
                    errorMessageLiveData.emit(it)
                }
            }
        }.launchIn(viewModelScope)
    }

}