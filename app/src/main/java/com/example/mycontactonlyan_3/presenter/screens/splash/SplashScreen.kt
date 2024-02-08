package com.example.mycontactonlyan_3.presenter.screens.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mycontactonlyan_3.R
import com.example.mycontactonlyan_3.data.model.MyShared
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : Fragment(R.layout.screen_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            delay(1000)
            if (MyShared.getToken() == "1") {
                findNavController().navigate(R.id.action_splashScreen_to_loginScreen)
            } else {
                findNavController().navigate(R.id.action_splashScreen_to_contactScreen)

            }
        }
    }
}