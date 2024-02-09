package com.example.mycontactonlyan_3.presenter.screens.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycontactonlyan_3.data.model.ContactUIData
import com.example.mycontactonlyan_3.domain.ContactRepository
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
class EditContactModelImpl @Inject constructor(
    private val repository: ContactRepository,
    private val networkStatusValidator: NetworkStatusValidator,
    private val navigator: AppNavigator
) : ViewModel(),
    EditContactViewModel {
    override val progressLiveData = MutableStateFlow<Boolean>(false)
    override val errorMessageLiveData = MutableSharedFlow<String>()

    override fun closeScreen() {
        viewModelScope.launch {
            navigator.back()
        }
    }

    override fun editContact(data: ContactUIData) {
        progressLiveData.value = true
        repository.editContact(
            data.id,
            data.firstName,
            data.lastName,
            data.phone,
            data.status.statusCode
        ).onEach {
            progressLiveData.value = true
            it.onSuccess {
                viewModelScope.launch {
                    progressLiveData.value = false
                    if (networkStatusValidator.hasNetwork) errorMessageLiveData.emit("Success !")
                    else errorMessageLiveData.emit("Save in local !")
                }
                MyEventBus.reloadEvent!!.invoke()

            }.onFailure {
                viewModelScope.launch {
                    progressLiveData.value = false
                    errorMessageLiveData.emit(it)
                }
            }
        }.launchIn(viewModelScope)
    }


}