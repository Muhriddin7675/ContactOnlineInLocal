package com.example.mycontactonlyan_3.presenter.screens.login

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mycontactonlyan_3.R
import com.example.mycontactonlyan_3.databinding.ScreenLoginBinding
import com.example.mycontactonlyan_3.utils.showToast
import com.example.mycontactonlyan_3.utils.text
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginScreen : Fragment(R.layout.screen_login) {
    private val binding by viewBinding(ScreenLoginBinding::bind)
    private val viewModel by viewModels<LoginViewModelImpl> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.openRegisterScreenLiveData.observe(this, openRegisterScreenObserver)
        viewModel.openContactScreenLiveData.observe(this, openContactScreenObserver)
        viewModel.errorMessage.observe(this, errorMessageObserver)
        viewModel.refreshInternetConnection.observe(this, refreshInternetConnectionObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        viewModel.progressLiveData.observe(viewLifecycleOwner, progressObserver)
        viewModel.networkConnect()
        binding.btnLogin.setOnClickListener {
            binding.btnLogin.setOnClickListener {
                viewModel.login(binding.editPhone.text(), binding.editPassword.text())
                viewModel.loginBool.observe(viewLifecycleOwner, loginBoolObserver)
            }
        }
        binding.btnSignUp.setOnClickListener {
            viewModel.openRegisterScreen()
        }
        binding.refreshBtn.setOnClickListener {
            viewModel.networkConnect()
        }
    }

    private val openRegisterScreenObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_loginScreen_to_registerScreen)
    }
    private val openContactScreenObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_loginScreen_to_contactScreen)
    }
    private val errorMessageObserver = Observer<String> {
        showToast(it)
    }
    private val loginBoolObserver = Observer<Boolean> {
        if (it) {
            viewModel.openContactScreen()
        }
    }
    private val progressObserver = Observer<Boolean> {
        if (it) {
            binding.btnLogin.visibility = View.INVISIBLE
            binding.frameLoading.isVisible = true
            binding.progress.show()
        } else {
            binding.btnLogin.visibility = View.VISIBLE
            binding.frameLoading.isVisible = false
            binding.progress.hide()
        }
    }
    private val refreshInternetConnectionObserver = Observer<Boolean> {
        binding.noConnection.isVisible = it
    }
}