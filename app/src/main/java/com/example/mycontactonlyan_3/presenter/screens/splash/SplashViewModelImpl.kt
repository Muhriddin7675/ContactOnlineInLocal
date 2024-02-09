package com.example.mycontactonlyan_3.presenter.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycontactonlyan_3.navigatiion.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModelImpl @Inject constructor(
    private val navigator: AppNavigator
):SplashViewModel, ViewModel() {
    override fun openLoginScreen() {
        viewModelScope.launch {
            navigator.navigateTo(SplashScreenDirections.actionSplashScreenToLoginScreen())
        }
    }
    override fun openContactScreen() {
        viewModelScope.launch {
            navigator.navigateTo(SplashScreenDirections.actionSplashScreenToContactScreen())
        }
    }

}