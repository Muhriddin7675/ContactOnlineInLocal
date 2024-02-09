package com.example.mycontactonlyan_3.presenter.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycontactonlyan_3.data.ResultData
import com.example.mycontactonlyan_3.data.sourse.remote.request.RegisterUserRequest
import com.example.mycontactonlyan_3.domain.ContactRepository
import com.example.mycontactonlyan_3.navigatiion.AppNavigator
import com.example.mycontactonlyan_3.utils.NetworkStatusValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModelImpl @Inject constructor(
    private val repositoryImpl: ContactRepository,
    private val networkStatusValidator: NetworkStatusValidator,
    private val navigator: AppNavigator
) : RegisterViewModel,
    ViewModel() {
    override val messageError = MutableSharedFlow<String>()
    override val noInternetConnection = MutableStateFlow<Boolean>(false)
    override val progressLiveData = MutableStateFlow<Boolean>(false)

    override fun register(firstName: String, lastName: String, phone: String, password: String) {

            val registerUser = RegisterUserRequest(firstName, lastName, phone, password)
            progressLiveData.value = true
            repositoryImpl.registerContact(registerUser).onEach { it ->
                it.onSuccess {
                    viewModelScope.launch {
                        progressLiveData.value = false
                        navigator.navigateTo(RegisterScreenDirections.actionRegisterScreenToVerifyScreen(phone))
                    }
                }

                it.onFailure {
                    viewModelScope.launch {
                        progressLiveData.value = false
                        messageError.emit(it)
                    }
                }
            }.launchIn(viewModelScope)


    }

    override fun internetConnection() {
        viewModelScope.launch {
            noInternetConnection.emit(!networkStatusValidator.hasNetwork)
        }
    }
}