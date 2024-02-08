package com.example.mycontactonlyan_3.presenter.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycontactonlyan_3.data.ResultData
import com.example.mycontactonlyan_3.data.model.MyShared
import com.example.mycontactonlyan_3.data.sourse.remote.request.LoginRequest
import com.example.mycontactonlyan_3.domain.ContactRepository
import com.example.mycontactonlyan_3.domain.ContactRepositoryImpl
import com.example.mycontactonlyan_3.utils.NetworkStatusValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewModelImpl @Inject constructor(
    private val repository: ContactRepository,
    private val networkStatusValidator: NetworkStatusValidator
) : LoginViewModel,
    ViewModel() {

    override val openRegisterScreenLiveData = MutableLiveData<Unit>()

    override val openContactScreenLiveData = MutableLiveData<Unit>()

    override val progressLiveData = MutableLiveData<Boolean>()

    override val refreshInternetConnection = MutableLiveData<Boolean>()

    override val loginBool = MutableLiveData<Boolean>()

    override val errorMessage = MutableLiveData<String>()


    override fun login(phone: String, password: String) {
        val loginRequest = LoginRequest(phone, password)
        progressLiveData.value = true
        repository.loginContact(loginRequest).onEach {
            when (it) {
                is ResultData.Success -> {
                    MyShared.setToken(it.data.token)
                    progressLiveData.value = false
                    loginBool.value = true
                    networkConnect()
                }

                is ResultData.Failure -> {
                    progressLiveData.value = false
                    loginBool.value = false
                    errorMessage.value = it.message
                }
            }
        }.launchIn(viewModelScope)
    }
    fun networkConnect() {
        refreshInternetConnection.value = !networkStatusValidator.hasNetwork
    }

    fun openRegisterScreen() {
        openRegisterScreenLiveData.value = Unit
    }

    fun openContactScreen() {
        openContactScreenLiveData.value = Unit
    }
}