package com.example.mycontactonlyan_3.presenter.screens.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mycontactonlyan_3.R
import com.example.mycontactonlyan_3.data.model.MyShared
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
@AndroidEntryPoint
class SplashScreen : Fragment(R.layout.screen_splash) {

    private val viewModel:SplashViewModel by viewModels<SplashViewModelImpl>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            delay(1000)
            if (MyShared.getToken() == "1") {
                viewModel.openLoginScreen()
            } else {
                viewModel.openContactScreen()
            }
        }
    }
}