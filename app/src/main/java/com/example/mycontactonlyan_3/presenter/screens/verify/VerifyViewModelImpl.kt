package com.example.mycontactonlyan_3.presenter.screens.verify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycontactonlyan_3.data.ResultData
import com.example.mycontactonlyan_3.data.model.MyShared
import com.example.mycontactonlyan_3.data.sourse.remote.request.VerifyUserRequest
import com.example.mycontactonlyan_3.domain.ContactRepository
import com.example.mycontactonlyan_3.domain.ContactRepositoryImpl
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
import javax.inject.Inject

@HiltViewModel
class VerifyViewModelImpl @Inject constructor(
    private val repositoryImpl: ContactRepository,
    private val networkStatusValidator: NetworkStatusValidator,
    private val navigator: AppNavigator
) : VerifyViewModel,
    ViewModel() {
    override val progressLiveData = MutableStateFlow<Boolean>(false)
    override val noConnection = MutableStateFlow<Boolean>(false)
    override val errorMessage = MutableSharedFlow<String>()
    override fun verify(phone: String, code: Int) {

        val verify = VerifyUserRequest(phone, code)
        progressLiveData.value = true
        repositoryImpl.verifyContact(verify).onEach { it ->
            it.onSuccess {
                MyShared.setToken(it.token)
                viewModelScope.launch {
                    progressLiveData.value = false
                    navigator.navigateTo(VerifyScreenDirections.actionVerifyScreenToContactScreen())
                }
            }
            it.onFailure {
                viewModelScope.launch {
                    progressLiveData.value = false
                    errorMessage.emit(it)
                }
            }
        }.launchIn(viewModelScope)
    }

    override fun noConnection() {
        noConnection.value = !networkStatusValidator.hasNetwork
    }
}