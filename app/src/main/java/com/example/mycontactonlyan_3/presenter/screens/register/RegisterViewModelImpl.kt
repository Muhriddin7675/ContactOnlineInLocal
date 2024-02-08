package com.example.mycontactonlyan_3.presenter.screens.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycontactonlyan_3.data.ResultData
import com.example.mycontactonlyan_3.data.sourse.remote.request.RegisterUserRequest
import com.example.mycontactonlyan_3.domain.ContactRepository
import com.example.mycontactonlyan_3.domain.ContactRepositoryImpl
import com.example.mycontactonlyan_3.utils.NetworkStatusValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RegisterViewModelImpl @Inject constructor(
    private val repositoryImpl: ContactRepository,
    private val networkStatusValidator: NetworkStatusValidator
) : RegisterViewModel,
    ViewModel() {
    override val openVerifyScreen = MutableLiveData<Unit>()
    override val messageError = MutableLiveData<String>()
    override val noInternetConnection = MutableLiveData<Boolean>()
    override val progressLiveData = MutableLiveData<Boolean>()

    override fun register(firstName: String, lastName: String, phone: String, password: String) {
        if (networkStatusValidator.hasNetwork) {
            val registerUser = RegisterUserRequest(firstName, lastName, phone, password)
            progressLiveData.value = true
            repositoryImpl.registerContact(registerUser).onEach {
                when (it) {
                    is ResultData.Success -> {
                        progressLiveData.value = false
                        openVerifyScreen.value = Unit
                    }

                    is ResultData.Failure -> {
                        progressLiveData.value = false
                        messageError.value = it.message
                    }
                }
            }.launchIn(viewModelScope)

        } else {
            noInternetConnection.value = !networkStatusValidator.hasNetwork
        }
    }

    fun internetConnection() {
        noInternetConnection.value = !networkStatusValidator.hasNetwork
    }
}