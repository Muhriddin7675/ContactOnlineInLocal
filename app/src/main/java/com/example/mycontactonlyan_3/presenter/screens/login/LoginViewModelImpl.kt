package com.example.mycontactonlyan_3.presenter.screens.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycontactonlyan_3.data.ResultData
import com.example.mycontactonlyan_3.data.model.MyShared
import com.example.mycontactonlyan_3.data.sourse.remote.request.LoginRequest
import com.example.mycontactonlyan_3.domain.ContactRepository
import com.example.mycontactonlyan_3.navigatiion.AppNavigator
import com.example.mycontactonlyan_3.utils.NetworkStatusValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModelImpl @Inject constructor(
    private val repository: ContactRepository,
    private val networkStatusValidator: NetworkStatusValidator,
    private val appNavigator: AppNavigator
) : LoginViewModel,
    ViewModel() {
    override val progressLiveData = MutableStateFlow<Boolean>(false)
    override val refreshInternetConnection = MutableStateFlow<Boolean>(false)
    override val errorMessage = MutableSharedFlow<String>()


    override fun login(phone: String, password: String) {
        val loginRequest = LoginRequest(phone, password)
        progressLiveData.value = true
        repository.loginContact(loginRequest).onEach { it ->
            it.onSuccess {
                MyShared.setToken(it.token)
                viewModelScope.launch {
                    progressLiveData.value = false
                    refreshInternetConnection.value = !networkStatusValidator.hasNetwork
                    appNavigator.navigateTo(LoginScreenDirections.actionLoginScreenToContactScreen())
                }

            }.onFailure {
                viewModelScope.launch {
                    progressLiveData.value = false
                    errorMessage.emit(it)
                }
            }
        }.launchIn(viewModelScope)
    }

    override fun networkConnect() {
        viewModelScope.launch {
            refreshInternetConnection.emit(!networkStatusValidator.hasNetwork)
        }
    }

    override fun openRegisterScreen() {
        viewModelScope.launch {
            Timber.tag("TTT").d("Asadlom")
            appNavigator.navigateTo(LoginScreenDirections.actionLoginScreenToRegisterScreen())
        }
    }
}