package com.example.mycontactonlyan_3.presenter.screens.edit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycontactonlyan_3.data.model.ContactUIData
import com.example.mycontactonlyan_3.data.model.StatusEnum
import com.example.mycontactonlyan_3.domain.ContactRepository
import com.example.mycontactonlyan_3.domain.ContactRepositoryImpl
import com.example.mycontactonlyan_3.utils.MyEventBus
import com.example.mycontactonlyan_3.utils.NetworkStatusValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditContactModelImpl @Inject constructor(
    private val repository: ContactRepository,
    private val networkStatusValidator: NetworkStatusValidator
) : ViewModel(),
    EditContactViewModel {
    override val closeScreenLiveData = MutableLiveData<Unit>()
    override val progressLiveData = MutableLiveData<Boolean>()
    override val messageLiveData = MutableLiveData<String>()
    override val errorMessageLiveData = MutableLiveData<String>()

    override fun closeScreen() {
        closeScreenLiveData.value = Unit
    }

    override fun editContact(data: ContactUIData) {
        progressLiveData.value = true
//        Timber.tag("TTT").d("${data.firstName} ${data.lastName} ${data.phone} ${data.status}")

        repository.editContact(
            data.id,
            data.firstName,
            data.lastName,
            data.phone,
            data.status.statusCode
        ).onEach {
            progressLiveData.value = true
            it.onSuccess {
                progressLiveData.value = false
                if (networkStatusValidator.hasNetwork) messageLiveData.value = "Success !"
                else messageLiveData.value = "Save in local !"
                MyEventBus.reloadEvent!!.invoke()

            }.onFailure {
                progressLiveData.value = false
                errorMessageLiveData.value = it
            }
        }.launchIn(viewModelScope)
    }


}