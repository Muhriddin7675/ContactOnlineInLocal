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
import com.example.mycontactonlyan_3.utils.NetworkStatusValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class VerifyViewModelImpl @Inject constructor(
    private val repositoryImpl: ContactRepository,
    private val networkStatusValidator: NetworkStatusValidator
) : VerifyViewModel,
    ViewModel() {

    override val progressLiveData = MutableLiveData<Boolean>()

    override val errorMessage = MutableLiveData<String>()

    override val noConnection = MutableLiveData<Boolean>()

    override val openContactScreen = MutableLiveData<Unit>()


    override fun verify(phone: String, code: Int) {
        if (networkStatusValidator.hasNetwork) {
            val verify = VerifyUserRequest(phone, code)
            progressLiveData.value = true
            repositoryImpl.verifyContact(verify).onEach {
                when (it) {
                    is ResultData.Success -> {
                        progressLiveData.value = false
                        MyShared.setToken(it.data.token)
                        openContactScreen.value = Unit
                    }

                    is ResultData.Failure -> {
                        progressLiveData.value = false
                        errorMessage.value = it.message
                    }
                }
            }.launchIn(viewModelScope)

        } else {
            noConnection.value = !networkStatusValidator.hasNetwork
        }
    }
    fun noConnection() {
        noConnection.value = !networkStatusValidator.hasNetwork
    }
}